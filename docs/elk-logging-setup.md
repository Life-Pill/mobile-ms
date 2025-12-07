# Centralized Logging with ELK Stack - Setup Guide

## Overview
Complete production-ready centralized logging for all microservices using Elasticsearch, Kibana, and Filebeat.

**What's Included:**
- ✅ Elasticsearch 8.11 - Log storage & indexing
- ✅ Kibana 8.11 - Log visualization & dashboards
- ✅ Filebeat 8.11 - Auto-collect from ALL containers
- ✅ Docker metadata enrichment (name, ID, labels)
- ✅ JSON log parsing
- ✅ Single-node configuration (production-ready)

---

## Quick Start

###  1. Start ELK Stack

```bash
cd /home/pramithajayasooriya/mobile-ms

# Start just ELK services
docker compose up -d elasticsearch kibana filebeat

# Or start everything including existing services
docker compose up -d
```

### 2. Access Kibana

**URL:** http://35.208.197.159:5601

**Wait:** ~2-3 minutes for Elasticsearch & Kibana to fully start

### 3. Create Index Pattern

1. Open Kibana: http://35.208.197.159:5601
2. Navigate to **Stack Management** → **Index Patterns**
3. Click **Create index pattern**
4. Enter: `filebeat-*`
5. Select time field: `@timestamp`
6. Click **Create index pattern**

### 4. View Logs

1. Go to **Discover** in left menu
2. Select `filebeat-*` index pattern
3. You'll see logs from ALL containers!

---

## Verify Everything Works

### Check Elasticsearch Health
```bash
curl http://localhost:9200/_cluster/health?pretty
```

**Expected Output:**
```json
{
  "cluster_name" : "lifepill-logs",
  "status" : "green" or "yellow",
  "number_of_nodes" : 1
}
```

### Check Filebeat Status
```bash
docker logs lifepill-filebeat --tail=50
```

**Look for:** `Connection to backoff(elasticsearch(http://elasticsearch:9200)) established`

### Check Log Collection
```bash
# Check index exists
curl http://localhost:9200/_cat/indices?v

# Count documents
curl http://localhost:9200/filebeat-*/_count

# Sample logs
curl "http://localhost:9200/filebeat-*/_search?pretty&size=5"
```

---

## Available Log Fields

Filebeat enriches every log with:

| Field | Description | Example |
|-------|-------------|---------|
| `container.name` | Container name | `lifepill-identity-service` |
| `container.id` | Container ID | `abc123...` |
| `container.image.name` | Image name | `mobile-ms-identity-service` |
| `container.labels` | Docker labels | Service metadata |
| `@timestamp` | Log timestamp | `2024-12-07T12:00:00.000Z` |
| `message` | Log message | Actual log content |
| `log.level` | Log level | `INFO`, `ERROR`, `WARN` |
| `stream` | Output stream | `stdout`, `stderr` |

---

## Kibana Queries

### Filter by Service
```
container.name: "lifepill-identity-service"
```

### Filter by Log Level
```
log.level: "ERROR"
```

### Search for Errors
```
message: "exception" OR message: "error"
```

### Multiple Services
```
container.name: ("lifepill-identity-service" OR "lifepill-api-gateway")
```

### Time Range + Service
```
container.name: "lifepill-branch-service" AND @timestamp > now-1h
```

---

## Create Dashboards

### 1. Error Dashboard

1. Go to **Dashboard** → **Create dashboard**
2. Add **Visualization** → **Pie Chart**
   - **Metrics:** Count
   - **Buckets:** Terms → `log.level`
3. Add **Visualization** → **Data Table**
   - **Columns:** `container.name`, `message`, `@timestamp`
   - **Filter:** `log.level: ERROR`

### 2. Service Health Dashboard

1. Add **Visualization** → **Line Chart**
   - **Y-axis:** Count of events
   - **X-axis:** Date Histogram on `@timestamp`
   - **Split series:** `container.name`

---

## Useful Commands

### Start/Stop Services
```bash
# Start ELK stack only
docker compose up -d elasticsearch kibana filebeat

# Stop ELK stack
docker compose stop elasticsearch kibana filebeat

# Restart Filebeat (to reload config)
docker compose restart filebeat

# View logs
docker compose logs elasticsearch
docker compose logs kibana
docker compose logs filebeat
```

