# Observability design
- Micrometer + OpenTelemetry; propagate W3C tracecontext.
- JSON logs with MDC: service, traceId, spanId; no PII.
- Metrics tagged {service, endpoint, status}. Target CloudWatch + Application Signals in deploy profiles.
