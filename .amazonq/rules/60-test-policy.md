# Testing policy
- Coverage ≥80% per module (exclude generated DTOs).
- Unit: JUnit5 + Mockito; no Spring context for pure units.
- Integration: Testcontainers for DB/brokers.
- Contract tests: golden JSON; verify headers/status/error.
- Bug fix → add regression test.
