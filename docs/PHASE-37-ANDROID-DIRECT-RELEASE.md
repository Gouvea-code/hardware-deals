# FASE 37 — Android para distribuição direta

## Configuração

- `applicationId`: `com.hardwaredeals`.
- `versionCode`: `1`.
- `versionName`: `1.0.0`.
- Variante `beta`: sufixo `.beta`, assinatura de depuração e bundle JavaScript incluído.
- Variante `release`: não usa mais a chave debug; exige keystore externo por propriedades `HD_UPLOAD_*`.

O APK beta é adequado apenas para teste direto e deve ser divulgado com fingerprint SHA-256. Ele não substitui uma release assinada definitiva. A URL da API é lida de `.env.beta`, criado fora do Git a partir de `.env.beta.example`.

## Bloqueio atual

Não existe API pública provisionada. Portanto, gerar um APK com `api.example.com` produziria um aplicativo instalável, porém inutilizável. A automação deve falhar se `PUBLIC_API_BASE_URL` não estiver configurada.

Quando a API existir:

```bash
cd mobile
copy .env.beta.example .env.beta
cd android
gradlew.bat assembleBeta
```

O arquivo será produzido em `mobile/android/app/build/outputs/apk/beta/app-beta.apk`.
