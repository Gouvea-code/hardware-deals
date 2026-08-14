# FASE 41 — Permissões Android

O manifesto solicita somente:

- `INTERNET`: acessar a API e abrir ofertas.
- `POST_NOTIFICATIONS`: enviar alertas no Android 13 ou superior, após consentimento do usuário.

Não são solicitados contatos, SMS, localização, câmera, microfone, armazenamento, telefone ou calendário. Um teste automatizado falha caso permissões sensíveis conhecidas sejam adicionadas sem revisão.

A permissão de notificação é opcional: negar não impede busca, comparação, favoritos e uso da conta; apenas desativa o recebimento de push no dispositivo.
