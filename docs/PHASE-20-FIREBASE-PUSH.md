# FASE 20 — Firebase Push

## Fluxo implementado

1. o aplicativo solicita permissão de notificação.
2. obtém o token FCM e acompanha sua renovação.
3. envia token e plataforma para `PUT /api/v1/devices` com JWT.
4. o backend cria ou reativa o registro em `device_tokens`.
5. `DELETE /api/v1/devices?token=...` desativa o dispositivo.

O Android 13 ou superior solicita `POST_NOTIFICATIONS` em tempo de execução.

## Configuração externa obrigatória

- Android: adicionar `mobile/android/app/google-services.json`.
- iOS: adicionar `GoogleService-Info.plist` ao target e habilitar Push Notifications
  e Background Modes no Xcode; depois executar `pod install`.
- configurar APNs no projeto Firebase para entrega no iOS.

O plugin Google Services do Android é aplicado somente quando o arquivo de
credenciais existe, preservando builds locais sem segredos. Esses arquivos não
devem ser versionados.

## Limite

Esta fase registra dispositivos. A avaliação do preço, controle antispam e envio
da mensagem pertencem à FASE 21.

## Checkpoint

FASE 20 concluída. Nenhum requisito da FASE 21 foi iniciado.
