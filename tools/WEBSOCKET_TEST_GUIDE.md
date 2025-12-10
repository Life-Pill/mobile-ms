# WebSocket Testing Guide

## Test WebSocket Connection through API Gateway

### 1. Test SockJS Info Endpoint (Basic CORS Test)
```bash
curl -i -H "Origin: null" http://35.208.197.159:9191/ws/info
```

**Expected Response:**
```
HTTP/1.1 200 OK
Access-Control-Allow-Origin: null
Access-Control-Allow-Credentials: true
Content-Type: application/json

{"websocket":true,"origins":["*:*"],...}
```

### 2. Test WebSocket Handshake
```bash
curl -i -N \
  -H "Connection: Upgrade" \
  -H "Upgrade: websocket" \
  -H "Sec-WebSocket-Version: 13" \
  -H "Sec-WebSocket-Key: dGhlIHNhbXBsZSBub25jZQ==" \
  -H "Origin: null" \
  http://35.208.197.159:9191/ws
```

**Expected Response:**
```
HTTP/1.1 101 Switching Protocols
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Accept: ...
Access-Control-Allow-Origin: null
```
Access to XMLHttpRequest at 'http://35.208.197.159:9191/ws/info?t=1765362281553' from origin 'null' has been blocked by CORS policy: The 'Access-Control-Allow-Origin' header contains multiple values 'null, null', but only one is allowed.Understand this error
35.208.197.159:9191/ws/info?t=1765362281553:1  Failed to load resource: net::ERR_FAILED
### 3. Test with Specific Origin (Your Frontend)
```bash
curl -i -H "Origin: http://localhost:3000" http://35.208.197.159:9191/ws/info
```

### 4. Test Direct to Notification Service (Bypass Gateway)
```bash
curl -i -H "Origin: null" http://localhost:8088/ws/info
```

---

## Common Issues

### CORS Error Despite Correct Headers
**Problem:** Browser shows CORS error even though curl works.
**Cause:** Duplicate CORS headers (both Gateway and downstream service adding headers).
**Solution:** Disable CORS in downstream service (notification-service) when using Gateway.

### 403 Forbidden
**Problem:** Gateway returns 403.
**Cause:** Origin not in allowed list.
**Fix:** Check `CORS_ALLOWED_ORIGINS` environment variable includes your origin or `*`.
