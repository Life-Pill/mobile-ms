# Prescription Notification System - Verification Workflow

## Quick Start

### 1. Open POS Test Client
Open in browser: `file:///home/pramithajayasooriya/mobile-ms/tools/pos-websocket-test.html`
Or run: `python3 -m http.server 3000 --directory /home/pramithajayasooriya/mobile-ms/tools`
Then visit: `http://localhost:3000/pos-websocket-test.html`

### 2. Upload a Prescription
```bash
curl -X POST http://localhost:9191/lifepill/v1/prescription/upload \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "image=@/path/to/prescription.jpg" \
  -F "notes=Test from curl" \
  -F "userId=YOUR_USER_ID"
```

### 3. Watch Notification Appear in POS Test Client

---

## Verification Checklist

### ✅ Step 1: Check Services Running
```bash
docker compose ps | grep -E "prescription|notification|rabbitmq"
```

### ✅ Step 2: Verify RabbitMQ Queues
```bash
curl -s -u admin:rabbitmq123 http://localhost:15672/api/queues | \
  jq '.[] | select(.name | contains("prescription")) | {name, consumers, messages}'
```
Expected: `consumers > 0`

### ✅ Step 3: Check WebSocket Endpoint (via API Gateway)
```bash
curl http://35.208.197.159:9191/ws/info
```
Expected: `{"entropy":..., "websocket":true}`

### ✅ Step 4: Check Notification Service Status (via API Gateway)
```bash
curl http://35.208.197.159:9191/api/v1/notifications/status | jq .
```
Expected: `"fcmEnabled": true, "databaseConnected": true`

### ✅ Step 5: View Notification Service Logs (After Upload)
```bash
docker logs lifepill-notification-service --tail 20 | grep -i "broadcast\|prescription"
```

---

## Architecture Flow

```
┌─────────────┐     ┌─────────────┐     ┌────────────────┐
│ Mobile App  │────▶│ API Gateway │────▶│ Prescription   │
│ (Upload)    │     │ :9191       │     │ Service :8087  │
└─────────────┘     └─────────────┘     └───────┬────────┘
                                                 │
                                    RabbitMQ Exchange
                                    "prescription.exchange"
                                                 │
                    ┌───────────────────────────┼───────────────────────────┐
                    │                           │                           │
                    ▼                           ▼                           ▼
        ┌───────────────────┐     ┌───────────────────┐     ┌───────────────────┐
        │ prescription.     │     │ prescription.     │     │ prescription.     │
        │ notification.queue│     │ user.notification │     │ order.queue       │
        │ (POS Notifications│     │ .queue (Mobile)   │     │ (Order Service)   │
        └─────────┬─────────┘     └─────────┬─────────┘     └───────────────────┘
                  │                         │
                  ▼                         ▼
        ┌───────────────────────────────────────────────┐
        │           Notification Service :8088           │
        │  ┌─────────────┐ ┌─────────────┐ ┌──────────┐ │
        │  │  WebSocket  │ │   Redis     │ │   FCM    │ │
        │  │  /ws        │ │   Pub/Sub   │ │  Push    │ │
        │  └──────┬──────┘ └─────────────┘ └──────────┘ │
        └─────────┼─────────────────────────────────────┘
                  │
                  ▼
        ┌───────────────────┐
        │ POS System        │
        │ (WebSocket Client)│
        │ Subscribes to:    │
        │ /topic/prescriptions
        └───────────────────┘
```

---

## WebSocket Topics

| Topic | Purpose | Subscriber |
|-------|---------|------------|
| `/topic/prescriptions` | All new prescriptions | All POS systems |
| `/topic/branch/{branchId}/prescriptions` | Branch-specific | Specific POS |
| `/topic/user/{userId}/responses` | Branch responses | Mobile user |
| `/topic/user/{userId}/notifications` | General notifications | Mobile user |

---

## Troubleshooting

### No messages received?
1. Check notification-service logs: `docker logs lifepill-notification-service --tail 50`
2. Check RabbitMQ connection: Logs should show "Created new connection"
3. Verify WebSocket connected in browser console

### 403 Error on prescription upload?
- Fixed by setting null role requirement for prescription endpoints
- Rebuild API Gateway if needed

### WebSocket connection fails?
- Check CORS: `curl http://localhost:8088/ws/info`
- Verify notification-service is running
