# Security-by-design
- Validate/normalize inputs; Bean Validation on request DTOs.
- Explicit CORS (no wildcards in prod).
- Never trust request-supplied identity; use Spring Security.
- Secrets from env/SSM/Secrets Manager only.
