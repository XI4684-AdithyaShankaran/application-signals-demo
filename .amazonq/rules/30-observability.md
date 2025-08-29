## Observability
- Enable OpenTelemetry + Micrometer.
- Propagate W3C tracecontext (traceparent, tracestate).
- Include traceId and spanId in logs via MDC; Logback pattern must print them.
- Emit metrics with consistent tags: service, endpoint, status.
- Prefer CloudWatch (logs/metrics/traces) and Application Signals when deployed.
