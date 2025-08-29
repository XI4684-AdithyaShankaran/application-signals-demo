# Containerization
- Base: temurin:17-jre or distroless; run as non-root.
- Minimal layers; clean build caches; only required files in image.
- HEALTHCHECK hits /actuator/health; correct EXPOSE/port docs.
- Pass config via env/ConfigMap/Secret; no files baked with secrets.
