# FASE 00 — Ambiente verificado

Resumo das verificações realizadas em 2026-08-13:

- `git --version`: git version 2.55.0.windows.3
- `java -version`: java version "21.0.11"
- `node --version`: v24.19.0
- `npm --version`: 11.17.0
- `docker --version`: Docker version 29.7.2
- `docker compose version`: Docker Compose version v5.3.1

Observações:
- `npm.ps1` foi bloqueado pelo PowerShell ExecutionPolicy; usei `npm.cmd` para obter a versão.
- Algumas instalações exigiram elevação. Reiniciar shells pode ser necessário para persistência no `PATH`.
