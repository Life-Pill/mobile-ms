package com.lifepill.identityservice.service.impl;

import com.lifepill.identityservice.config.JwtService;
import com.lifepill.identityservice.dto.response.CachedEmployerDTO;
import com.lifepill.identityservice.dto.response.CachedSessionResponseDTO;
import com.lifepill.identityservice.entity.Employer;
import com.lifepill.identityservice.exception.AuthenticationException;
import com.lifepill.identityservice.exception.NotFoundException;
import com.lifepill.identityservice.repository.EmployerRepository;
import com.lifepill.identityservice.security.EmployerUserDetails;
import com.lifepill.identityservice.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Implementation of SessionService for managing employer sessions with Redis caching.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SessionServiceImpl implements SessionService {

    private static final String SESSION_KEY_PREFIX = "employer:session:";
    private static final String TOKEN_KEY_PREFIX = "employer:token:";
    private static final long SESSION_EXPIRY_HOURS = 24;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private final RedisTemplate<String, Object> redisTemplate;
    private final EmployerRepository employerRepository;
    private final JwtService jwtService;

    @Override
    public CachedEmployerDTO cacheEmployerSession(String employerEmail, String accessToken, String refreshToken) {
        Employer employer = employerRepository.findByEmployerEmail(employerEmail)
                .orElseThrow(() -> new NotFoundException("Employer not found: " + employerEmail));

        long currentTime = System.currentTimeMillis();
        long expiresAt = currentTime + TimeUnit.HOURS.toMillis(SESSION_EXPIRY_HOURS);
        
        CachedEmployerDTO cachedEmployer = CachedEmployerDTO.builder()
                .employerId(employer.getEmployerId())
                .employerNicName(employer.getEmployerNicName())
                .employerFirstName(employer.getEmployerFirstName())
                .employerLastName(employer.getEmployerLastName())
                .employerEmail(employer.getEmployerEmail())
                .employerPhone(employer.getEmployerPhone())
                .employerAddress(employer.getEmployerAddress())
                .employerSalary(employer.getEmployerSalary())
                .employerNic(employer.getEmployerNic())
                .role(employer.getRole().name())
                .branchId(employer.getBranchId())
                .activeStatus(employer.isActiveStatus())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .loginTimestamp(currentTime)
                .lastActivityTimestamp(currentTime)
                .expiresAt(expiresAt)
                .revoked(false)
                .gender(employer.getGender() != null ? employer.getGender().name() : null)
                .dateOfBirth(employer.getDateOfBirth() != null ? DATE_FORMAT.format(employer.getDateOfBirth()) : null)
                .pin(employer.getPin())
                .build();

        String sessionKey = SESSION_KEY_PREFIX + employerEmail;
        String tokenKey = TOKEN_KEY_PREFIX + accessToken;

        // Store session by email
        redisTemplate.opsForValue().set(sessionKey, cachedEmployer, SESSION_EXPIRY_HOURS, TimeUnit.HOURS);
        
        // Store mapping from token to email for quick lookup
        if (accessToken != null) {
            redisTemplate.opsForValue().set(tokenKey, employerEmail, SESSION_EXPIRY_HOURS, TimeUnit.HOURS);
        }

        log.info("Cached session for employer: {}", employerEmail);
        return cachedEmployer;
    }

    @Override
    public Optional<CachedEmployerDTO> getCachedEmployer(String employerEmail) {
        String sessionKey = SESSION_KEY_PREFIX + employerEmail;
        Object cached = redisTemplate.opsForValue().get(sessionKey);
        
        if (cached instanceof CachedEmployerDTO) {
            return Optional.of((CachedEmployerDTO) cached);
        }
        
        // Try to convert from LinkedHashMap if Redis serialization issue
        if (cached instanceof Map) {
            try {
                Map<String, Object> map = (Map<String, Object>) cached;
                CachedEmployerDTO dto = mapToCachedEmployerDTO(map);
                return Optional.of(dto);
            } catch (Exception e) {
                log.warn("Failed to convert cached object: {}", e.getMessage());
            }
        }
        
        return Optional.empty();
    }

    @Override
    public List<CachedSessionResponseDTO> getAllCachedEmployers() {
        Set<String> keys = redisTemplate.keys(SESSION_KEY_PREFIX + "*");
        
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }

        return keys.stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .filter(Objects::nonNull)
                .map(this::convertToSessionResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public CachedSessionResponseDTO authenticateFromCache(String username, Integer pin) {
        // First check if session is cached and valid
        Optional<CachedEmployerDTO> cached = getCachedEmployer(username);
        
        if (cached.isPresent()) {
            CachedEmployerDTO cachedEmployer = cached.get();
            
            // Check if session is revoked or expired
            if (cachedEmployer.isRevoked()) {
                throw new AuthenticationException("Session has been revoked");
            }
            if (cachedEmployer.getExpiresAt() < System.currentTimeMillis()) {
                throw new AuthenticationException("Session has expired");
            }
            
            // Update last activity
            updateLastActivity(username);
            log.info("Authenticated from cache: {}", username);
            return convertToSessionResponse(cachedEmployer);
        }
        
        // If not in cache, validate PIN against DB and cache the session
        Employer employer = employerRepository.findByEmployerEmail(username)
                .orElseThrow(() -> new AuthenticationException("User not found: " + username));
        
        // Validate PIN
        if (employer.getPin() != pin) {
            throw new AuthenticationException("Invalid PIN");
        }
        
        // Update active status
        employer.setActiveStatus(true);
        employerRepository.save(employer);
        
        // Generate new tokens and cache session
        EmployerUserDetails userDetails = new EmployerUserDetails(employer);
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        
        log.info("Authenticated from DB and cached: {}", username);
        CachedEmployerDTO cachedDto = cacheEmployerSession(username, accessToken, refreshToken);
        return convertToSessionResponse(cachedDto);
    }

    @Override
    public void temporaryLogout(String employerEmail) {
        // Temporary logout: KEEP cache data, only update DB status
        // User can still re-login using cached session with PIN
        
        // Update DB to set inactive
        Employer employer = employerRepository.findByEmployerEmail(employerEmail)
                .orElseThrow(() -> new NotFoundException("Employer not found: " + employerEmail));
        
        employer.setActiveStatus(false);
        employerRepository.save(employer);
        
        // Update cache to reflect inactive status but DON'T delete cache
        Optional<CachedEmployerDTO> cached = getCachedEmployer(employerEmail);
        if (cached.isPresent()) {
            CachedEmployerDTO cachedEmployer = cached.get();
            cachedEmployer.setActiveStatus(false);
            // Keep the session in cache so user can re-login with PIN
            String sessionKey = SESSION_KEY_PREFIX + employerEmail;
            redisTemplate.opsForValue().set(sessionKey, cachedEmployer, SESSION_EXPIRY_HOURS, TimeUnit.HOURS);
        }
        
        log.info("Temporary logout for employer: {} - cache data preserved for PIN re-login", employerEmail);
    }

    @Override
    public void permanentLogout(String employerEmail) {
        // Permanent logout: REMOVE cache data completely, user cannot re-login with cached data
        String sessionKey = SESSION_KEY_PREFIX + employerEmail;
        
        // Get cached session to delete token mapping
        Optional<CachedEmployerDTO> cached = getCachedEmployer(employerEmail);
        if (cached.isPresent()) {
            CachedEmployerDTO cachedEmployer = cached.get();
            
            // Delete token mapping
            if (cachedEmployer.getAccessToken() != null) {
                String tokenKey = TOKEN_KEY_PREFIX + cachedEmployer.getAccessToken();
                redisTemplate.delete(tokenKey);
            }
        }
        
        // Delete the session from cache completely
        redisTemplate.delete(sessionKey);
        
        // Update DB to set inactive
        Employer employer = employerRepository.findByEmployerEmail(employerEmail)
                .orElseThrow(() -> new NotFoundException("Employer not found: " + employerEmail));
        
        employer.setActiveStatus(false);
        employerRepository.save(employer);
        
        log.info("Permanent logout for employer: {} - session revoked", employerEmail);
    }

    @Override
    public void updateLastActivity(String employerEmail) {
        String sessionKey = SESSION_KEY_PREFIX + employerEmail;
        Optional<CachedEmployerDTO> cached = getCachedEmployer(employerEmail);
        
        if (cached.isPresent()) {
            CachedEmployerDTO employer = cached.get();
            employer.setLastActivityTimestamp(System.currentTimeMillis());
            redisTemplate.opsForValue().set(sessionKey, employer, SESSION_EXPIRY_HOURS, TimeUnit.HOURS);
        }
    }

    @Override
    public boolean isSessionCached(String employerEmail) {
        String sessionKey = SESSION_KEY_PREFIX + employerEmail;
        return Boolean.TRUE.equals(redisTemplate.hasKey(sessionKey));
    }

    @Override
    public boolean isSessionValid(String employerEmail) {
        Optional<CachedEmployerDTO> cached = getCachedEmployer(employerEmail);
        
        if (cached.isEmpty()) {
            return false;
        }
        
        CachedEmployerDTO employer = cached.get();
        
        // Check if revoked
        if (employer.isRevoked()) {
            return false;
        }
        
        // Check if expired
        if (employer.getExpiresAt() < System.currentTimeMillis()) {
            return false;
        }
        
        return true;
    }

    @Override
    public void cleanupExpiredSessions() {
        // Redis handles expiry automatically via TTL
        // This method can be used for additional cleanup logic if needed
        log.info("Session cleanup triggered - Redis handles TTL automatically");
    }

    @Override
    public Optional<CachedSessionResponseDTO> getCachedSession(String employerEmail) {
        Optional<CachedEmployerDTO> cached = getCachedEmployer(employerEmail);
        return cached.map(this::convertToSessionResponse);
    }

    /**
     * Converts cached employer data to session response format.
     */
    private CachedSessionResponseDTO convertToSessionResponse(Object cached) {
        CachedEmployerDTO dto;
        
        if (cached instanceof CachedEmployerDTO) {
            dto = (CachedEmployerDTO) cached;
        } else if (cached instanceof Map) {
            try {
                dto = mapToCachedEmployerDTO((Map<String, Object>) cached);
            } catch (Exception e) {
                log.warn("Failed to convert cached object: {}", e.getMessage());
                return null;
            }
        } else {
            return null;
        }
        
        return CachedSessionResponseDTO.builder()
                .authenticationResponse(CachedSessionResponseDTO.AuthenticationResponsePart.builder()
                        .message("Session active")
                        .accessToken(dto.getAccessToken())
                        .refreshToken(dto.getRefreshToken())
                        .build())
                .employerDetails(CachedSessionResponseDTO.EmployerDetailsPart.builder()
                        .employerId(dto.getEmployerId())
                        .branchId(dto.getBranchId())
                        .employerNicName(dto.getEmployerNicName())
                        .employerFirstName(dto.getEmployerFirstName())
                        .employerLastName(dto.getEmployerLastName())
                        .employerEmail(dto.getEmployerEmail())
                        .employerPhone(dto.getEmployerPhone())
                        .employerAddress(dto.getEmployerAddress())
                        .employerSalary(dto.getEmployerSalary())
                        .employerNic(dto.getEmployerNic())
                        .gender(dto.getGender())
                        .dateOfBirth(dto.getDateOfBirth())
                        .role(dto.getRole())
                        .pin(dto.getPin())
                        .profileImage(null)
                        .activeStatus(dto.isActiveStatus())
                        .build())
                .revoked(dto.isRevoked())
                .expiresAt(dto.getExpiresAt())
                .build();
    }

    /**
     * Converts a Map to CachedEmployerDTO (for Redis deserialization issues).
     */
    private CachedEmployerDTO mapToCachedEmployerDTO(Map<String, Object> map) {
        return CachedEmployerDTO.builder()
                .employerId(getLong(map, "employerId"))
                .employerNicName((String) map.get("employerNicName"))
                .employerFirstName((String) map.get("employerFirstName"))
                .employerLastName((String) map.get("employerLastName"))
                .employerEmail((String) map.get("employerEmail"))
                .employerPhone((String) map.get("employerPhone"))
                .employerAddress((String) map.get("employerAddress"))
                .employerSalary(getDouble(map, "employerSalary"))
                .employerNic((String) map.get("employerNic"))
                .role((String) map.get("role"))
                .branchId(getLong(map, "branchId"))
                .activeStatus(getBoolean(map, "activeStatus"))
                .accessToken((String) map.get("accessToken"))
                .refreshToken((String) map.get("refreshToken"))
                .loginTimestamp(getLongPrimitive(map, "loginTimestamp"))
                .lastActivityTimestamp(getLongPrimitive(map, "lastActivityTimestamp"))
                .expiresAt(getLongPrimitive(map, "expiresAt"))
                .revoked(getBoolean(map, "revoked"))
                .gender((String) map.get("gender"))
                .dateOfBirth((String) map.get("dateOfBirth"))
                .pin(getInteger(map, "pin"))
                .build();
    }

    private Long getLong(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }

    private long getLongPrimitive(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return 0L;
    }

    private Double getDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return null;
    }

    private Integer getInteger(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }

    private boolean getBoolean(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }
}
