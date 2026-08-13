# FASE 13 — Mobile inicial

## Objetivo

Criar a fundação do aplicativo React Native sem antecipar as funcionalidades da Home.

## Stack congelada

- React Native 0.87.0 e React 19.2.3.
- TypeScript 6.
- React Navigation 7 com navegação nativa em pilha.
- TanStack Query 5 e Axios 1 para acesso remoto.
- Zustand 5 para estado local.

As versões exatas estão registradas em `mobile/package.json` e no lockfile do npm.

## Estrutura

O diretório `mobile/src` contém componentes, telas, navegação, serviços, hooks,
estado, tipos, utilitários e tema. O projeto inclui os módulos nativos Android e
iOS gerados pelo template oficial.

## Fundação implementada

- `RootNavigator` com a rota inicial tipada.
- tema centralizado com cores, espaçamentos e tipografia.
- componentes base `Screen`, `AppText` e `AppButton`.
- cliente Axios apontando para o backend local (`10.0.2.2` no emulador Android
  e `localhost` nas demais plataformas).
- interceptor de autenticação conectado ao estado de sessão.
- TanStack Query configurado para cache de dados remotos.
- store Zustand de sessão.
- tipos e utilitários compartilhados.

## Execução

```text
cd mobile
npm install
npm run android
```

O endereço da API deve ser configurado de acordo com o ambiente antes de uma
distribuição. O valor atual atende ao emulador Android acessando o backend local.

## Validação

- verificação de tipos TypeScript.
- lint do projeto completo.
- 3 testes automatizados.

O build nativo não foi executado neste ambiente porque o Android SDK não está
instalado. A auditoria do npm registra 8 alertas altos originados por
`image-size 1.2.1`, dependência transitiva do Metro 0.87. A correção automática disponível
propõe um downgrade incompatível do React Native; por isso ela não foi aplicada.

## Checkpoint

FASE 13 concluída. A Home contém somente um estado inicial; ofertas, categorias,
cards e demais requisitos pertencem à FASE 14.
