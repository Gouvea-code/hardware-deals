# FASE 43 — Teste interno por distribuição direta

## Escopo

Validar o APK assinado fora da Play Store em pelo menos um aparelho Android físico. O teste deve usar a mesma API HTTPS e a mesma versão candidata à distribuição pública.

## Identificação da execução

- versão/tag do APK:
- SHA-256 conferido:
- modelo e versão do Android:
- URL da API (sem segredos):
- data e responsável:

## Roteiro obrigatório

| Item | Resultado | Evidência/observação |
|---|---|---|
| APK baixa, instala e abre sem alerta inesperado | Pendente | |
| cadastro e verificação de e-mail | Pendente | |
| login, persistência da sessão e refresh automático | Pendente | |
| logout remove a sessão local | Pendente | |
| Home mostra ofertas reais | Pendente | |
| Busca retorna Mercado Livre/Amazon/KaBuM!/Magazine Luiza habilitados | Pendente | |
| detalhe, histórico e comparação carregam | Pendente | |
| favorito persiste após reiniciar | Pendente | |
| alerta pode ser criado e removido | Pendente | |
| push chega com o app fechado e abre o destino correto | Pendente | |
| link de compra abre o marketplace correto | Pendente | |
| erros de rede são compreensíveis e recuperáveis | Pendente | |
| não há crash ou dado sensível nos registros | Pendente | |

## Aceite

Todos os itens críticos devem estar aprovados. Uma falha de autenticação, preço incorreto, link para loja errada, crash, vazamento de segredo ou push quebrado bloqueia a versão. Defeitos devem registrar versão, dispositivo, passos, resultado esperado/obtido e evidência.

## Estado

Roteiro preparado. A conclusão depende de uma API pública com coletores autorizados, credenciais Firebase válidas e execução em aparelho físico.
