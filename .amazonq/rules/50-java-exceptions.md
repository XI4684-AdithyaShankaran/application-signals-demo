# Exception handling
- Catch the narrowest exception; avoid catch (Exception).
- Never swallow errors; log with context, then translate or rethrow as unchecked.
- Map to domain/HTTP via @ControllerAdvice/@ExceptionHandler (or ResponseStatusException).
- Validate inputs early; use Optional.orElseThrow for required data.
- Public controller methods must **not** declare checked exceptions or change signatures; translate errors so the method signature remains unchanged.