# FASE 29 — Integração contínua

O pipeline bloqueia o artefato de release até concluir compilação, lint/typecheck, testes unitários, integração com PostgreSQL/Redis, auditoria npm, validação k6, build Android e build da imagem backend.

Artefatos APK e JAR ficam disponíveis por 14 dias. Dependency Review, Dependabot e CodeQL complementam a análise. Falha em qualquer dependência de `release-candidate` impede a produção do candidato.

O workflow manual `Environment readiness` valida staging ou produção sob um GitHub Environment. Publicação externa permanece bloqueada até existir adaptador e segredos do provedor.
