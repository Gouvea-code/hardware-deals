# FASE 22 — Redirecionamento de ofertas

## Entrega

- `POST /api/v1/offers/{offerId}/click` registra o clique e devolve o destino.
- O registro contém oferta, produto, loja, usuário quando autenticado e horário.
- Cliques anônimos são aceitos com `user_id` nulo para não bloquear a compra.
- Somente ofertas e relações ativas podem ser abertas.
- O destino deve usar HTTPS e pertencer ao domínio cadastrado da loja ou a um subdomínio.
- A tela de detalhes exibe “Ver oferta”, registra o clique e só então abre o navegador externo.
- Falhas mostram mensagem ao usuário e não abrem uma URL não validada.
- A exclusão LGPD remove cliques associados à conta.

## Banco

A migration `V1_13__Create_offer_clicks_table.sql` cria `offer_clicks` e índices para oferta, usuário e horário.

## Testes

- Registro de clique anônimo e retorno do destino.
- Rejeição de domínio externo.
- Rejeição de oferta indisponível.
- Suíte completa backend, TypeScript, lint e Jest mobile.

## Limite de escopo

Agregação e painel de métricas pertencem à FASE 23. A FASE 22 apenas captura o evento necessário com dados mínimos.
