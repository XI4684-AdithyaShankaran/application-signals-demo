# REST clients
- RestTemplate: set connect/read timeouts; use ResponseEntity<T>; handle null body.
- WebClient: per-request timeout; retry(backoff, limited); circuit breaker (Resilience4j).
- Use DTOs for (de)serialization; don’t leak internal models.
- Tests: MockWebServer/WireMock; include error-path cases/timeouts.
