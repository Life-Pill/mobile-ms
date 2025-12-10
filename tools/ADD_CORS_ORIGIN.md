# How to Add CORS Origin for Pharmacy Frontend

## Steps:

### 1. Edit your `.env` file
```bash
nano /home/pramithajayasooriya/mobile-ms/.env
```

### 2. Find the line starting with `CORS_ORIGINS=`

### 3. Add the new URL to the list (comma-separated):
```bash
CORS_ORIGINS=http://34.59.6.212,http://34.59.6.212:9191,http://34.59.6.212:8080,http://34.59.6.212:8070,http://34.59.6.212:8081,http://34.59.6.212:8082,http://34.59.6.212:8085,http://34.59.6.212:8086,http://34.59.6.212:3001,http://localhost,http://localhost:3000,http://localhost:5173,https://pharmacyone.lifepill.devnerd.online
```

**Note:** Remove the trailing `/` from the URL (use `https://pharmacyone.lifepill.devnerd.online` without the slash)

### 4. Restart API Gateway to apply changes:
```bash
cd /home/pramithajayasooriya/mobile-ms
docker compose restart api-gateway
```

### 5. Verify the change:
```bash
curl -i -H "Origin: https://pharmacyone.lifepill.devnerd.online" \
  http://35.208.197.159:9191/lifepill/v1/auth/authenticate \
  -X POST -H "Content-Type: application/json" \
  -d '{"employerEmail":"test@test.com","employerPassword":"test"}'
```

**Expected output:**
```
Access-Control-Allow-Origin: https://pharmacyone.lifepill.devnerd.online
Access-Control-Allow-Credentials: true
```

---

## Alternative: Quick Command

Run this to add the URL automatically:
```bash
cd /home/pramithajayasooriya/mobile-ms
# Backup first
cp .env .env.backup
# Add the new origin
sed -i 's|CORS_ORIGINS=\(.*\)|CORS_ORIGINS=\1,https://pharmacyone.lifepill.devnerd.online|' .env
# Restart gateway
docker compose restart api-gateway
```
