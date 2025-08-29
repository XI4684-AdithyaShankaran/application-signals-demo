# Java style
- Java 17; Spring Boot 2.6.x.
- Constructor injection only (no field/setter).
- Public APIs have Javadoc; DTOs concise.
- Use SLF4J logger (see 45-java-logging); never System.out/printStackTrace.
- Validate inputs at edges; avoid NPEs (early null checks/Optionals).
- Centralize errors via @ControllerAdvice; map to meaningful HTTP codes.
