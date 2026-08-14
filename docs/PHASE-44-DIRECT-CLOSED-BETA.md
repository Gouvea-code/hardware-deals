# FASE 44 — Beta fechado por distribuição direta

## Objetivo

Executar um beta controlado sem Google Play, com pessoas convidadas, APK identificado por versão e canal único de feedback. O arquivo nunca deve conter segredo ou apontar para ambiente local.

## Organização

- criar uma GitHub Release de pré-lançamento com APK assinado e SHA-256;
- convidar inicialmente de 5 a 12 pessoas com aparelhos variados;
- enviar o link da página oficial de download, política de privacidade e instruções;
- manter o teste por 7 a 14 dias ou até cobrir todos os fluxos críticos;
- registrar erros pelo formulário `Erro no beta interno` do GitHub;
- revogar/retirar uma versão com problema e publicar outra tag, sem substituir binários silenciosamente.

## Matriz mínima

| Grupo | Meta |
|---|---|
| Android | versões suportadas mínima, intermediária e recente |
| Fabricante | ao menos três fabricantes |
| Rede | Wi-Fi e rede móvel |
| Conta | cadastro novo e conta existente |
| Push | app aberto, em segundo plano e fechado |
| Marketplace | cada integração habilitada com preço e redirecionamento válidos |

## Métricas de saída

- 100% dos fluxos críticos aprovados;
- zero crash bloqueador aberto;
- zero preço associado ao produto ou loja errados;
- zero segredo ou dado pessoal exposto;
- taxa de sucesso registrada para autenticação, carregamento da Home e abertura de ofertas;
- problemas não bloqueadores triados para a próxima versão.

## Estado

Plano pronto para execução. A fase somente será concluída após existirem API pública, APK de produção e resultados reais documentados. Recrutamento de pessoas e passagem do período de teste são atividades externas que não podem ser simuladas pelo código.
