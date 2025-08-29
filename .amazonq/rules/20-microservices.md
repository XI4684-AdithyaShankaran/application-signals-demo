# Microservices
- Existing clients: RestTemplate with connect/read timeouts + Resilience4j.
- New clients: WebClient with timeouts + retry(backoff) + circuit breaker.
- Expose /actuator/health and /actuator/info; readiness uses health.
- Config from application.yml; no secrets in repo (env/SSM/Secrets Manager).
- Stable error model (code/message/correlationId); propagate correlation/trace IDs.
