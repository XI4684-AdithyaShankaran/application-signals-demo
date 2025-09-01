# Java logging
- private static final Logger log = LoggerFactory.getLogger(X.class);
- INFO=milestones, WARN=recoverable issues, ERROR=failures; no DEBUG-only errors.
- Parameterized logs with key/value (petId={}, status={}).
- Never log secrets/tokens/PII or full payloads.
- Replace System.out/printStackTrace with proper logs.
