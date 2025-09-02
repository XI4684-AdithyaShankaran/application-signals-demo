# REST clients
- RestTemplate: connect/read timeouts; ResponseEntity<T>; handle null body.
- WebClient: per-request timeout + retry(backoff, bounded) + circuit breaker.
- DTOs for (de)serialization; no internal models leakage.
- Tests: WireMock/MockWebServer; include error/timeout paths.
