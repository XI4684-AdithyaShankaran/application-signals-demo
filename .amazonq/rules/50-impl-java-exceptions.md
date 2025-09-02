# Exception handling
- Catch narrow exceptions; avoid blanket Exception.
- Log context then translate/rethrow; no swallowing.
- Public controllers MUST NOT declare checked throws; translate via @ControllerAdvice or ResponseStatusException.
- Validate early; Optional.orElseThrow for required data.
