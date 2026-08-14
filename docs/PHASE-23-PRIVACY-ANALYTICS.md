# FASE 23 — Analytics com privacidade

## Objetivo

Medir o uso essencial do produto sem integrar plataformas publicitárias e sem coletar conteúdo desnecessário.

## Eventos implementados

| Evento | Origem | Contexto permitido |
| --- | --- | --- |
| `APP_OPEN` | inicialização mobile | nenhum |
| `SEARCH` | conclusão de uma busca | nenhum; o texto não é enviado |
| `PRODUCT_VIEW` | tela de detalhes | `productId` |
| `FAVORITE` | favorito criado com sucesso | `productId` e usuário autenticado |
| `ALERT_CREATED` | alerta salvo com sucesso | `productId` e usuário autenticado |
| `NOTIFICATION_OPEN` | abertura de push | `notificationId` pertencente ao usuário |
| `OFFER_CLICK` | redirecionamento da FASE 22 | produto, oferta e usuário opcional, definidos no servidor |

## Privacidade e integridade

- Não são aceitos texto pesquisado, e-mail, URL, token, IP em payload, identificador de publicidade ou mapa livre de propriedades.
- Eventos personalizados exigem autenticação e validação de propriedade.
- `OFFER_CLICK` não pode ser criado pelo endpoint genérico; isso evita métricas forjadas pelo cliente.
- Falha de analytics no mobile não bloqueia busca, favorito, alerta ou navegação.
- Exclusão de conta remove os eventos ligados ao usuário.
- Eventos anônimos não contêm identificador persistente capaz de reconstruir a identidade.

## Persistência e retenção

A migration `V1_14__Create_analytics_events_table.sql` cria a tabela e os índices por tipo, usuário e horário. Um job diário elimina registros anteriores à retenção configurada em `ANALYTICS_RETENTION`, cujo padrão é `P90D` (90 dias).

## API

`POST /api/v1/analytics/events` recebe somente:

```json
{
  "eventType": "PRODUCT_VIEW",
  "productId": "uuid-opcional-conforme-o-evento",
  "notificationId": "uuid-opcional-conforme-o-evento"
}
```

Agregações e dashboard serão expostos apenas depois da autorização administrativa da FASE 24. Não existe endpoint público de leitura dos eventos.

## Validação

- Eventos anônimos mínimos.
- Contexto permitido e rejeição de contexto excedente.
- Autenticação para eventos personalizados.
- Proteção do evento controlado pelo servidor.
- Integração automática com redirecionamento e push.
- Testes mobile de envio e tolerância offline.
