# WebSocket Gateway Configuration for LifePill Notification Service

## Overview
This document explains how WebSocket connections are routed through the API Gateway to the Notification Service.

## Architecture

```
Client Application
      ↓
ws://35.208.197.159:9191/ws (API Gateway)
      ↓
lb:ws://NOTIFICATION-SERVICE (Load Balanced via Eureka)
      ↓
ws://notification-service:8088/ws (Notification Service)
```

## Configuration Details

### 1. API Gateway Configuration

#### Location: `api-gateway/src/main/resources/application.yml`

```yaml
# Notification Service - WebSocket Endpoint
- id: notification-websocket
  uri: lb:ws://NOTIFICATION-SERVICE
  predicates:
    - Path=/ws/**
  filters:
    - name: CircuitBreaker
      args:
        name: notificationServiceCircuitBreaker
        fallbackUri: forward:/fallback/notification
```

**Key Points:**
- `uri: lb:ws://NOTIFICATION-SERVICE` - Uses WebSocket protocol with load balancing
- `Path=/ws/**` - Routes all /ws paths to notification service
- Circuit breaker protection included

#### CORS Configuration for WebSocket

```yaml
cors:
  allowed-origins: ${CORS_ALLOWED_ORIGINS:*}
  allowed-methods: ${CORS_ALLOWED_METHODS:GET,POST,PUT,PATCH,DELETE,OPTIONS}
  allowed-headers: ${CORS_ALLOWED_HEADERS:Authorization,Content-Type,Accept,Origin,X-Requested-With,X-XSRF-TOKEN,X-Gateway-Source,Sec-WebSocket-Protocol,Sec-WebSocket-Extensions,Sec-WebSocket-Key,Sec-WebSocket-Version}
  exposed-headers: ${CORS_EXPOSED_HEADERS:Authorization,X-Response-Time,X-Request-Id}
  allow-credentials: ${CORS_ALLOW_CREDENTIALS:false}
  max-age: ${CORS_MAX_AGE:3600}
```

**Important:** WebSocket-specific headers added:
- `Sec-WebSocket-Protocol`
- `Sec-WebSocket-Extensions`
- `Sec-WebSocket-Key`
- `Sec-WebSocket-Version`

#### Security Configuration

**Location:** `api-gateway/src/main/java/com/lifepill/api_gateway/security/RouteValidator.java`

The `/ws` endpoint is added to the open endpoints list, allowing WebSocket connections to bypass JWT validation at the gateway level:

```java
private static final List<String> OPEN_ENDPOINTS = List.of(
    // ... other endpoints
    // WebSocket endpoints (authentication handled by WebSocket interceptor)
    "/ws",
    // ... other endpoints
);
```

**Why?** WebSocket authentication is handled by the Notification Service's `JwtChannelInterceptor` after the connection is established.

### 2. Notification Service Configuration

#### Location: `notification-service/src/main/resources/application.yml`

```yaml
server:
  port: 8088

spring:
  application:
    name: NOTIFICATION-SERVICE
```

#### WebSocket Configuration

**Location:** `notification-service/src/main/java/com/lifepill/notification/config/WebSocketConfig.java`

```java
@Override
public void registerStompEndpoints(StompEndpointRegistry registry) {
    // Main WebSocket endpoint with SockJS fallback
    registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS()
            .setHeartbeatTime(25000)
            .setDisconnectDelay(5000);
    
    // Direct WebSocket endpoint (no SockJS)
    registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*");
}
```

**Supported Topics:**
- `/topic/prescriptions` - All new prescriptions (for POS)
- `/topic/branch/{branchId}/prescriptions` - Branch-specific prescriptions
- `/topic/user/{userId}/responses` - User-specific prescription responses
- `/topic/user/{userId}/notifications` - User-specific notifications

#### JWT Authentication

**Location:** `notification-service/src/main/java/com/lifepill/notification/security/JwtChannelInterceptor.java`

WebSocket connections are authenticated using JWT tokens in the STOMP CONNECT frame headers.

## Connection Flow

### 1. Client Connects to API Gateway
```javascript
const socket = new SockJS('ws://35.208.197.159:9191/ws');
const stompClient = Stomp.over(socket);
```

### 2. API Gateway Routes to Notification Service
- Request matches `/ws/**` predicate
- Gateway forwards to `lb:ws://NOTIFICATION-SERVICE`
- Eureka resolves to actual notification-service instance

### 3. Notification Service Handles Connection
- SockJS handshake completes
- STOMP protocol layer established
- JWT validation (if token provided in headers)

