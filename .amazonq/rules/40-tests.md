# Testing
- Coverage ≥80% per module; exclude generated DTOs.
- Unit: JUnit5 + Mockito; no Spring context for pure units.
- Name: ClassNameTest#method_shouldExpectedBehavior.
- Integration: Testcontainers for DB/brokers.
- Contract: golden JSONs; verify headers/status/error paths.
- Every bug fix adds a regression test.
