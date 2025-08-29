## Testing
- Unit coverage ≥ 80% per module (exclude generated DTOs).
- Testcontainers for integration tests.
- Every bug fix adds a regression test.
- Unit tests run offline: mock all external HTTP.
- In tests: disable Config Server/Eureka.