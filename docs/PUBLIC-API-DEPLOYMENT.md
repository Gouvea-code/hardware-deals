# API pública gratuita para o beta

## Arquitetura escolhida

| Componente | Serviço | Custo inicial | Limite relevante |
|---|---|---|---|
| API Spring Boot | Koyeb Free Web Service | US$ 0 | 512 MB RAM, 0,1 vCPU, uma instância; dorme após 1 hora sem tráfego |
| PostgreSQL | Neon Free | US$ 0 | 0,5 GB e 100 CU-horas mensais por projeto |
| Redis | Upstash Free | US$ 0 | 256 MB e 500 mil comandos mensais |
| Site | GitHub Pages | US$ 0 | já publicado |

Essa composição serve para desenvolvimento e beta com pouco tráfego. Não possui SLA de produção e pode apresentar alguns segundos de espera no primeiro acesso após o período ocioso.

## 1. Criar o PostgreSQL

1. Crie uma conta gratuita em `https://console.neon.tech`.
2. Crie um projeto na região mais próxima possível da API.
3. Copie a connection string com SSL.
4. Converta o início de `postgresql://` para `jdbc:postgresql://` e salve o resultado como segredo `DB_URL` no Koyeb.
5. Separe usuário e senha nos segredos `DB_USER` e `DB_PASSWORD`.

Formato esperado, sem credenciais reais:

`jdbc:postgresql://host.neon.tech/hardware_deals?sslmode=require`

## 2. Criar o Redis

1. Crie um banco gratuito em `https://console.upstash.com`.
2. Escolha a mesma região ou uma região próxima da API.
3. Copie a URL TLS no formato `rediss://default:senha@host:porta`.
4. Salve-a como segredo `REDIS_URL` no Koyeb.

## 3. Publicar a API no Koyeb

1. Em `https://app.koyeb.com`, escolha **Create Web Service > GitHub**.
2. Autorize somente o repositório `Gouvea-code/hardware-deals`.
3. Selecione a branch `main`, builder **Dockerfile** e work directory `backend`.
4. Use `Dockerfile` como localização do arquivo e selecione a instância **Free**.
5. Defina porta HTTP `8080`, rota `/` e health check `/actuator/health/readiness`.
6. Configure as variáveis abaixo e faça o deploy.

## 4. Variáveis do Koyeb

Valores comuns:

```text
SPRING_PROFILES_ACTIVE=prod
PORT=8080
ALLOWED_ORIGINS=https://gouvea-code.github.io
COLLECTOR_ENABLED=true
MERCADO_LIVRE_ENABLED=true
MERCADO_LIVRE_QUERY=hardware computador
FIREBASE_ENABLED=false
ALERT_ENGINE_ENABLED=false
DISTRIBUTED_RATE_LIMIT_ENABLED=true
```

Segredos:

```text
DB_URL=<jdbc do Neon com sslmode=require>
DB_USER=<usuário Neon>
DB_PASSWORD=<senha Neon>
REDIS_URL=<URL rediss do Upstash>
JWT_SECRET=<valor aleatório de ao menos 32 bytes>
APP_PUBLIC_URL=<URL HTTPS gerada pelo Koyeb>
MERCADO_LIVRE_ACCESS_TOKEN=<token OAuth; deixar vazio até autorizar>
MAIL_HOST=<servidor SMTP>
MAIL_PORT=587
MAIL_USER=<usuário SMTP>
MAIL_PASSWORD=<senha SMTP>
MAIL_FROM=<remetente>
```

Não use novamente a chave do Mercado Livre que foi exposta em conversa. Gere outra antes do OAuth.

## 5. Validar e conectar os clientes

1. Confirme `https://<app>.koyeb.app/actuator/health/readiness`.
2. Confirme `https://<app>.koyeb.app/api/v1/deals`.
3. Preencha `APP_PUBLIC_URL` com a própria URL do Koyeb e faça redeploy.
4. Atualize `public-site/config.js` com essa URL.
5. Gere novo APK com `API_BASE_URL=https://<app>.koyeb.app/api/v1`.

## Limites e migração futura

- O primeiro acesso após inatividade terá cold start.
- Monitore armazenamento do Neon e comandos do Upstash.
- Não ative Firebase/Alert Engine antes de configurar as credenciais e medir consumo.
- Quando houver usuários recorrentes, migre primeiro a API para uma instância sem suspensão; banco e Redis podem ser ampliados separadamente.
