# FASE 38 — Target API Android

O projeto está configurado com:

- `minSdkVersion 24` — Android 7.0 ou superior.
- `targetSdkVersion 36` — Android 16.
- `compileSdkVersion 37` e Build Tools 37.

O target 36 atende a direção prevista para agosto de 2026, mas a exigência deve ser conferida novamente antes de qualquer submissão, pois a política é mutável. Referência: [requisitos de nível de API do Google Play](https://developer.android.com/google/play/requirements/target-sdk?hl=pt-br).

Mesmo sem Play Store, manter o target atualizado preserva compatibilidade com segurança, permissões e comportamento das versões recentes do Android. A CI compila o projeto Android em cada push.
