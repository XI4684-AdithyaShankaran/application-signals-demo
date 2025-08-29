# Exception handling
- Catch the narrowest exception; avoid blanket Exception.
- Never swallow errors: log with context then rethrow/translate.
- Map to domain/HTTP errors via @ControllerAdvice/@ExceptionHandler.
- Validate inputs early; prefer Optional.orElseThrow for required data.
