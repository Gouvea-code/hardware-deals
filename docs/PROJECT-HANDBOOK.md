# Manual completo do projeto Hardware Deals

> Documento principal para pessoas e agentes de IA. Leia este arquivo, o `README.md` e a documentação da fase atual antes de alterar o projeto.

## 1. O que é o Hardware Deals

Hardware Deals é um aplicativo mobile que reúne ofertas de hardware, compara preços históricos, calcula a qualidade da promoção e avisa o usuário quando um produto atinge o preço desejado.

Fluxo principal:

1. Um collector autorizado recebe ofertas dos marketplaces.
2. O backend valida e normaliza os produtos.
3. Preço e histórico são gravados no PostgreSQL.
4. O Deal Engine calcula uma pontuação da oferta.
5. O aplicativo mostra ofertas, busca, detalhes, favoritos e alertas.
6. O Alert Engine compara alertas ativos com o preço atual e envia push pelo Firebase.
7. Ao escolher “Ver oferta”, o clique é registrado e o site confiável da loja é aberto.

## 2. Estado atual

- Fases 00 a 24 implementadas.
- Backend em Java 21, Spring Boot 3.5 e Maven.
- Aplicativo Android em React Native 0.87, React 19 e TypeScript.
- PostgreSQL para persistência, Redis preparado para cache/rate limit e Firebase Cloud Messaging para push.
- Autenticação JWT com access token curto, refresh token rotativo, bcrypt, verificação de e-mail e recuperação de senha.
- CI executa testes backend, integração com Docker, verificações mobile e auditorias.
- Marketplaces cadastrados: Mercado Livre, Amazon Brasil, KaBuM!, Magazine Luiza e Shopee.

Credenciais reais, ambientes publicados e conectores oficiais dos marketplaces ainda dependem de contas externas. Consulte `PROJECT-REMAINING-WORK.md`.

## 3. Organização do repositório

```text
hardware-deals/
├── backend/                         API Spring Boot
│   ├── src/main/java/.../
│   │   ├── collector/               entrada e normalização de ofertas
│   │   ├── config/                  segurança, Firebase e erros
│   │   ├── controller/              endpoints HTTP
│   │   ├── deal/                    cálculo do Deal Score
│   │   ├── dto/                     contratos da API
│   │   ├── entity/                  tabelas representadas em Java
│   │   ├── notification/            envio de push
│   │   ├── repository/              acesso ao banco
│   │   ├── security/                JWT e rate limit
│   │   └── service/                 regras de negócio
│   ├── src/main/resources/          configurações e migrations Flyway
│   └── src/test/                    testes unitários, web e integração
├── mobile/                          aplicativo React Native
│   ├── src/components/              componentes reutilizáveis
│   ├── src/hooks/                   consultas e estado das telas
│   ├── src/navigation/              rotas
│   ├── src/screens/                 telas
│   ├── src/services/                chamadas HTTP, sessão e push
│   ├── src/store/                   estado global de autenticação
│   └── __tests__/                   testes Jest
├── docs/                            decisões, fases e manuais
├── .github/workflows/ci.yml         integração contínua
├── docker-compose.yml               PostgreSQL e Redis locais
└── docker-compose.production.yml    base de implantação
```

## 4. Conceitos essenciais para iniciantes

- **Endpoint:** endereço da API, como `GET /api/v1/deals`.
- **Entity:** classe Java ligada a uma tabela do banco.
- **Repository:** interface usada para consultar ou gravar entities.
- **Service:** regra de negócio. Controllers devem ser pequenos e delegar para services.
- **DTO:** formato enviado ou recebido pela API. Não expor entities diretamente.
- **Migration:** SQL versionado aplicado pelo Flyway. Nunca editar uma migration que já tenha sido aplicada em ambiente compartilhado; criar a próxima versão.
- **JWT:** credencial curta enviada no header `Authorization: Bearer ...`.
- **Refresh token:** credencial mais longa usada apenas para renovar a sessão.
- **React Query:** gerencia carregamento, cache e atualização dos dados no aplicativo.
- **FCM:** serviço Firebase que entrega notificações push.

## 5. Modelo de dados

