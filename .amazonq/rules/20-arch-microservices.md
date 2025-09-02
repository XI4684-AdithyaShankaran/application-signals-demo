# Microservices architecture
- RestTemplate with connect/read timeouts + Resilience4j.
- Prefer WebClient for new code: timeout + retry(backoff) + circuit breaker.
- Expose /actuator/health, /actuator/info.
- Config in application.yml; no secrets (env/SSM/Secrets Manager).
