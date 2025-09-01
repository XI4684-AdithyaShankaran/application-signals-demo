# Containerization
- Base: temurin:17-jre or distroless; run as non-root.
- Minimal layers; no build caches or secrets in image.
- HEALTHCHECK → /actuator/health; correct EXPOSE.
