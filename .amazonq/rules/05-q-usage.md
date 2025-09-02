# Using Amazon Q (meta)
- Always name the exact **file path** and **rule IDs** to follow (e.g., 45-java-logging, 50-java-exceptions).
- Small scope: one file (or one method). No cross-file refactors.
- List **3–5 concrete edits**; everything else is out of scope.
- **Output = unified diff only** (apply with `git apply -p0`). No prose.
- Add any needed imports. Do not change packages, public APIs, or add deps.
- If diff > 300 LOC, split into smaller patches.
- Tests only under `src/test/java` when requested.