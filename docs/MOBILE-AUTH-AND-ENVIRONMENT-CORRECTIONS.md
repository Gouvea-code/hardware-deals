# Correções de autenticação e ambientes mobile

## Autenticação

- telas de login, cadastro e recuperação de senha.
- tokens armazenados no Keychain/Keystore pelo `react-native-keychain` 10.
- restauração da sessão antes de renderizar a navegação.
- refresh token rotacionado automaticamente após resposta `401`, com uma única
  requisição concorrente.
- falha de refresh limpa a sessão segura.
- logout revoga o refresh token, desativa o dispositivo FCM e limpa credenciais.

O cadastro exige verificação do e-mail antes do primeiro login, conforme o
contrato existente do backend.

## Ambientes

Copiar o exemplo correspondente para um arquivo não versionado:

```text
.env.development.example -> .env.development
.env.staging.example     -> .env.staging
.env.production.example  -> .env.production
```

Android seleciona desenvolvimento em debug e produção em release. Para staging
ou builds iOS, definir `ENVFILE` no processo de build. URLs reais de staging e
produção devem substituir os domínios de exemplo e usar HTTPS.