| Tabela | Responsabilidade |
| --- | --- |
| `users` | conta e identidade do usuário |
| `auth_tokens` | refresh, verificação de e-mail e redefinição, todos armazenados como hash |
| `stores` | marketplaces/lojas habilitados |
| `products` | produto normalizado compartilhado entre lojas |
| `store_products` | anúncio/SKU e URL em uma loja específica |
| `offers` | fotografia de preço coletada |
| `price_history` | série histórica por produto e loja |
| `favorites` | produtos favoritos por usuário |
| `price_alerts` | preço-alvo e controle de notificação |
| `device_tokens` | dispositivos FCM ativos |
| `notifications` | histórico de notificações geradas |
| `offer_clicks` | clique, oferta, produto, loja, usuário opcional e horário |
| `analytics_events` | eventos mínimos, contexto tipado e retenção limitada |
| `admin_audit` | trilha mínima de alterações administrativas |

## 6. Backend: fluxo e regras

Todas as rotas usam o prefixo `/api/v1`.

### Rotas públicas principais

- `POST /auth/register`, `/login`, `/refresh`, `/logout`
- `POST /auth/forgot-password`, `/reset-password`, `/verify-email`
- `GET /products`, `/products/{id}`
- `GET /stores`, `/stores/{id}`
- `GET /deals`, `/deals/{id}` e `/products/{id}/offers`
- `POST /offers/{offerId}/click`
- `POST /analytics/events`
- `GET /health` e health checks do Actuator

### Rotas autenticadas principais

- `GET` e `DELETE /auth/me`
- favoritos
- alertas de preço
- tokens de dispositivo
- administração em `/admin/**`, exclusivamente para `ROLE_ADMIN`

### Coleta

`JsonFeedPriceCollector` lê um feed autorizado definido por `COLLECTOR_FEED_URL`. Cada item vira `CollectedOffer`. O pipeline valida campos e URLs, normaliza identidade, encontra ou cria o produto, relaciona a loja, grava oferta e histórico. Falhas isoladas não interrompem todo o lote.

### Alertas

`AlertEngineService` é executado pelo agendador quando habilitado. Ele considera alertas ativos, menor oferta atual disponível e cooldown. Cria a notificação antes do envio, envia para os dispositivos ativos e desativa tokens que o Firebase identifica como inválidos.

### Redirecionamento

`OfferRedirectService` aceita somente oferta ativa e URL HTTPS no domínio cadastrado da loja ou subdomínio. Grava `OfferClick` e devolve a URL ao app. Nunca aceite uma URL recebida diretamente do cliente.

## 7. Mobile: fluxo e arquitetura

`RootNavigator` reúne as telas. `SessionBootstrap` restaura a sessão segura ao iniciar. O Axios em `apiClient.ts` inclui o access token e, diante de 401, coordena um único refresh para chamadas simultâneas. Se a renovação falhar, limpa a sessão.

Telas existentes:

- Home e busca.
- Detalhes, ofertas e histórico visual.
- Login, cadastro, verificação e recuperação de senha.
- Favoritos e alertas.
- Conta, logout e exclusão permanente.

Tokens de sessão usam o armazenamento seguro nativo por `react-native-keychain`. Nunca mover tokens para AsyncStorage.

## 8. Preparação do ambiente

Instale:

- Git.
- Java JDK 21.
- Maven 3.9 ou use uma instalação compatível.
- Node.js 22 e npm.
- Android Studio, Android SDK e um emulador/aparelho.
- Docker Desktop para PostgreSQL, Redis e Testcontainers.

No Windows, se `npm` for bloqueado pela política do PowerShell, use `npm.cmd`.

### Backend local

```powershell
docker compose up -d
cd backend
mvn.cmd spring-boot:run
```

A API inicia normalmente em `http://localhost:8080`. Swagger fica em `/swagger-ui/index.html`.

### Mobile local

```powershell
cd mobile
npm.cmd install
npm.cmd start
npm.cmd run android
```

Use um dos exemplos `.env.*.example` da pasta mobile. Em emulador Android, o host da máquina costuma ser `10.0.2.2`, não `localhost`.

## 9. Variáveis e segredos

Variáveis relevantes do backend:

- `DB_URL`, `DB_USER`, `DB_PASSWORD`
- `REDIS_HOST`, `REDIS_PORT`
- `JWT_SECRET` com no mínimo 32 bytes aleatórios
- `MAIL_HOST`, `MAIL_PORT`, `MAIL_USER`, `MAIL_PASSWORD`, `MAIL_FROM`
- `GOOGLE_APPLICATION_CREDENTIALS`
- `FIREBASE_ENABLED`, `ALERT_ENGINE_ENABLED`
- `COLLECTOR_ENABLED`, `COLLECTOR_FEED_URL`, `COLLECTOR_CRON`
- `ALLOWED_ORIGINS`

