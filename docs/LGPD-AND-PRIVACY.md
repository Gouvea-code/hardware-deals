# LGPD e privacidade — base técnica

Este documento é uma base operacional e deve ser revisado por responsável jurídico antes da publicação.

## Dados tratados

- Conta: nome, e-mail e hash bcrypt da senha.
- Preferências: favoritos, alertas de preço e histórico de notificações.
- Dispositivo: token FCM e plataforma, usados exclusivamente para push solicitado pelo usuário.
- Segurança: tokens de sessão armazenados como hash no servidor.

## Finalidades e controles

Os dados são usados para autenticação e para entregar alertas escolhidos pelo titular. O acesso é autenticado, os tokens de curta duração são renovados com rotação e credenciais não devem ser registradas em logs. Defina formalmente bases legais, prazos de retenção, operador/controlador e canal do encarregado antes do lançamento.

## Direitos do titular

O endpoint autenticado `DELETE /api/v1/auth/me`, com confirmação de senha, elimina conta, tokens, dispositivos, favoritos, alertas e notificações em uma transação. A política pública deve ainda explicar acesso, correção, portabilidade, revogação e contato para solicitações.

## Operação obrigatória

- Manter inventário e registro das operações de tratamento.
- Coletar somente o necessário e limitar acesso por função.
- Definir retenção para backups e expiração segura.
- Ter processo de incidente, comunicação e auditoria.
- Registrar fornecedores/suboperadores de e-mail, nuvem, Firebase e analytics.
- Não habilitar analytics ou publicidade sem atualizar consentimento e política.
