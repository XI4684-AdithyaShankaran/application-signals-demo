# Local development
- Use `local`/`test` profiles; prefer H2/Testcontainers.
- Unit tests must not call external services (no Config Server/Eureka).
- WireMock/MockWebServer for HTTP in tests. Optional docker-compose for real deps.
