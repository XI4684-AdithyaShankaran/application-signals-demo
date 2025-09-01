# Amazon Q rules Implementation on Java Microservices

**Repo (fork):** `github.com/XI4684-AdithyaShankaran/application-signals-demo`
**Scope:** Java 17 Spring Boot microservices (PetClinic variant) with **Amazon Q Project Rules** driving refactors, tests, and hygiene.
**Primary goal:** Use Amazon Q in VS Code to surface Problems and auto-generate targeted patches guided by **.amazonq/rules**.

---

## 1) Prerequisites

Install or verify:

* **AWS account & Builder ID** (Xebia email) and **Amazon Q** access.
* **AWS CLI v2 ≥ 2.13**, **kubectl**, **eksctl**, **jq**, **Docker**, **Java 17 (Temurin)**, **Maven 3.8+**, **Node 18+** (for CDK), **Go** (per repo), **Terraform** *(optional)*, **Python 3**.
* **VS Code** + **Amazon Q extension** (sign in with Builder ID).

```bash
# Quick checks
aws --version
java -version
mvn -v
node -v
docker --version
kubectl version --client
eksctl version
jq --version
```

---

## 2) Repo setup

```bash
git clone https://github.com/XI4684-AdithyaShankaran/application-signals-demo
cd application-signals-demo
./mvnw -q clean install   # local build
```

> Local tests are already passing; several Problems are reported by Q/linters (Java, shell, CDK TS, etc.).

---

## 3) Amazon Q Project Rules

### 3.1 What’s in `.amazonq/rules/`

* **05-q-usage.md** – micro-playbook for asking Q (diff-only, scope, iteration).
* **10-java-style.md** – Java 17, constructor injection, Javadoc, controller advice.
* **15-local-dev.md** – local/test profiles, Testcontainers/WireMock, no external deps.
* **20-microservices.md** – WebClient/RestTemplate policies, actuator, config hygiene.
* **25-governance.md** – PR checklist & gates.
* **30-observability.md** – Micrometer/OTel, MDC fields, metrics tags.
* **35-runbooks.md** – SLO, alarms, playbooks.
* **40-tests.md** – JUnit5/Mockito, coverage, naming, offline tests.
* **45-java-logging.md** – SLF4J, levels, structured params, no secrets.
* **50-java-exceptions.md** – narrow catches, translate/log, no swallow.
* **55-rest-clients.md** – timeouts, retries/CB, DTO boundaries, tests.
* **60-security.md** – Bean Validation, inputs, CORS, secrets, redaction.
* **62-shell-scripts.md** – `set -euo pipefail`, quoting, error handling.
* **65-deps-build.md** – pin versions, enforcer, CVE scan.
* **67-infra-cdk-ts.md** – encryption, block public, deletionProtection, IAM scope.
* **70-docker.md** – base image, non-root, healthcheck, secrets via env.
* **75-api-docs.md** – springdoc OpenAPI, errors, versioning.
* **80-ci-cd.md** – pipeline order & gates.
* **85-deploy.md** – EKS hints, OTEL/AppSignals env, Terraform vs one-step.
* **90-prompts.md** – prompt snippets (diff-only, file-scoped).
* **95-release.md** – tags, changelog, immutable images.

> These are concise, imperative, and intentionally scoped so Q can act reliably.

---

## 4) How to use Amazon Q effectively

### 4.1 Rule selection (Rules pill)

* **Always enable:** `05-q-usage`.
* Then enable **≤2 more** matching the file **type** and **Problems** text.

**Cheat-sheet:**

| Problem text (VS Code)                                         | Enable (add to 05)                               |
| -------------------------------------------------------------- | ------------------------------------------------ |
| “Poor logging practice”, “log injection”, “CWE-532”            | `45-java-logging` (+ `60-security` if sensitive) |
| “Poor error handling”, “catch(Exception)”, “generic exception” | `50-java-exceptions`                             |
| “timeouts/retry/circuit breaker” or REST client issues         | `55-rest-clients` + `20-microservices`           |
| “shell-unquoted-variables”, fragile loops                      | `62-shell-scripts`                               |
| “AWS missing encryption”, “block public access”, IAM \*        | `67-infra-cdk-ts`                                |
| “package vulnerability / pin”                                  | `65-deps-build`                                  |

