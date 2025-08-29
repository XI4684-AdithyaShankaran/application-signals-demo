# Deploy
- Kubernetes: readiness/liveness, requests/limits, rolling updates.
- OTEL/App Signals env: service name, OTEL_EXPORTER, sampler; propagate trace headers.
- Prefer Terraform for envs; one-step script OK for demos/POCs.
- Before deploy cmds: export AWS_PROFILE=<profile>; export AWS_REGION=<region>.
- EKS: use ALB/Ingress, IRSA for AWS access; ship logs/metrics to CloudWatch.
