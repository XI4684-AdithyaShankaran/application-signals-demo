# Microservices
- RestTemplate/WebClient must have connect/read timeouts.
- Add retry(backoff) and circuit breaker for outbound calls where appropriate.
- Expose /actuator/health and /actuator/info.
- Config in application.yml; no secrets in repo (use env/SSM/Secrets Manager).
- Propagate correlation/trace IDs across calls.
