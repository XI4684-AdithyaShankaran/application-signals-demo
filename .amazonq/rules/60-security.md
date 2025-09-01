# Security
- Bean Validation on request DTOs; sanitize/normalize inputs.
- Spring Security where used; never trust request-supplied identity.
- Secrets via env/SSM/Secrets Manager only.
- CORS: explicit origins/methods (no *) in prod. Redact sensitive fields in logs.
