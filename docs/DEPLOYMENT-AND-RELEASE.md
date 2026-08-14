# Implantação, ambientes e publicação

## Ambientes

- Desenvolvimento usa `docker-compose.yml` e o perfil padrão.
- Staging deve usar infraestrutura isolada, domínio próprio e `SPRING_PROFILES_ACTIVE=prod`.
- Produção pode partir de `docker-compose.production.yml`, sempre atrás de um proxy HTTPS.
- Segredos nunca entram no Git. Use o cofre do provedor e monte a conta de serviço Firebase como secret somente no backend.

## Procedimento

1. Copiar `.env.production.example` para um arquivo externo ao repositório e gerar valores exclusivos.
2. Criar banco PostgreSQL com backup, retenção e restauração testada.
3. Configurar DNS, TLS, SMTP e Firebase Cloud Messaging.
4. Executar migrações e verificar `/actuator/health/readiness` antes de liberar tráfego.
5. Rodar um teste em staging: cadastro, verificação de e-mail, login, refresh, favorito, alerta, push e exclusão de conta.
6. Publicar gradualmente, monitorar erros e manter uma imagem anterior disponível para rollback.

## Android / Firebase

O arquivo `google-services.json` deve ser obtido no console Firebase para o package do aplicativo e colocado localmente em `mobile/android/app/`. Ele está ignorado pelo Git. O backend usa Application Default Credentials via `GOOGLE_APPLICATION_CREDENTIALS`.

A validação final exige um aparelho físico: permitir notificações, registrar o token, criar um alerta acima do preço atual, executar o engine e confirmar recebimento com o app em primeiro e segundo plano.

## Checklist de publicação

- Política de privacidade publicada em URL HTTPS.
- Formulário de segurança de dados da loja preenchido conforme a coleta real.
- Conta de teste e instruções fornecidas para revisão.
- Ícone, screenshots, descrição, classificação etária e contato de suporte revisados.
- App Bundle assinado com chave protegida e Play App Signing habilitado.
- Testes interno, fechado e aberto concluídos antes da produção.

## Pendências externas

A criação dos ambientes, as credenciais Firebase/SMTP, domínio, assinatura Android e publicação nas lojas dependem das contas do proprietário e não podem ser concluídas apenas pelo código-fonte.
