# FASE 05 — Autenticação

## Objetivo

Implementar autenticação stateless segura, verificação de e-mail, recuperação de senha e proteção contra abuso.

## Endpoints

Todos usam o prefixo `/api/v1/auth`:

- `POST /register` — cadastra usuário e envia verificação de e-mail.
- `POST /verify-email` — confirma o token de verificação.
- `POST /login` — retorna JWT de acesso e refresh token.
- `POST /refresh` — rotaciona o refresh token e emite novo par.
- `POST /logout` — revoga o refresh token informado.
- `POST /forgot-password` — envia instruções sem revelar se a conta existe.
- `POST /reset-password` — redefine a senha com token de uso único.
- `GET /me` — endpoint protegido usado para validar o JWT.

## Segurança

- Senhas codificadas com bcrypt, custo 12.
- JWT de acesso com expiração padrão de 15 minutos.
- Refresh tokens aleatórios com validade de 30 dias, armazenados apenas como SHA-256 e rotacionados a cada uso.
- Tokens de verificação e recuperação aleatórios, com validade de 1 hora e uso único.
- Usuários precisam verificar o e-mail antes do login.
- Rate limiting por IP e endpoint nos `POST` de autenticação; padrão de 10 requisições por minuto.
- Resposta de recuperação de senha não permite enumeração de contas.
- Segredo JWT e credenciais SMTP são fornecidos por variáveis de ambiente em produção.

## Configuração de produção

Variáveis obrigatórias: `JWT_SECRET` (mínimo de 32 bytes), `MAIL_HOST`, `MAIL_USER`, `MAIL_PASSWORD` e `MAIL_FROM`.
Variáveis opcionais: `MAIL_PORT` e `APP_PUBLIC_URL`.

## Banco de dados

A migration `V1_11__Add_authentication.sql` adiciona `users.email_verified` e a tabela `auth_tokens`.

## Testes

`AuthControllerTest` cobre cadastro, validação, bcrypt, verificação de e-mail, login, JWT em endpoint protegido, refresh com rotação, logout, recuperação e redefinição de senha, token de uso único, e-mail desconhecido e duplicidade de cadastro.

Executado:

```text
mvn.cmd "-Dmaven.repo.local=..." test
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Checkpoint

FASE 05 concluída. Não foi iniciado nenhum requisito da FASE 06.
