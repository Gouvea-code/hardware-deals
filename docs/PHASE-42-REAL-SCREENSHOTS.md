# FASE 42 — Screenshots reais do Android

## Objetivo

Produzir imagens reais das telas Home, Busca, Produto, Histórico, Comparação e Alertas, usando uma instalação conectada à API pública e dados verdadeiros. Mockups não contam como evidência desta fase.

## Pré-requisitos

- Android SDK com `adb` disponível;
- aparelho físico ou emulador com resolução mínima de 1080 × 1920;
- APK configurado com uma API HTTPS pública;
- usuário de teste verificado, com favorito e alerta cadastrados;
- ofertas coletadas de ao menos um marketplace autorizado.

## Captura

1. Instale e abra o APK no dispositivo.
2. Entre com a conta de teste e navegue até a tela desejada.
3. Na raiz do projeto, execute, por exemplo:

   `powershell -File mobile/scripts/capture-android-screenshot.ps1 -Screen home`

4. Repita para `search`, `product`, `history`, `comparison` e `alerts`.
5. As imagens serão gravadas em `artifacts/screenshots/android/` (diretório ignorado pelo Git).

Use `-Device <serial>` quando houver mais de um dispositivo conectado.

## Critérios de aceite

- nenhuma senha, token, e-mail pessoal ou identificador sensível aparece;
- preços, loja, data e estado de carregamento são legíveis;
- não há erro de rede, placeholder, teclado ou elemento de depuração visível;
- Home e Busca exibem ofertas reais; Produto exibe preço e redirecionamento;
- Histórico e Comparação têm dados suficientes para leitura;
- Alertas mostra o fluxo autenticado;
- cada arquivo corresponde à versão exata do APK distribuído.

## Estado

Automação e checklist preparados. A fase somente poderá ser marcada como concluída após a API pública estar disponível e as seis capturas reais serem validadas em aparelho ou emulador.
