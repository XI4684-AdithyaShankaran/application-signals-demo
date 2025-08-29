# Local development
- Use 'local'/'test' profiles; prefer H2/Testcontainers for deps.
- Disable Config Server/Eureka for unit tests; avoid external calls.
- WireMock/MockWebServer for HTTP in tests.
- docker-compose only for true local infra (optional).
