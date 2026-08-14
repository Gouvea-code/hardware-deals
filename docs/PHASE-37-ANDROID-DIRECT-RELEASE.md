# FASE 37 — Android para distribuição direta

## Configuração

- `applicationId`: `com.hardwaredeals`.
- `versionCode`: `1`.
- `versionName`: `1.0.0`.
- Variante `beta`: sufixo `.beta`, assinatura de depuração e bundle JavaScript incluído.
- Variante `release`: não usa mais a chave debug; exige keystore externo por propriedades `HD_UPLOAD_*`.

O APK beta é adequado apenas para teste direto e deve ser divulgado com fingerprint SHA-256. Ele não substitui uma release assinada definitiva. A URL da API é lida de `.env.beta`, criado fora do Git a partir de `.env.beta.example`.

## APK gerado

Foi publicada uma beta técnica para emulador, usando a API local:

- [Download do APK](https://github.com/Gouvea-code/hardware-deals/releases/download/direct-beta-1.0.0-local/hardware-deals-beta-local.apk)
- Tamanho: 167.668.262 bytes.
- SHA-256: `d7da4c1f45878c89c78d53a42edfa2b3fd927659d6d552dd8196c83daa7d8559`.

Não existe API pública provisionada. Portanto, esta beta não é indicada para celulares reais fora do ambiente de desenvolvimento. A beta pública será gerada somente após configurar `PUBLIC_API_BASE_URL`.

Quando a API existir:

```bash
cd mobile
copy .env.beta.example .env.beta
cd android
gradlew.bat assembleBeta
```

O arquivo será produzido em `mobile/android/app/build/outputs/apk/beta/app-beta.apk`.
