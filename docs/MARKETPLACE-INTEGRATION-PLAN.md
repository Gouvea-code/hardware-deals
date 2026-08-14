# Plano de integração dos marketplaces

Os cinco marketplaces já são cadastrados pela migração `V1_12`. O pipeline existente aceita ofertas normalizadas por feed JSON autorizado, valida conteúdo, associa produtos e preserva histórico de preços.

## Estratégia por canal

| Marketplace | Canal autorizado | Situação para ativação |
| --- | --- | --- |
| Mercado Livre | API oficial de itens, preços e notificações | Criar aplicação, obter OAuth do seller/parceiro e definir catálogo permitido. O campo antigo de preço de `/items` está em descontinuação; usar o recurso oficial de preço. |
| Amazon Brasil | Creators API | Associar-se ao programa, cumprir os requisitos de acesso e gerar credenciais. Não iniciar integração nova com PA-API 5, já descontinuada. |
| Magazine Luiza | APIs Magalu para sellers/parceiros | Solicitar acesso e confirmar que o contrato permite consumir catálogo e preço para comparação. |
| Shopee | Open Platform ou feed de afiliado/parceiro | Obter `partner_id`, chave e autorização aplicável à operação brasileira. |
| KaBuM! | Feed/API formal de afiliado ou parceiro | Solicitar documentação e autorização; não foi localizada API pública de catálogo adequada. |

## Contrato de entrada atual

Cada conector deve converter a resposta oficial para `CollectedOffer`, incluindo `storeSlug`, identificador externo, SKU, nome, marca, modelo, categoria, EAN, URL, preço, disponibilidade e horário. Os slugs aceitos são `mercado-livre`, `amazon-brasil`, `kabum`, `magazine-luiza` e `shopee`.

## Regras obrigatórias

- Não raspar páginas nem contornar autenticação, CAPTCHA ou limites.
- Respeitar termos de exibição, cache, atribuição, links de afiliado e validade do preço de cada parceiro.
- Guardar credenciais no cofre do ambiente, rotacioná-las e nunca registrá-las em logs.
- Aplicar timeout, retry com backoff, rate limit, métricas e circuit breaker em cada adaptador.
- Validar em sandbox quando oferecido e liberar cada canal separadamente por feature flag.

## Próximo passo externo

O código específico de cada API só pode ser finalizado depois do recebimento das credenciais, escopos aprovados, IDs de seller/afiliado e confirmação contratual. Até lá, o feed JSON autorizado permite integrar dados fornecidos diretamente por um parceiro sem acoplar o domínio a um formato proprietário.
