# Amazon Q Prompt Cookbook (copy templates)
> Enable only the rules you reference (Rules pill). **Return unified diff only.**

## A) Java logging + exceptions (single file)
Apply rules 05-q-usage, 45-java-logging, 50-java-exceptions, 10-java-style to:
<PATH>/spring-petclinic-*/**/<FILE.java>
Edits:
1) Add logger field.
2) Replace System.out/printStackTrace with parameterized logs.
3) Narrow catches; remove catch(Exception); log context.
4) Translate errors to existing @ControllerAdvice model (no behavior change).
Output: unified diff only.

## B) RestTemplate/WebClient hardening
Apply rules 55-rest-clients, 20-microservices, 45-java-logging to:
<PATH>/<FILE.java>
Edits:
1) Add connect/read timeouts; per-request timeout for WebClient.
2) Handle non-2xx → domain exception; redact sensitive data.
3) Handle null/empty body (Optional.orElseThrow).
4) Log endpoint, method, status (no secrets).
Output: unified diff only.

## C) Controller validation + MDC
Apply rules 10-java-style, 30-observability, 60-security to:
<PATH>/<Controller.java>
Edits: constructor injection; Bean Validation on DTOs; entry INFO log with MDC {traceId, spanId}; no body/secrets in logs.
Output: unified diff only.

## D) Focused unit tests (no Spring)
Apply rule 40-tests for:
<PATH>/src/main/java/.../<ClassUnderTest>.java
Create under src/test/java. Cases: happy path + timeout/error; mock RestTemplate/WebClient; assert exception mapping; verify one WARN/ERROR log line.
Output: unified diff only (new test files).

## E) Shell: quoting + safety
Apply rules 62-shell-scripts to:
<PATH>/scripts/**/<FILE.sh>
Edits: `set -euo pipefail` + IFS; quote all vars and substitutions; add die(); replace ls|grep with globs; check exit codes.
Output: unified diff only.

## F) CDK/TS: encryption/protection
Apply rules 67-infra-cdk-ts to:
<PATH>/cdk/**/stacks/*.ts
Edits: enforce encryption, block public access, deletion protection; least privilege IAM; minimal nag suppressions with rationale.
Output: unified diff only.

## G) “Only remaining Problems in this file”
For <PATH>/<FILE>, fix only the current VS Code Problems using rules: <RULE-IDs>. No unrelated refactors. Output unified diff only.

## H) Repo-wide plan → batches
Scan workspace vs enabled rules + Problems. Group by rule→files. Propose 5–8 patch batches (<300 LOC each) with titles and file lists. Output the plan only (no diffs).
