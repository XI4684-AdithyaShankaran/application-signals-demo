# Containerization
- Base temurin:17-jre or distroless; non-root.
- Minimal layers; clean caches.
- HEALTHCHECK -> /actuator/health; correct exposed port.
- Config via env/ConfigMap/Secret; never bake secrets.
