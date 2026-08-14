# Hardware Deals

Aplicativo Android para encontrar ofertas de hardware, comparar preços históricos, salvar favoritos e receber alertas quando um produto atingir o preço desejado.

Fase atual concluída: **FASE 27 — Segurança**.

## Tecnologias

- Backend: Java 21, Spring Boot 3.5, Maven, PostgreSQL, Redis e Flyway.
- Mobile: React Native 0.87, React 19 e TypeScript.
- Push: Firebase Cloud Messaging.
- Infraestrutura local: Docker Compose.

## Pré-requisitos

Instale antes de começar:

- Git.
- Java JDK 21 (`java -version`).
- Maven 3.9 (`mvn -version`). No Windows, o comando pode ser `mvn.cmd`.
- Node.js 22 e npm (`node --version` e `npm --version`).
- Docker Desktop ou Docker Engine com Compose (`docker compose version`).
- Android Studio com Android SDK para executar o aplicativo.

## 1. Baixar o projeto

```bash
git clone https://github.com/Gouvea-code/hardware-deals.git
cd hardware-deals
```

## 2. Iniciar PostgreSQL, Redis e servidor de e-mail local

Certifique-se de que o Docker está ativo e execute na raiz:

```bash
docker compose up -d
docker compose ps
```

Serviços locais:

| Serviço | Endereço |
| --- | --- |
| PostgreSQL | `localhost:5432`, banco `hardware_deals`, usuário/senha `postgres` |
| Redis | `localhost:6379` |
| Mailpit | `http://localhost:8025` |

O Mailpit captura e-mails de desenvolvimento; nenhuma mensagem é enviada para endereços reais.

Para encerrar os serviços sem apagar os dados:

```bash
docker compose stop
```

## 3. Executar o backend

Abra outro terminal:

```bash
cd backend
mvn spring-boot:run
```

No Windows PowerShell, se necessário:

```powershell
cd backend
mvn.cmd spring-boot:run
```

O Flyway cria e atualiza as tabelas automaticamente. Após iniciar:

- Saúde: `http://localhost:8080/api/v1/health`
- Swagger: `http://localhost:8080/swagger-ui/index.html`
- Documentação OpenAPI: `http://localhost:8080/v3/api-docs`

## 4. Executar o aplicativo Android

Abra outro terminal:

```bash
cd mobile
npm install
```

Crie a configuração de desenvolvimento:

Linux/macOS:

```bash
cp .env.development.example .env.development
```

Windows PowerShell:

```powershell
Copy-Item .env.development.example .env.development
```

O exemplo usa `http://10.0.2.2:8080/api/v1`, endereço correto para o emulador Android acessar a máquina. Em aparelho físico, substitua `10.0.2.2` pelo IP local do computador e permita a porta 8080 no firewall.

Inicie o Metro:

```bash
npm start
```

Em outro terminal, com um emulador ou aparelho conectado:

```bash
cd mobile
npm run android
```

No Windows, use `npm.cmd` se o PowerShell bloquear `npm.ps1`.

O aplicativo compila sem Firebase local, mas push exige o `google-services.json` correto em `mobile/android/app/`. Esse arquivo é secreto e não deve entrar no Git.

## 5. Criar e verificar uma conta local

1. Cadastre-se pelo aplicativo.
2. Abra `http://localhost:8025`.
3. Abra o e-mail de verificação e copie o valor depois de `token=`.
4. Na tela de verificação do aplicativo, informe esse token.
5. Faça login.

Recuperação de senha usa o mesmo Mailpit.

## 6. Criar o primeiro administrador

Primeiro registre e verifique normalmente a conta. Depois reinicie o backend definindo o e-mail:

Linux/macOS:

```bash
APP_ADMIN_EMAIL=admin@example.com mvn spring-boot:run
```

Windows PowerShell:

```powershell
$env:APP_ADMIN_EMAIL='admin@example.com'
mvn.cmd spring-boot:run
```

O bootstrap somente promove uma conta existente; não cria usuário nem altera senha. Depois de promovida, faça login novamente. As rotas administrativas estão em `/api/v1/admin/**` e exigem `ROLE_ADMIN`.

## 7. Executar os testes

Backend unitário e web:

```bash
cd backend
mvn test
```

Backend com Testcontainers — exige Docker ativo:

```bash
mvn -Dintegration test
```

Mobile:

```bash
cd mobile
npm run typecheck
npm run lint
npm test -- --runInBand
```

## 8. Configurações importantes

O backend aceita variáveis como:

- `DB_URL`, `DB_USER`, `DB_PASSWORD`
- `JWT_SECRET`
- `MAIL_HOST`, `MAIL_PORT`, `MAIL_USER`, `MAIL_PASSWORD`, `MAIL_FROM`
- `COLLECTOR_ENABLED`, `COLLECTOR_FEED_URL`, `COLLECTOR_CRON`
- `GOOGLE_APPLICATION_CREDENTIALS`, `FIREBASE_ENABLED`
- `ALERT_ENGINE_ENABLED`, `ALERT_ENGINE_CRON`
- `ANALYTICS_RETENTION`
- `ALLOWED_ORIGINS`
- `APP_ADMIN_EMAIL`

Produção não deve utilizar os valores locais. Consulte `.env.production.example` e `docker-compose.production.yml`.

## 9. Problemas comuns

- **Backend não conecta ao PostgreSQL:** confirme `docker compose ps` e se a porta 5432 está livre.
- **Testcontainers não inicia:** confirme que `docker info` responde.
- **Mobile não conecta:** verifique `API_BASE_URL`; `localhost` dentro do emulador não aponta para o computador.
- **Cadastro retorna erro:** confirme que o Mailpit está ativo na porta 1025.
- **Push não funciona:** configure Firebase, permita notificações e teste em aparelho físico.
- **PowerShell bloqueia npm:** use `npm.cmd`.
- **Porta ocupada:** encerre o serviço conflitante ou altere a porta correspondente.

## Documentação

- [Manual completo](docs/PROJECT-HANDBOOK.md)
- [FASE 24 — Administração](docs/PHASE-24-ADMINISTRATION.md)
- [FASE 25 — Testes completos](docs/PHASE-25-COMPLETE-TESTS.md)
- [FASE 26 — Testes negativos](docs/PHASE-26-NEGATIVE-TESTS.md)
- [FASE 27 — Segurança](docs/PHASE-27-SECURITY.md)
- [Cronograma de execução e publicação](docs/EXECUTION-ROADMAP.md)
- [Trabalho restante](docs/PROJECT-REMAINING-WORK.md)
- [Implantação e publicação](docs/DEPLOYMENT-AND-RELEASE.md)
- [LGPD e privacidade](docs/LGPD-AND-PRIVACY.md)
- [Plano dos marketplaces](docs/MARKETPLACE-INTEGRATION-PLAN.md)

## Segurança

Nunca envie ao Git arquivos `.env`, `google-services.json`, contas de serviço Firebase, keystores, senhas ou tokens. Consulte a política de segurança antes de usar dados reais.
