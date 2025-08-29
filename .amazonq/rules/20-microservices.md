## Microservices
- Existing calls: RestTemplate (timeouts + Resilience4j).
- New calls: prefer WebClient (+ circuit breaker / retries).
- Expose /actuator/health and /actuator/info.
- Config in application.yml; no secrets in repo.