Nunca commitar `.env`, `google-services.json`, conta de serviço Firebase, keystore Android ou senhas. Use arquivos de exemplo somente como modelo.

## 10. Testes e qualidade

Antes de qualquer commit:

```powershell
cd backend
mvn.cmd test
mvn.cmd -Dintegration test

cd ../mobile
npm.cmd run typecheck
npm.cmd run lint
npm.cmd test -- --runInBand
npm.cmd audit --audit-level=critical
```

Os testes de integração precisam do Docker ativo. Não declarar que foram validados localmente quando o daemon estiver desligado. A CI também compila o Android e executa revisão de dependências.

## 11. Como implementar uma nova fase

1. Ler este manual e a seção correspondente em `docs/IMPLEMENTATION.md`.
2. Conferir `git status` e preservar alterações que não pertencem à tarefa.
3. Identificar contratos, migrations, segurança, comportamento mobile e testes afetados.
4. Criar migrations novas e compatíveis; nunca reescrever histórico aplicado.
5. Manter controllers finos, regras em services e contratos em DTOs.
6. Escrever testes positivos, negativos e de autorização.
7. Rodar backend, mobile e integração proporcionalmente ao risco.
8. Criar `docs/PHASE-XX-NAME.md` com escopo, decisões, testes e limitações.
9. Atualizar `README.md` e `PROJECT-REMAINING-WORK.md`.
10. Revisar diff, não incluir segredos, então fazer commit e push quando autorizado.

## 12. Regras para qualquer IA que continue o projeto

- O repositório e o código são a fonte de verdade; documentos antigos podem descrever um estado anterior.
- Leia todos os `.md` relevantes antes de agir, especialmente este manual e a fase solicitada.
- Não invente que Firebase, Docker, staging, produção ou publicação foram validados sem evidência.
- Não implemente scraping ou contorno de controles dos marketplaces. Use APIs/feeds autorizados.
- Não exponha dados pessoais, tokens, chaves ou credenciais em código, log, teste ou resposta.
- Preserve compatibilidade de API e dados. Alterações incompatíveis exigem decisão documentada.
- Para dados do usuário, aplique minimização, autorização por proprietário e exclusão LGPD.
- Para links externos, o servidor decide e valida o destino; o cliente não envia a URL.
- Finalize cada fase com testes, documentação, lista explícita de pendências e hash do commit.
- Se uma tarefa depender de conta, contrato ou credencial externa, prepare o código e registre o bloqueio; não simule conclusão.

## 13. Diagnóstico rápido

- **API não conecta ao banco:** confirme Docker, porta 5432 e variáveis do datasource.
- **Testcontainers falha:** inicie Docker Desktop e confirme que `docker info` responde.
- **Mobile não alcança a API:** confira `API_BASE_URL` e o endereço do host no emulador/aparelho.
- **Login falha após cadastro:** verifique o e-mail usando o token enviado pelo SMTP configurado.
- **Push não chega:** confirme `google-services.json`, permissão Android, token registrado, Firebase Admin e engine habilitados.
- **Collector não inicia:** quando habilitado, `COLLECTOR_FEED_URL` é obrigatório.
- **Migration falha:** não alterar migration já aplicada; corrija com nova versão.

## 14. Documentos complementares

- `IMPLEMENTATION.md`: roteiro original das fases.
- `PROJECT-REMAINING-WORK.md`: trabalho restante e dependências externas.
- `DEPLOYMENT-AND-RELEASE.md`: implantação e publicação.
- `LGPD-AND-PRIVACY.md`: base técnica de privacidade.
- `MARKETPLACE-INTEGRATION-PLAN.md`: estratégia dos cinco marketplaces.
- `PHASE-21-ALERT-ENGINE.md` e `PHASE-22-OFFER-REDIRECT.md`: últimas entregas funcionais.
- `PHASE-23-PRIVACY-ANALYTICS.md`: eventos mínimos e regras de retenção.
- `PHASE-24-ADMINISTRATION.md`: papéis, operações e auditoria administrativa.