### Cleanup Old Logs
```bash
# Delete indices older than 7 days
curl -X DELETE "http://localhost:9200/filebeat-$(date -d '7 days ago' +%Y.%m.%d)"

# Delete all filebeat indices
curl -X DELETE "http://localhost:9200/filebeat-*"
```

### Check Disk Usage
```bash
# Elasticsearch disk usage
curl http://localhost:9200/_cat/allocation?v

# Docker volume size
docker system df -v | grep elasticsearch
```

---

## Troubleshooting

### Elasticsearch won't start
**Issue:** `max virtual memory areas vm.max_map_count [65530] is too low`

**Fix:**
```bash
sudo sysctl -w vm.max_map_count=262144
echo "vm.max_map_count=262144" | sudo tee -a /etc/sysctl.conf
```

### No logs appearing in Kibana

**Check 1:** File beat running?
```bash
docker ps | grep filebeat
```

**Check 2:** Filebeat connected to Elasticsearch?
```bash
docker logs lifepill-filebeat | grep -i "connection\|error"
```

**Check 3:** Containers generating logs?
```bash
docker logs lifepill-identity-service --tail=10
```

**Check 4:** Index pattern correct?
```bash
curl http://localhost:9200/_cat/indices | grep filebeat
```

### Kibana "Elasticsearch service is not available"

**Fix:** Wait longer (can take 2-3 minutes) or restart:
```bash
docker compose restart elasticsearch kibana
```

### High disk usage

**Monitor:**
```bash
# Check index sizes
curl http://localhost:9200/_cat/indices?v&h=index,store.size&s=store.size:desc

# Delete old indices (older than 7 days)
curator_cli --host localhost delete-indices --filter_list '
[
  {"filtertype": "pattern", "kind": "prefix", "value": "filebeat-"},
  {"filtertype": "age", "source": "name", "direction": "older", "timestring": "%Y.%m.%d", "unit": "days", "unit_count": 7}
]'
```

---

## Performance Tuning

### Reduce Elasticsearch Memory
Edit `docker-compose.yml`:
```yaml
- "ES_JAVA_OPTS=-Xms512m -Xmx512m"  # Reduce from 1g to 512m
```

### Limit Log Retention
Edit `filebeat.yml` to add:
```yaml
setup.ilm.enabled: true
setup.ilm.policy_name: "lifepill-logs"
setup.ilm.rollover_alias: "filebeat"
setup.ilm.pattern: "{now/d}-000001"
```

### Exclude Noisy Containers
Edit `filebeat.yml`:
```yaml
filebeat.autodiscover:
  providers:
    - type: docker
      hints.enabled: true
      # Exclude specific containers
      templates:
        - condition:
            equals:
              docker.container.name: "lifepill-redis"
          config:
            - type: container
              enabled: false
```

---

## Ports

| Service | Port | URL |
|---------|------|-----|
| Elasticsearch | 9200 | http://35.208.197.159:9200 |
| Elasticsearch cluster | 9300 | Internal only |
| Kibana | 5601 | http://35.208.197.159:5601 |

---

## Next Steps

1. ✅ Create custom dashboards for each microservice
2. ✅ Set up alerting rules for errors
3. ✅ Configure log rotation policy
4. ✅ Export dashboard configurations
5. ⚠️ Optional: Add Grafana for metrics + logs view
6. ⚠️ Optional: Switch to Loki if Elasticsearch is too heavy

---

## Resources

**Filebeat Docs:** https://www.elastic.co/guide/en/beats/filebeat/current/index.html  
**Kibana Docs:** https://www.elastic.co/guide/en/kibana/current/index.html  
**Elasticsearch Docs:** https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html

---

## Status Check
```bash
# All ELK services healthy
docker compose ps elasticsearch kibana filebeat

# Quick health check
curl -s http://localhost:9200/_cluster/health | jq .
curl -s http://localhost:5601/api/status | jq .status
```

**Everything working?** You should see logs in Kibana within 1-2 minutes of services starting! 🎉
