# WebSocket Gateway - Quick Reference Guide

## 🚀 Quick Start

### Connection URL
```
Production: ws://35.208.197.159:9191/ws
Local: ws://localhost:9191/ws
```

### JavaScript Client (SockJS + STOMP)

```javascript
// 1. Include dependencies
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>

// 2. Connect
const socket = new SockJS('ws://35.208.197.159:9191/ws');
const stompClient = Stomp.over(socket);

// 3. Connect with optional JWT
const headers = {
    'Authorization': 'Bearer YOUR_JWT_TOKEN' // Optional
};

stompClient.connect(headers, 
    // Success callback
    function(frame) {
        console.log('Connected:', frame);
        
        // Subscribe to topics
        stompClient.subscribe('/topic/prescriptions', function(message) {
            console.log('Message:', JSON.parse(message.body));
        });
    },
    // Error callback
    function(error) {
        console.error('Connection error:', error);
    }
);

// 4. Disconnect
stompClient.disconnect(() => {
    console.log('Disconnected');
});
```

## 📡 Available Topics

### 1. All Prescriptions (POS Systems)
```javascript
stompClient.subscribe('/topic/prescriptions', function(message) {
    const prescription = JSON.parse(message.body);
    console.log('New prescription:', prescription);
});
```

### 2. Branch-Specific Prescriptions
```javascript
const branchId = 1;
stompClient.subscribe(`/topic/branch/${branchId}/prescriptions`, function(message) {
    const prescription = JSON.parse(message.body);
    console.log(`Branch ${branchId} prescription:`, prescription);
});
```

### 3. User Response Notifications
```javascript
const userId = 123;
stompClient.subscribe(`/topic/user/${userId}/responses`, function(message) {
    const response = JSON.parse(message.body);
    console.log('Prescription response:', response);
});
```

### 4. User General Notifications
```javascript
const userId = 123;
stompClient.subscribe(`/topic/user/${userId}/notifications`, function(message) {
    const notification = JSON.parse(message.body);
    console.log('Notification:', notification);
});
```

## 🔧 Configuration Changes Made

### 1. API Gateway - RouteValidator.java
Added `/ws` to open endpoints (line 29):
```java
// WebSocket endpoints (authentication handled by WebSocket interceptor)
"/ws",
```

### 2. API Gateway - application.yml
Added WebSocket headers to CORS configuration (line 551):
```yaml
allowed-headers: Authorization,Content-Type,Accept,Origin,X-Requested-With,
  X-XSRF-TOKEN,X-Gateway-Source,Sec-WebSocket-Protocol,
  Sec-WebSocket-Extensions,Sec-WebSocket-Key,Sec-WebSocket-Version
```

### 3. WebSocket Route Already Configured
Gateway route already exists (line 450-458):
```yaml
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

## 🧪 Testing

### Option 1: HTML Test Tool
Open: `tools/websocket-gateway-test.html` in browser

### Option 2: Browser Console
```javascript
// Paste this in browser console
const socket = new SockJS('ws://35.208.197.159:9191/ws');
const stompClient = Stomp.over(socket);
stompClient.connect({}, frame => {
    console.log('✅ Connected via Gateway!');
    stompClient.subscribe('/topic/prescriptions', msg => {
        console.log('📋 Message:', msg.body);
    });
});
```

## 📋 Message Formats

### Prescription Upload Event
```json
{
    "prescriptionId": 123,
    "customerId": 456,
    "branchId": 1,
    "imageUrl": "https://...",
    "uploadedAt": "2025-12-10T10:30:00",
    "status": "PENDING"
}
```

### Prescription Response Event
```json
{
    "responseId": 789,
    "prescriptionId": 123,
    "customerId": 456,
    "branchId": 1,
    "medicines": [
        {
            "medicineId": 101,
            "medicineName": "Aspirin",
            "available": true,
            "quantity": 2
        }
    ],
    "totalAmount": 50.00,
    "respondedAt": "2025-12-10T10:35:00"
}
```

## 🔒 Security

### Optional JWT Authentication
```javascript
// Add token to connection headers
const headers = {
    'Authorization': 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'
};

stompClient.connect(headers, function(frame) {
    // Connected with authentication
});
```

### JWT Token Should Include:
- User ID
- Roles/Permissions
- Branch ID (if applicable)

## 🐛 Troubleshooting

### Connection Fails
```javascript
// Check if services are running
fetch('http://35.208.197.159:9191/actuator/health')
    .then(r => r.json())
    .then(d => console.log('Gateway:', d));
```

### Enable Debug Logging
```javascript
stompClient.debug = function(str) {
    console.log('STOMP:', str);
};
```

### Check Subscriptions
```javascript
// After connecting
console.log('Active subscriptions:', stompClient.subscriptions);
```

## 📱 Mobile Integration Examples

### React Native
```javascript
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

class WebSocketService {
    connect(token) {
        const socket = new SockJS('ws://35.208.197.159:9191/ws');
        this.client = Stomp.over(socket);
        
        const headers = token ? { 'Authorization': `Bearer ${token}` } : {};
        
        this.client.connect(headers, 
            () => this.onConnected(),
            (error) => this.onError(error)
        );
    }
    
    subscribe(topic, callback) {
        return this.client.subscribe(topic, callback);
    }
    
    disconnect() {
        if (this.client) {
            this.client.disconnect();
        }
    }
}
```

### Flutter
```dart
import 'package:stomp_dart_client/stomp_dart_client.dart';

class WebSocketService {
  late StompClient client;
  
  void connect(String? token) {
    client = StompClient(
      config: StompConfig(
        url: 'ws://35.208.197.159:9191/ws',
        webSocketConnectHeaders: {
          if (token != null) 'Authorization': 'Bearer $token',
        },
        onConnect: onConnected,
        onStompError: (error) => print('Error: $error'),
      ),
    );
    client.activate();
  }
  
  void subscribe(String topic, Function(StompFrame) callback) {
    client.subscribe(
      destination: topic,
      callback: callback,
    );
  }
}
```

## 🔗 Related Files

- **Configuration**: `api-gateway/src/main/resources/application.yml`
- **Security**: `api-gateway/src/main/java/com/lifepill/api_gateway/security/RouteValidator.java`
- **WebSocket Config**: `notification-service/src/main/java/com/lifepill/notification/config/WebSocketConfig.java`
- **Test Tool**: `tools/websocket-gateway-test.html`
- **Full Documentation**: `docs/WEBSOCKET_GATEWAY_SETUP.md`

## ✅ Checklist for Integration

- [ ] API Gateway running on port 9191
- [ ] Notification Service registered with Eureka
- [ ] WebSocket endpoint accessible: `ws://35.208.197.159:9191/ws`
- [ ] JWT token obtained (if authentication required)
- [ ] SockJS and STOMP libraries included
- [ ] Subscribed to appropriate topics
- [ ] Error handling implemented
- [ ] Connection status monitoring in place
- [ ] Reconnection logic implemented

## 📞 Support

If you encounter issues:
1. Check logs: `logs/api-gateway/` and `logs/notification-service/`
2. Verify service health: `http://35.208.197.159:9191/actuator/health`
3. Test with HTML tool: `tools/websocket-gateway-test.html`
4. Review documentation: `docs/WEBSOCKET_GATEWAY_SETUP.md`
