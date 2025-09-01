# Exception handling
- Catch the narrowest exception; avoid catch (Exception).
- Never swallow errors; log context, rethrow/translate.
- Map to domain/HTTP via @ControllerAdvice/@ExceptionHandler.
- Validate inputs early; Optional.orElseThrow for required data.
