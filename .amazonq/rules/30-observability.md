## Observability
- Micrometer + OpenTelemetry (enabled in deploy profiles, optional in unit tests).
- Propagate W3C tracecontext.
- JSON logging with MDC: service, traceId, spanId.
- Emit metrics with tags {service, endpoint, status}.
- Target CloudWatch + Application Signals in deploys.