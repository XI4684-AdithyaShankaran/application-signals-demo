# CI/CD
- Pipeline: build → unit → integration(Testcontainers) → image → image scan → deploy.
- Gates: checkstyle, spotbugs, jacoco (≥80%), CVE scan. Block merges on failures.
