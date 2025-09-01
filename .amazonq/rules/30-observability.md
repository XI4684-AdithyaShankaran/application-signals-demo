# Observability
- Micrometer + OpenTelemetry; W3C tracecontext propagation.
- JSON logs with MDC: service, traceId, spanId (no secrets).
- Emit metrics with tags {service, endpoint, status}; timers around I/O.
- Target CloudWatch + Application Signals in deploy profiles.
