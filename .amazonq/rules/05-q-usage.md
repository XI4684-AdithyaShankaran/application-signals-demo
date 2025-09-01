# Using Amazon Q (meta)
- Always name the **exact file path** and **rule IDs** to follow.
- **Small scope**: one file (or one method). No cross-file refactors.
- **List 3–5 concrete edits** to make. Everything else is out of scope.
- **Output = unified diff only** (apply with `git apply -p0`). No prose.
- Add any needed imports. Do not change packages, public APIs, or add deps.
- If diff >300 LOC, split into multiple smaller patches.
- Tests land only under `src/test/java` when requested.
