# Shell scripts (safety)
- set -euo pipefail; safe IFS.
- Quote all vars and command substitutions.
- No ls|grep; use globs; check exit codes.
- Provide die() for failures.
