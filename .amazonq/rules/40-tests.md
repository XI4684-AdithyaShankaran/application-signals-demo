# Testing (unit/integration/contract)
- Coverage ≥ 80% per module (exclude generated DTOs).
- Unit tests: JUnit5 + Mockito; no Spring context for pure units.
- Name: ClassNameTest#method_shouldExpectedBehavior.
- Mock external HTTP (WireMock/MockWebServer); assert logs/metrics when relevant.
- Integration: Testcontainers for DB/brokers.
- Contract tests: golden JSONs; verify headers, status, and error paths.
- Every bug fix adds a regression test; tests run offline.
- In tests: disable Config Server/Eureka.
