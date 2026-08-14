# O que falta para finalizar o Hardware Deals

Atualizado após a FASE 32. Este arquivo separa desenvolvimento, validação externa e evolução futura para evitar que preparação seja confundida com conclusão.

## 1. Próxima fase recomendada

### FASE 33 — Site público

Criar as páginas públicas de produto, privacidade, termos, suporte e exclusão de conta.

## 2. Produto e operação ainda pendentes

### Administração — melhorias posteriores

- Criar interface web administrativa quando houver decisão de hospedagem.
- Paginar listas administrativas e adicionar filtros.
- Integrar reports de usuários quando a FASE 49 existir.

### FASES 25 e 26 — Testes completos e negativos

- Expandir testes de services, controllers, mobile e navegação.
- Cobrir expiração, concorrência, duplicidade, indisponibilidade e autorização cruzada.
- Executar e estabilizar Testcontainers com Docker real.
- Adicionar testes de contrato API/mobile e fluxo completo em staging.

### FASE 27 — Segurança

- Threat model e revisão OWASP.
- Rate limiting distribuído com Redis em vez de memória local.
- Headers de segurança, rotação de segredos, auditoria e política de logs.
- Resolver alertas transitivos altos do toolchain React Native por atualização compatível.
- Automatizar análise de dependências Java e análise estática.

### FASE 28 — Performance

- Medir consultas e eliminar N+1.
- Paginação onde ainda existem listas sem limite.
- Cache Redis com invalidação e métricas.
- Índices orientados por consultas reais e teste de carga.

### FASE 29 — CI

A CI básica já foi antecipada. Ainda falta proteger a branch, exigir checks, publicar relatórios, cachear dependências com segurança e produzir artefatos Android reproduzíveis.

## 3. Infraestrutura e publicação

### FASES 30 a 33 — Staging, produção, domínio e site público

O repositório contém Dockerfile, composição e configurações, mas nenhum ambiente foi criado. Falta escolher o provedor, provisionar banco/Redis, backups, observabilidade, domínio, DNS, TLS, SMTP, cofre de segredos e uma página pública de suporte/privacidade.

### FASES 34 e 35 — LGPD e política de privacidade

Existe exclusão técnica de conta e uma base documental. Falta revisão jurídica, definição formal de controlador/operadores, bases legais, retenção, canal do encarregado, atendimento ao titular, tratamento de backups e publicação da política em URL HTTPS.

### FASES 36 a 45 — Google Play e Android Release

- Criar e verificar a conta Play Console.
- Definir application ID definitivo, versionamento e assinatura.
- Configurar Play App Signing e proteger a keystore.
- Confirmar target API exigida na data de publicação.
- Revisar permissões e preencher Data Safety.
- Produzir ícone, screenshots, textos e contato de suporte.
- Realizar testes interno, fechado e produção gradual.

### FASE 46 — Pós-lançamento

Monitoramento de crash, latência, falhas de collector/push, suporte, incidentes, rollback e rotina de atualizações.

## 4. Marketplaces e qualidade dos dados

### Credenciais e contratos

Obter acesso formal para:

- Mercado Livre: aplicação OAuth e escopos para itens, preços e notificações.
- Amazon Brasil: conta elegível e credenciais da Creators API.
- Magazine Luiza: acesso de seller/parceiro às APIs Magalu.
- Shopee: Open Platform ou feed de afiliado autorizado para o Brasil.
- KaBuM!: feed/API formal de parceiro; não há conector público confirmado no projeto.

### FASE 47 — Operação dos collectors

Criar um adaptador por marketplace com feature flag, paginação, rate limit, retry com backoff, circuit breaker, métricas, deduplicação e testes de contrato. O feed JSON atual é a porta de entrada genérica, não equivale aos cinco conectores finalizados.

### FASES 48 e 49 — Qualidade e reports

- Detectar preço falso, frete relevante, estoque, cupom expirado e outliers.
- Permitir que o usuário reporte oferta incorreta e acompanhar resolução.

### FASE 50 — Monetização futura

Links afiliados e publicidade somente após contratos, transparência, consentimento aplicável e garantia de que ranking/Deal Score não será manipulado.

## 5. Evoluções posteriores previstas no roteiro

- FASE 51: IA no produto com objetivo, dados e avaliação definidos.
- FASE 52: notificações por Discord e Telegram.
- FASE 53: expansão de lojas/conectores.
- FASE 54: comparação avançada.
- FASE 55: histórico avançado.
- FASE 56: ranking.
- FASE 57: compatibilidade de componentes de PC.

Essas fases são evolução e não devem preceder segurança, testes, collectors reais e publicação estável.

## 6. Dependências que exigem ação do proprietário

| Dependência | Necessária para |
| --- | --- |
| Projeto e credenciais Firebase | push real e teste em aparelho |
| SMTP e domínio de e-mail | verificação e recuperação de senha reais |
| Conta no provedor de nuvem | staging e produção |
| Domínio e DNS | API/site público e política de privacidade |
| Contas e contratos dos marketplaces | collectors reais e links de afiliado |
| Play Console e identidade do publicador | testes e publicação Android |
| Dados institucionais e revisão jurídica | LGPD, termos e política pública |

## 7. Critérios para considerar o projeto finalizado

- Os cinco connectors autorizados operam com métricas e alertas.
- Testes unitários, integração, contrato, mobile e fluxos em aparelho estão verdes.
- Staging reproduz produção e passou pelo checklist completo.
- Segurança e privacidade foram revisadas e pendências críticas/altas tratadas.
- Backup e restauração foram testados.
- Firebase, e-mail e notificações funcionam em dispositivo físico.
- Política de privacidade e suporte estão publicados.
- Aplicativo passou por teste fechado e foi liberado gradualmente.
- Monitoramento, incidentes e rollback têm responsáveis definidos.

## 8. Ordem prática recomendada

1. FASE 33.
2. Escolher provedor e domínio para materializar as FASES 30 a 32.
3. Credenciais e connectors da FASE 47 em paralelo ao staging.
4. FASE 30–35 com validação Firebase física.
5. FASE 36–45 para publicação.
6. FASE 46, 48 e 49 para operação sustentável.
7. Somente então priorizar FASES 50–57 conforme uso real.
