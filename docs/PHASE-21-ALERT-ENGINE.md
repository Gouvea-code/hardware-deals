# FASE 21 — Alert Engine

## Funcionamento

- job configurável, com execução padrão a cada 15 minutos.
- consulta alertas ativos e o preço mais recente de cada vínculo loja/produto.
- compara o menor preço disponível com o limite do usuário.
- persiste uma notificação `PRICE_ALERT` e envia FCM a dispositivos ativos.
- desativa tokens rejeitados como inválidos ou não registrados.
- usa `last_notified_at` e cooldown padrão de 24 horas para evitar spam.
- falha em um alerta não interrompe os demais.

## Firebase

O backend usa Firebase Admin Java 9.10.0 e Application Default Credentials.
Produção exige `GOOGLE_APPLICATION_CREDENTIALS` ou identidade do ambiente Google,
FCM HTTP v1 habilitado, `FIREBASE_ENABLED=true` e `ALERT_ENGINE_ENABLED=true`.

Sem credenciais, ambos permanecem desabilitados no ambiente local.

## Validação externa pendente

O fluxo foi coberto com gateway simulado. A confirmação de entrega real exige o
projeto Firebase, credenciais e um dispositivo físico registrado.

## Checkpoint

FASE 21 concluída. Nenhum requisito da FASE 22 foi iniciado.
