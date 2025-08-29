## Microservices
- Expose /actuator/health and /actuator/info.
- Externalize config in application.yml; no secrets in repo.
- Set HTTP client timeouts and Resilience4j fallbacks for remote calls.
- Use Feign/WebClient with circuit breaker for interservice calls.
