# Inventário de segurança de dados

Este inventário descreve o código atual e serve para política pública e distribuição direta. Deve ser revisado quando Firebase, hospedagem, analytics ou funcionalidades mudarem.

| Categoria | Coletado | Compartilhado com operador | Finalidade | Obrigatório | Exclusão |
| --- | --- | --- | --- | --- | --- |
| Nome | sim | hospedagem | conta e identificação no serviço | para conta | com a conta |
| E-mail | sim | hospedagem e SMTP | login, verificação e recuperação | para conta | com a conta |
| Senha | transmitida; somente hash BCrypt persistido | hospedagem | autenticação | para conta | com a conta |
| Identificador interno do usuário | sim | hospedagem | relacionar preferências e segurança | para conta | com a conta |
| Refresh/reset/verify tokens | somente hash persistido | hospedagem | sessão e segurança | funcional | por expiração, uso ou conta |
| Token FCM e plataforma | quando push é ativado | hospedagem e Google Firebase | entregar notificações | opcional | logout, invalidação ou conta |
| Favoritos e alertas | quando usados | hospedagem | funcionalidade solicitada | opcional | pelo usuário ou com a conta |
| Cliques em ofertas | sim ao abrir oferta | hospedagem | redirecionamento e métricas próprias | funcional | com a conta; anônimo segue retenção |
| Eventos de uso | sim | hospedagem | melhoria e medição próprias | funcional | 90 dias por padrão ou conta |
| Histórico de notificações | quando há alertas | hospedagem/Firebase | informar e controlar alertas | opcional | com a conta |
| Endereço de rede | processado | hospedagem/Redis | rate limiting e segurança | funcional | contador em cerca de 2 minutos; logs conforme provedor |

## Declarações

- Não há venda de dados pessoais.
- Não há SDK de anúncios nem publicidade comportamental.
- Não são coletados localização precisa, contatos, fotos, áudio, arquivos, saúde ou dados financeiros.
- Compras e pagamentos acontecem diretamente nas lojas.
- A transmissão pública deve usar HTTPS; o APK não será liberado antes da API HTTPS.
- O usuário pode solicitar exclusão dentro do app ou pela rota documentada no site.

## Verificação por release

1. Comparar dependências mobile e backend com este inventário.
2. Revisar permissões do AndroidManifest.
3. Conferir eventos emitidos pelo `analyticsService`.
4. Confirmar operadores realmente contratados e retenções.
5. Atualizar política pública antes de distribuir o novo APK.
