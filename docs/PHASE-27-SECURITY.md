# FASE 27 — Segurança

## Resultado

A fase consolida controles preventivos no backend, automação de análise e os requisitos que precisam ser aplicados na infraestrutura. Segurança não termina nesta fase: cada release deve continuar passando pela CI, revisão de dependências e validação do ambiente publicado.

## Checklist

| Controle | Situação | Implementação ou evidência |
| --- | --- | --- |
| HTTPS | Preparado | API escuta somente em `127.0.0.1` na composição de produção, aceita headers encaminhados e deve ficar atrás de proxy HTTPS. HSTS é enviado em conexões seguras. Certificado depende do domínio/provedor. |
| Hash de senha | Concluído | BCrypt com custo 12; senhas não são armazenadas nem registradas em logs. |
| JWT seguro | Concluído | HS256 com segredo mínimo de 32 bytes, validade curta, identificador único e validação de assinatura/expiração. Refresh tokens são aleatórios, rotativos e persistidos somente como SHA-256. |
| Rate limiting | Concluído | Contador Redis compartilhado por minuto e rota de autenticação, com fallback local quando Redis estiver indisponível. Respostas 429 incluem `Retry-After`. |
| CORS | Concluído | Origens são uma allowlist por ambiente; métodos e headers são restritos. Não existe wildcard com credenciais. |
| Input validation | Concluído | Bean Validation, limites de tamanho, valores mínimos, UUID tipado e respostas genéricas para entradas inválidas. |
| SQL Injection | Concluído | Repositórios JPA e parâmetros vinculados; não há SQL construído com entrada do usuário. |
| XSS | Concluído | API JSON não renderiza HTML; CSP bloqueia fontes, frames, formulários e conteúdo por padrão. |
| Authorization | Concluído | Rotas administrativas exigem `ROLE_ADMIN`; recursos pessoais derivam o usuário do JWT e testes cobrem acesso anônimo e cruzado. |
| Secrets | Preparado | `.env` é ignorado, Firebase usa Docker secret e produção exige variáveis externas. Valores reais nunca devem entrar no Git. |
| Logs sem dados sensíveis | Concluído | Logs operacionais usam IDs/códigos e não imprimem senha, JWT, refresh token, token Firebase ou credenciais. Erros de produção não expõem mensagem, stack trace ou binding. |
| Dependências | Automatizado | Dependabot semanal para Maven, npm e GitHub Actions; Dependency Review em PR; npm audit para vulnerabilidades críticas; CodeQL para Java e TypeScript. |

## Headers adicionados

- `Content-Security-Policy: default-src 'none'...`
- `X-Frame-Options: DENY`
- `Referrer-Policy: no-referrer`
- `Permissions-Policy` sem câmera, microfone, localização ou pagamentos.
- `Strict-Transport-Security` por um ano, subdomínios e preload em HTTPS.

## Rate limiting distribuído

O filtro usa chaves Redis com janela temporal e uma identidade não reversível composta pelo endereço de rede e rota. O TTL remove automaticamente contadores antigos. Em desenvolvimento ou testes, o controle pode ser configurado por:

```text
DISTRIBUTED_RATE_LIMIT_ENABLED=false
```

Produção deve manter o valor `true`. O fallback local preserva proteção básica durante uma indisponibilidade do Redis, mas não compartilha contagem entre réplicas.

## Dependências e risco conhecido

Em 14/08/2026, `npm audit --audit-level=critical` passou sem vulnerabilidades críticas. O relatório ainda apresenta 8 ocorrências de severidade alta herdadas do toolchain Metro/React Native através de `image-size`. A correção automática sugerida faria downgrade incompatível do React Native, portanto não foi aplicada. Dependabot deve acompanhar uma correção compatível; esse risco afeta ferramentas de processamento de imagens do build, não uma rota de upload da API.

## Validação executada

```bash
cd backend
mvn --batch-mode test

cd ../mobile
npm.cmd audit --audit-level=critical
```

Resultado: **68 testes backend aprovados** e **zero vulnerabilidades npm críticas**.

## Ações externas obrigatórias antes da produção

1. Configurar domínio, proxy reverso e certificado TLS válido.
2. Gerar segredos aleatórios no cofre do provedor e executar rotação periódica.
3. Restringir rede de PostgreSQL e Redis somente à aplicação.
4. Habilitar proteção de branch exigindo CI, CodeQL e Dependency Review.
5. Revisar alertas do GitHub Security e Dependabot antes de cada release.
6. Executar teste de invasão no staging e registrar aceite dos riscos residuais.

## Próxima fase

A FASE 28 medirá latência da API, banco, collectors e push, além de testar os patamares iniciais de 100, 1.000 e 10.000 usuários.
