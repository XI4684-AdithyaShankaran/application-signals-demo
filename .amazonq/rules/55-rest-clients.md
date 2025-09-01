# REST clients
- RestTemplate: set connect/read timeouts; use ResponseEntity<T>; null body safe.
- WebClient: per-request timeout; retry(backoff, bounded); circuit breaker.
- Use DTOs; do not leak internal models.
- Tests: MockWebServer/WireMock; include error/timeouts.
