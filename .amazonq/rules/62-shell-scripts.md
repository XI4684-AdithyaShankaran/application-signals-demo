# Shell scripts
- Add `set -euo pipefail` and `IFS=$'\n\t'` at top.
- Quote all variable expansions and command substitutions.
- Prefer globs/find over `ls | grep`; check exit codes.
- Provide `die(){ echo "ERR: $*" >&2; exit 1; }` and use after critical cmds.
