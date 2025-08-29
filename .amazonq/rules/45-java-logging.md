# Logging rules
- private static final Logger log = LoggerFactory.getLogger(X.class);
- Levels: INFO=milestones, WARN=recoverable issues, ERROR=failures; avoid DEBUG-only errors.
- Structured logs: key/value context (petId={}, ownerId={}).
- Never log secrets/tokens/PII or full payloads.
- Replace System.out/printStackTrace with proper logs + cause.
