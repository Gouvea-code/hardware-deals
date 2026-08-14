# Revisão do projeto após a FASE 20

## Estado verificado

- FASES 00 a 20 possuem implementação e documentação de checkpoint.
- suíte atual: 15 testes mobile e 47 testes backend.
- TypeScript e lint mobile passam sem avisos.
- favoritos, alertas e dispositivos são isolados pelo usuário autenticado.
- o roteiro ainda contém as FASES 21 a 50.

## Pendências prioritárias antes da FASE 21

### 1. Autenticação no aplicativo mobile

O backend possui cadastro, verificação, login, refresh, logout e recuperação, mas
o aplicativo não oferece essas telas nem persiste a sessão. O token do Zustand
existe apenas em memória. Sem este fluxo, favoritos, alertas e registro FCM não
podem ser usados por um usuário real.

Recomendação: criar um checkpoint corretivo antes da FASE 21 com login/cadastro,
armazenamento seguro de access/refresh token, restauração da sessão, rotação de
refresh, logout e desativação do token FCM.

### 2. Configuração por ambiente

O mobile usa URL local fixa. É necessário separar desenvolvimento, staging e
produção, impedir HTTP fora de desenvolvimento e definir a URL da API no build.

### 3. Credenciais e validação nativa

Adicionar fora do Git:

- `google-services.json` no Android.
- `GoogleService-Info.plist` e capacidades de push no iOS.
- credenciais APNs no Firebase.

O build Android não foi executado porque o SDK Android não está instalado neste
ambiente. O build iOS exige macOS, CocoaPods e assinatura. A entrega de push deve
ser comprovada em dispositivo físico antes de produção.

## Próximas fases funcionais

1. **FASE 21 — Alert Engine:** job, comparação de preço, Firebase Admin no
   backend, registro em `notifications`, antispam com `last_notified_at`, retentativa
   e desativação de tokens inválidos.
2. **FASE 22 — Redirecionamento:** registrar clique e abrir a oferta externa com
   validação de URL.
3. **FASE 23 — Analytics:** eventos mínimos, consentimento e política de retenção.
4. **FASE 24 — Administração:** escopo, papéis e autorização administrativa.

## Qualidade e operação pendentes

- A CI atual apenas imprime uma mensagem; lint, compilação, testes, build e scan
  pertencem à FASE 29 e ainda não protegem a branch.
- Os testes de integração com PostgreSQL/Testcontainers são excluídos da suíte
  padrão e dependem de Docker.
- Faltam testes de telas completas, autenticação mobile, concorrência/idempotência
  e fluxos reais de push.
- O npm registra 8 alertas altos transitivos em `image-size 1.2.1`, vindo do Metro;
  a correção automática propõe downgrade incompatível. Deve ser acompanhado até
  uma atualização compatível do React Native/Metro.
- Spring Boot 3.2.0 e outras dependências devem passar por atualização e scan na
  FASE 27.
- Falta observabilidade para falhas de registro/envio FCM, latência e collectors.
- A resposta global de ofertas não é paginada; o impacto deve ser medido antes do
  crescimento do catálogo.

## Segurança, privacidade e lançamento

Ainda faltam as FASES 27 e 30–45: CORS explícito, HTTPS, gestão de secrets,
staging, produção, backup/restore, LGPD, exclusão de conta, política de
privacidade, Data Safety, revisão de permissões, assinatura e trilhas de teste da
Google Play.

## Sequência recomendada

1. checkpoint corretivo de autenticação e ambientes mobile.
2. configurar Firebase e validar builds/dispositivos.
3. FASE 21 e FASE 22.
4. ampliar testes negativos e integração antes de analytics/admin.
5. antecipar CI básica da FASE 29 para impedir regressões nas fases seguintes.
