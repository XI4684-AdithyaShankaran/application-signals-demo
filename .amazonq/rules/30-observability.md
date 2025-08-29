# Observability
- Micrometer + OpenTelemetry; propagate W3C tracecontext.
- JSON logs with MDC: service, traceId, spanId, user/context keys.
- Emit metrics with tags {service, endpoint, status}; timers around I/O.
- Target CloudWatch + Application Signals in deploy profiles.
