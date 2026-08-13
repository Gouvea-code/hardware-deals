# Correções técnicas após a FASE 08

## Escopo

Correção somente dos problemas críticos e altos encontrados na auditoria anterior à FASE 09.

## Correções realizadas

- Nomes de índices SQL e JPA agora são exclusivos em todo o schema PostgreSQL.
- EAN passou a possuir restrição única no banco e no mapeamento JPA.
- O perfil H2 não herda mais o dialect PostgreSQL.
- O perfil `local` deixou de ser ativado automaticamente pelo arquivo-base.
- Somente `/actuator/health` permanece público e seus detalhes internos ficam ocultos.
- Produção deixou de exigir TLS embutido sem keystore; cabeçalhos de proxy são respeitados para terminação HTTPS externa.
- Redefinir uma senha agora revoga todos os refresh tokens ativos do usuário.

As migrations antigas foram corrigidas diretamente porque ainda não houve banco PostgreSQL validado ou implantação de produção. Depois da primeira implantação compartilhada, migrations aplicadas não devem mais ser editadas.

## Testes

- Regressão de autenticação confirma que refresh token anterior ao reset deixa de funcionar.
- Teste do Actuator confirma health público sem componentes e demais endpoints protegidos.
- Teste de integridade verifica nomes de índices únicos e restrição única de EAN nas migrations.
- Suíte H2 executada sem os erros anteriores de criação de índices.

## Validação PostgreSQL

O Docker daemon não estava em execução durante esta correção. Portanto, Testcontainers não pôde validar Flyway em PostgreSQL real. Essa validação continua obrigatória assim que o Docker estiver disponível, antes de qualquer implantação.

## Checkpoint

Problemas críticos e altos conhecidos foram corrigidos. A FASE 09 ainda não foi iniciada.
