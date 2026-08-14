# FASE 24 — Administração

## Objetivo

Fornecer uma API operacional protegida para acompanhar o sistema e executar alterações controladas, sem criar ainda uma interface administrativa web.

## Segurança

- Usuários recebem `role=USER` por padrão.
- `/api/v1/admin/**` exige `ROLE_ADMIN`.
- O filtro JWT consulta o papel atual no banco a cada requisição; remoções de acesso passam a valer imediatamente.
- Nenhum endpoint administrativo retorna hash de senha, tokens, credenciais do collector ou segredos.
- Um administrador não pode desativar a própria conta nem remover o próprio papel.
- Alterações administrativas geram registros em `admin_audit`.

## Primeiro administrador

Registre e verifique uma conta normal. Defina temporariamente `APP_ADMIN_EMAIL` e reinicie o backend. O bootstrap promove apenas a conta existente. Remova a variável depois da promoção e faça login novamente para receber uma nova sessão.

## Endpoints

| Método e rota | Função |
| --- | --- |
| `GET /admin/dashboard` | totais operacionais |
| `GET /admin/users` | usuários sem dados secretos |
| `PATCH /admin/users/{id}` | status e papel |
| `GET /admin/products` | catálogo com estado ativo |
| `PATCH /admin/products/{id}/active` | ativar/desativar produto |
| `GET /admin/stores` | lojas e estado ativo |
| `PATCH /admin/stores/{id}/active` | ativar/desativar loja |
| `GET /admin/offers` | 50 coletas mais recentes |
| `GET /admin/collectors` | habilitação, cron e existência de feed, sem revelar URL |
| `GET /admin/reports` | totais de cliques, eventos, notificações e alertas ativos |
| `GET /admin/audit` | 100 ações administrativas mais recentes |

Exemplo de alteração de usuário:

```json
{
  "status": "ACTIVE",
  "role": "ADMIN"
}
```

Exemplo de ativação:

```json
{
  "active": false
}
```

## Banco

A migration `V1_15__Add_administration.sql` adiciona `users.role`, restrição para `USER`/`ADMIN` e a tabela `admin_audit`. O administrador da ação pode ficar nulo após exclusão LGPD, mas ação, alvo e horário permanecem para integridade operacional.

## Testes

- Usuário comum recebe `403`.
- Administrador acessa o dashboard.
- Alteração de usuário é registrada na auditoria.
- Administrador não remove o próprio acesso.
- Suíte backend completa permanece verde.

## Limitações deliberadas

- Não existe senha padrão ou endpoint público para criar administrador.
- Não existe painel web; ele pode ser criado posteriormente consumindo esta API.
- Reports de usuários sobre ofertas pertencem à FASE 49; nesta fase `reports` significa resumo operacional.
- Paginação e otimização das listas serão reforçadas nas FASES 25 e 28 antes de grande volume.