### 4. Client Subscribes to Topics
```javascript
// Subscribe to branch-specific prescriptions
stompClient.subscribe('/topic/branch/1/prescriptions', function(message) {
    console.log('Received:', message.body);
});
```

## Testing

### Using the Test Tool

A test HTML page is provided: `tools/websocket-gateway-test.html`

**Features:**
- Connect via API Gateway
- Subscribe to various topics
- Real-time message logging
- JWT token support
- Connection status monitoring

**To use:**
1. Open `tools/websocket-gateway-test.html` in a browser
2. Verify the URL is set to: `ws://35.208.197.159:9191/ws`
3. Click "Connect via Gateway"
4. Subscribe to desired topics

### Using JavaScript Client

```javascript
// Connect through API Gateway
const socket = new SockJS('ws://35.208.197.159:9191/ws');
const stompClient = Stomp.over(socket);

// Optional: Add JWT token
const headers = {
    'Authorization': 'Bearer YOUR_JWT_TOKEN_HERE'
};

stompClient.connect(headers, function(frame) {
    console.log('Connected: ' + frame);
    
    // Subscribe to topics
    stompClient.subscribe('/topic/prescriptions', function(message) {
        const data = JSON.parse(message.body);
        console.log('New prescription:', data);
    });
}, function(error) {
    console.error('Connection error:', error);
});
```

## Endpoints

| Service | Direct URL | Via Gateway URL |
|---------|-----------|----------------|
| WebSocket | `ws://localhost:8088/ws` | `ws://35.208.197.159:9191/ws` |
| WebSocket (Docker) | `ws://notification-service:8088/ws` | `ws://35.208.197.159:9191/ws` |

## Troubleshooting

### Connection Refused
**Symptom:** Cannot connect to WebSocket
**Solutions:**
1. Verify API Gateway is running on port 9191
2. Check Notification Service is registered with Eureka
3. Verify `/ws` is in RouteValidator open endpoints

### 401 Unauthorized
**Symptom:** Connection drops immediately
**Solutions:**
1. Provide valid JWT token in connection headers
2. Verify JWT secret matches between services
3. Check JwtChannelInterceptor configuration

### Messages Not Received
**Symptom:** Connected but no messages
**Solutions:**
1. Verify subscription topic path is correct
2. Check RabbitMQ is running and connected
3. Verify PrescriptionEventListener is processing events

### CORS Errors
**Symptom:** Browser blocks connection
**Solutions:**
1. Verify CORS configuration includes WebSocket headers
2. Check `allowed-origins` includes your client domain
3. Ensure `setAllowedOriginPatterns("*")` in WebSocketConfig

## Security Considerations

### Gateway Level
- WebSocket upgrade requests bypass JWT filter
- CORS headers properly configured for WebSocket
- Circuit breaker protection enabled

### Service Level
- JWT validation on STOMP CONNECT frame
- Channel interceptor validates all messages
- Topic-level authorization (future enhancement)

## Monitoring

### Health Checks
```bash
# API Gateway health
curl http://35.208.197.159:9191/actuator/health

# Notification Service health
curl http://localhost:8088/actuator/health
```

### Metrics
- Circuit breaker status: `/actuator/circuitbreakers`
- Gateway routes: `/actuator/gateway/routes`
- WebSocket connections: Check notification-service logs

## Environment Variables

### API Gateway
```env
CORS_ALLOWED_ORIGINS=*
CORS_ALLOWED_HEADERS=Authorization,Content-Type,Accept,Origin,X-Requested-With,X-XSRF-TOKEN,X-Gateway-Source,Sec-WebSocket-Protocol,Sec-WebSocket-Extensions,Sec-WebSocket-Key,Sec-WebSocket-Version
```

### Notification Service
```env
CORS_ORIGINS=*
JWT_SECRET=your_jwt_secret_here
```

## Production Recommendations

1. **Use WSS (WebSocket Secure)** in production
   - Configure SSL/TLS certificates
   - Update URLs to `wss://` instead of `ws://`

2. **Restrict CORS Origins**
   - Change `allowed-origins: *` to specific domains
   - Example: `allowed-origins: https://app.lifepill.com`

3. **Enable Authentication**
   - Always require JWT tokens in production
   - Implement token refresh mechanism

4. **Configure Load Balancing**
   - Enable sticky sessions for WebSocket
   - Use Redis for session persistence

5. **Monitor Connection Pool**
   - Set appropriate connection limits
   - Monitor active WebSocket connections
   - Implement connection timeout policies

## References

- Spring Cloud Gateway WebSocket: https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#websocket-routing-filter
- STOMP Protocol: https://stomp.github.io/
- SockJS: https://github.com/sockjs/sockjs-client
