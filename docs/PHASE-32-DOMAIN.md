# FASE 32 — Domínio e DNS

O registro é uma ação comercial externa e não foi realizado automaticamente. Antes da compra, o proprietário deve escolher o nome e verificar disponibilidade e marcas aplicáveis no Registro.br.

## Estrutura planejada

| Nome | Finalidade | Registro sugerido |
| --- | --- | --- |
| `www.DOMINIO` | futuro site público | CNAME para hospedagem web |
| `api.DOMINIO` | API | A/AAAA ou CNAME para o proxy de produção |
| `privacy.DOMINIO` | política de privacidade | redirecionamento para `/privacy` |
| `terms.DOMINIO` | termos | redirecionamento para `/terms` |
| `support.DOMINIO` | suporte | redirecionamento para `/support` |

Também devem ser configurados CAA, SPF, DKIM e DMARC. TTL inicial recomendado: 300 segundos durante a implantação; após estabilizar, 3600 segundos. Nunca publique o DNS da API antes do certificado TLS e health check estarem aprovados.

As páginas correspondentes pertencem à FASE 33. Até sua publicação, os subdomínios jurídicos e de suporte não devem apontar para conteúdo inexistente.
