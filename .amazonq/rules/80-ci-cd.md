# CI/CD
- Pipeline: build → unit → integration(Testcontainers) → image → image scan → deploy.
- Gates: checkstyle, spotbugs, jacoco (≥80%), CVE scan.
- Cache Maven layers; parallelize modules where safe.
- Block merges on failing gates.
