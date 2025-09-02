# Java style
- Java 17; Spring Boot 2.6.x.
- Constructor injection only; no field/setter injection.
- Public APIs have Javadoc; DTOs minimal.
- Validate inputs at edges; avoid NPEs (Optional/early checks).
- Centralize errors via @ControllerAdvice with meaningful HTTP codes.