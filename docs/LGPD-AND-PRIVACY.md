# LGPD — inventário e processo operacional

> Base técnica para revisão jurídica. O controlador, CNPJ/CPF publicador, encarregado e canal oficial precisam ser preenchidos antes do lançamento.

## Inventário de tratamento

| Dados | Finalidade | Armazenamento | Retenção técnica inicial | Compartilhamento necessário |
| --- | --- | --- | --- | --- |
| Nome, e-mail e hash BCrypt | criar, autenticar e recuperar a conta | PostgreSQL | vida da conta | provedor de e-mail e hospedagem |
| Tokens de sessão em hash | manter e proteger sessões | PostgreSQL | até expirar, uso ou revogação | hospedagem |
| Favoritos e alertas | executar escolhas do usuário | PostgreSQL | vida da conta ou remoção pelo usuário | hospedagem |
| Token FCM e plataforma | entregar push solicitado | PostgreSQL e Firebase | até logout, invalidação ou exclusão | Google Firebase |
| Histórico de notificações | demonstrar alertas enviados | PostgreSQL | vida da conta, sujeito à política final | hospedagem/Firebase |
| Cliques e eventos de uso | medir funcionamento e ofertas | PostgreSQL | 90 dias por padrão | hospedagem; nenhum terceiro analítico hoje |
| Logs técnicos e endereço de rede | segurança, diagnóstico e rate limiting | logs/Redis | janela do rate limit de 2 min; logs conforme provedor | hospedagem/observabilidade |

Dados comerciais de produtos, preços e lojas não identificam por si só um titular. Não são coletados localização precisa, contatos, fotos, microfone, dados financeiros ou publicidade comportamental.

## Exclusão e direitos

O endpoint autenticado `DELETE /api/v1/auth/me`, confirmado por senha, remove em transação eventos vinculados, auditorias administrativas do titular, notificações, cliques, alertas, favoritos, dispositivos, tokens e a conta. O teste automatizado confirma a remoção.

Solicitações de confirmação, acesso, correção, portabilidade, oposição, revogação e revisão devem seguir este processo:

1. registrar protocolo e data;
2. confirmar identidade sem pedir dados excessivos;
3. classificar o direito solicitado e sistemas afetados;
4. responder no prazo jurídico aplicável;
5. executar e registrar a ação;
6. informar limitações legais, backups e operadores alcançados.

Backups não devem restaurar permanentemente contas eliminadas. Após restore, a lista de supressão/exclusões deve ser reaplicada conforme procedimento aprovado pelo responsável jurídico.

## Governança antes do lançamento

- Definir controlador, operadores, bases legais e canal do encarregado.
- Firmar contratos com hospedagem, SMTP, Firebase e observabilidade.
- Manter registro das operações, controle de acesso e resposta a incidentes.
- Validar transferências internacionais e localização dos provedores.
- Revisar retenções com jurídico e configurar descarte verificável.
- Atualizar inventário e política antes de habilitar nova coleta, analytics ou publicidade.

Referências oficiais: [direitos previstos pela LGPD — ANPD](https://www.gov.br/anpd/pt-br/assuntos/direitos-dos-titulares) e [Lei nº 13.709/2018](https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/l13709.htm).
