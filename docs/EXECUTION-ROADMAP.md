# Cronograma para executar e publicar o Hardware Deals

Estimativa criada em 14 de agosto de 2026. Os prazos são dias úteis e começam quando as credenciais e decisões necessárias estiverem disponíveis.

## Meta A — Rodar localmente

O código já está preparado. Nesta máquina, o bloqueio atual é o Docker Desktop, que respondeu “unable to start”.

| Ordem | Atividade | Responsável | Estimativa | Dependência | Resultado |
| --- | --- | --- | --- | --- | --- |
| 1 | Reparar/reinstalar Docker Desktop e habilitar virtualização/WSL 2 | proprietário da máquina | 0,5–1 dia | acesso administrativo e reinicialização | `docker info` responde |
| 2 | Subir PostgreSQL, Redis e Mailpit | desenvolvimento | 15 min | Docker | `docker compose ps` saudável |
| 3 | Iniciar backend e aplicar Flyway | desenvolvimento | 15–30 min | serviços locais | health e Swagger disponíveis |
| 4 | Instalar Android SDK/emulador e copiar `.env.development` | proprietário + desenvolvimento | 0,5–1 dia | Android Studio | emulador conectado |
| 5 | Compilar e abrir o mobile | desenvolvimento | 30–60 min | backend e Android | Home aberta no emulador |
| 6 | Cadastrar, verificar pelo Mailpit e testar login | QA/desenvolvimento | 30 min | app e backend | fluxo autenticado funcional |

Prazo esperado para a aplicação básica local: **1 a 2 dias úteis**, principalmente condicionado ao Docker/Android da máquina. Firebase e marketplaces reais não são necessários para abrir e navegar no app local.

## Meta B — MVP integrado em staging

| Semana | Entregas | Dependências externas |
| --- | --- | --- |
| 1 | FASE 26 concluída; reparar Docker local e iniciar a FASE 27 de segurança | máquina com Docker |
| 2 | FASE 27 concluída; escolher cofre/provedor e executar revisão externa em staging | decisão sobre cofre/provedor |
| 3 | FASE 28: paginação, índices, cache e teste de carga | volume de teste |
| 4 | Consolidar FASE 29 e criar staging da FASE 30 | conta no provedor de nuvem |
| 5 | Configurar domínio, TLS, SMTP, PostgreSQL, Redis e backups | domínio e credenciais SMTP |
| 6 | Configurar Firebase e validar push em aparelho físico | projeto Firebase e dispositivo Android |
| 7–8 | Primeiro connector autorizado, recomendado Mercado Livre | aplicação OAuth/contrato |
| 9 | Teste completo em staging, observabilidade e correções | todos os serviços anteriores |

Prazo estimado para MVP de staging: **7 a 9 semanas**. Sem credenciais de marketplace, staging pode operar com feed de teste, mas não com ofertas reais.

## Meta C — Publicação Android

| Semana | Entregas |
| --- | --- |
| 10 | LGPD e política revisadas; site público de suporte/privacidade |
| 11 | Play Console, application ID definitivo, assinatura e Data Safety |
| 12 | Assets da loja, teste interno e correções |
| 13 | Teste fechado com usuários reais |
| 14 | Liberação gradual em produção e monitoramento |

Prazo total estimado até publicação responsável: **12 a 14 semanas**, desde que contas, revisão jurídica, domínio e credenciais sejam fornecidos sem atraso.

## Marketplaces

Os connectors podem seguir em paralelo após segurança e staging:

| Marketplace | Estimativa após acesso | Bloqueio atual |
| --- | --- | --- |
| Mercado Livre | 1–2 semanas | OAuth, escopos e conta parceira |
| Amazon Brasil | 1–2 semanas | elegibilidade e Creators API |
| Magazine Luiza | 1–2 semanas | acesso seller/parceiro |
| Shopee | 1–2 semanas | Open Platform/feed autorizado |
| KaBuM! | indefinida até resposta | API/feed formal não confirmado |

As estimativas não significam scraping. Cada integração depende de autorização e testes de contrato.

## Ações necessárias do proprietário agora

1. Reparar o Docker Desktop/WSL 2.
2. Confirmar se Android Studio e um aparelho/emulador estão disponíveis.
3. Criar ou fornecer o projeto Firebase.
4. Escolher provedor de staging e domínio.
5. Solicitar acessos oficiais aos marketplaces.
6. Definir dados institucionais para política de privacidade e Play Console.

## Critério de “podemos rodar”

- Local básico: etapas 1–6 da Meta A concluídas.
- Staging real: banco, Redis, SMTP, Firebase, TLS, backup e ao menos um collector autorizado validados.
- Produção: segurança, LGPD, teste fechado, monitoramento e rollback aprovados.
