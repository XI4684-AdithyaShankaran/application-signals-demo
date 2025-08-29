# Security
- Bean Validation on request DTOs; sanitize/normalize inputs.
- Spring Security where applicable; never trust request-supplied identity.
- Secrets via env/SSM/Secrets Manager; never hard-code.
- CORS: explicit origins/methods; no wildcard in production.
- Redact sensitive fields in logs.
