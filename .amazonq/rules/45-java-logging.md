# Java logging
- Use: private static final Logger log = LoggerFactory.getLogger(X.class);
- Levels: INFO=milestones, WARN=recoverable issues, ERROR=failures; never DEBUG-only errors.
- Parameterized logs with key/value (e.g., petId={}, status={}).
- Never log secrets/tokens/PII or full payloads; sanitize/redact.
- Replace System.out/printStackTrace with proper logs; include cause (e).