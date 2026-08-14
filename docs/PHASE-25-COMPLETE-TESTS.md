# FASE 25 — Testes completos

## Cobertura entregue

### Unitários e services

- Normalização e correspondência de produtos.
- Deal Score e classificações.
- Alert Engine, cooldown e token inválido.
- Retenção de analytics.
- Validação explícita de ofertas coletadas.
- Execução do job para todos os collectors configurados.

### API

Um fluxo RestAssured em porta HTTP real cobre:

1. cadastro;
2. login;
3. consulta de produto;
4. listagem de deals;
5. criação e listagem de favorito;
6. criação de alerta.

Os testes MockMvc existentes complementam autenticação, refresh, logout, administração, analytics, histórico, lojas, tokens FCM e redirecionamento.

### Integração

- PostgreSQL 16 com Testcontainers.
- Migrations Flyway em PostgreSQL real.
- Redis 7 com escrita, leitura e remoção reais.
- Repositories JPA.
- Pipeline e jobs dos collectors.

O perfil é executado com `mvn -Dintegration test`. Nesta estação, os arquivos foram implementados, mas a execução local com containers ficou bloqueada porque o Docker Desktop informou que não consegue iniciar. A CI Linux executa a suíte de integração a cada push.

### Mobile

- Contratos de autenticação: login, cadastro, verificação, recuperação, reset e logout.
- Home, busca e detalhes por services/hooks/componentes.
- Favoritos e alertas.
- Registro e abertura de notificações.
- Analytics tolerante a indisponibilidade.
- Registro do clique antes do link externo.
- Persistência e renovação da sessão.

Testes em dispositivo físico e navegação ponta a ponta permanecem como validação de staging, pois dependem de Android/Firebase reais.

## Correção encontrada

A configuração Redis foi migrada para o prefixo atual `spring.data.redis`. Antes, o Spring podia ignorar o host configurado e usar `localhost` silenciosamente. `REDIS_HOST` e `REDIS_PORT` agora controlam efetivamente a conexão.

## Resultado local

- Backend regular: 62 testes aprovados.
- Mobile: 20 testes aprovados em 17 suítes.
- TypeScript e ESLint aprovados.
- Docker Compose validado estruturalmente.

## Próxima fase

A FASE 26 deve se concentrar exclusivamente em casos negativos e limites de segurança, sem duplicar os fluxos positivos desta fase.
