# CI/CD gates
- Build → unit → integration(Testcontainers) → image → image scan → deploy.
- Gates: checkstyle, spotbugs, jacoco ≥80%, CVE scan.
- Block merges on failing gates.