### 4.2 Prompt style (diff-only, scoped)

* **Be specific:** exact file path + rule names + 3–5 edits.
* **Ask for patch:** “Return **unified diff only**.”
* **Limit scope:** one file (or one method).
* **Iterate:** “Now only fix remaining Problems in this file.”

**Examples (copy/paste):**

**A) Java logging + exceptions**

```
Apply rules 05-q-usage, 45-java-logging, 50-java-exceptions to:
spring-petclinic-customers-service/src/main/java/.../PetResource.java

Edits (only):
1) Add SLF4J logger.
2) Replace System.out/printStackTrace with parameterized logs (no secrets).
3) Remove catch(Exception); narrow catches; log context then translate/rethrow via controller advice.
4) Preserve behavior and API signature.

Return unified diff only.
```

**B) REST client hardening**

```
Apply 05-q-usage, 55-rest-clients, 20-microservices to:
spring-petclinic-vets-service/src/main/java/.../VetClient.java

Edits:
1) Add connect/read timeouts (RestTemplate) or per-request timeout (WebClient).
2) Map non-2xx to domain exception with safe message.
3) Null/empty body -> Optional.orElseThrow with context.

Return unified diff only.
```

**C) Shell script safety**

```
Apply 05-q-usage, 62-shell-scripts to:
scripts/eks/appsignals/deploy-sample-app.sh

Edits:
1) Add set -euo pipefail and IFS=$'\n\t'.
2) Quote all variable expansions and command substitutions.
3) Add die(){...} and check exit codes after aws/eksctl/kubectl.
4) Replace ls|grep with globs/find.

Return unified diff only.
```

**D) Fix only current Problems**

```
Fix only Problems shown by VS Code in:
<path>

Use rules: 05-q-usage, <one specific rule>. No unrelated refactors.
Return unified diff only.
```

---

## 5) Workflow

1. **Open file with Problems** (VS Code Problems panel).
2. **Enable rules** (05 + ≤2 specific).
3. **Run a focused prompt** (diff-only).
4. **Review patch** → `git apply` or use Q’s apply.
5. **Run tests:** `./mvnw -q -DskipITs=false test`.
6. **Commit:** meaningful message referencing rule(s)/CWE fixed.
7. Repeat per file; batch by module for PRs.

---

## 6) Local build & quick run

```bash
# Build all services
./mvnw clean install

# Optional: build images locally & push to ECR
export ACCOUNT=$(aws sts get-caller-identity --query Account -o text)
export AWS_REGION=<your-region>
./push-ecr.sh
```

---

## 7) Deployment

**EKS one-step demo (from repo README):**

```bash
export AWS_PROFILE=<your-profile>
export AWS_REGION=<region>
cd scripts/eks/appsignals
./setup-eks-demo.sh --region=$AWS_REGION
# ingress URL:
kubectl get svc -n ingress-nginx | grep ingress-nginx | awk '{print $4}'
```

* The demo sets up **Application Signals** → metrics/traces/logs in **CloudWatch**.
* For IaC environments, use **Terraform** under `terraform/eks` (*optional*).

> Deployment is secondary here; our focus is Amazon Q–driven code quality.

---

## 8) CI/CD & gates

* Gates: **checkstyle**, **spotbugs**, **jacoco ≥80% per module**, **CVE scan** (OWASP/Trivy).
* PR checklist per `25-governance.md`.
* Tests strategy per `40-tests.md`; contract tests for outbound HTTP.

---

### Appendix A — “When Q struggles”

* Shrink scope (one file / one method).
* Keep only 05 + one rule enabled.
* Ask for *unified diff only*.
* After applying, **re-run Problems** and prompt: “**Now only fix remaining Problems in this file**.”

---
