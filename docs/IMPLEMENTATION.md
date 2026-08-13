# Hardware Deals — Blueprint Completo para Implementação com IA
## Do zero absoluto até publicação e operação na Google Play

**Versão:** 1.0  
**Data:** 13/08/2026  
**Objetivo:** servir como documento mestre para que uma IA de programação consiga implementar o produto de forma incremental, testável e publicável.

---

# 0. Como usar este documento

Este documento não é apenas uma lista de ideias.

Ele deve ser tratado como o **contrato de implementação do projeto**.

A implementação deve acontecer em fases. Em cada fase, a IA deve:

1. ler o objetivo da fase;
2. analisar o estado atual do repositório;
3. implementar somente o escopo solicitado;
4. criar/alterar testes;
5. executar os testes;
6. corrigir falhas;
7. atualizar documentação;
8. gerar um resumo do que foi feito;
9. parar no checkpoint da fase.

## Regra principal

**Não peça para a IA construir o projeto inteiro em uma única solicitação.**

Use uma IA de programação como agente incremental.

Exemplo:

```text
"Leia o documento docs/IMPLEMENTATION.md.
Estamos na Fase 03.
Analise o estado atual do projeto.
Implemente somente os requisitos da Fase 03.
Execute os testes.
Não avance para a Fase 04."
```

---

# 1. Visão do produto

## 1.1 Nome provisório

**Hardware Deals**

O nome poderá ser alterado depois de validação de marca, domínio e disponibilidade na Google Play.

## 1.2 Objetivo

Criar um aplicativo Android especializado em promoções de hardware.

O aplicativo deverá:

- encontrar ofertas;
- comparar preços;
- armazenar histórico;
- identificar promoções reais;
- calcular um Deal Score;
- permitir favoritos;
- permitir alertas de preço;
- enviar notificações;
- direcionar o usuário para lojas;
- futuramente trabalhar com afiliados;
- futuramente oferecer recursos premium.

## 1.3 Problema

Lojas frequentemente apresentam:

```text
DE R$ 4.999
POR R$ 3.999
```

Mas isso não significa necessariamente que R$ 3.999 seja uma boa oportunidade.

O aplicativo deverá responder:

> **O preço atual realmente está bom comparado ao histórico?**

---

# 2. Princípios do projeto

## 2.1 MVP primeiro

Não implementar tudo de uma vez.

Primeira versão:

```text
Android
+
Backend
+
PostgreSQL
+
Redis
+
1 coletor
+
Histórico
+
Deal Score
+
Favoritos
+
Alertas
+
Push
```

## 2.2 Monólito modular inicialmente

Não começar com dezenas de microserviços.

Backend:

```text
1 aplicação Spring Boot
```

Dentro dela:

```text
auth
products
stores
offers
price-history
deals
favorites
alerts
notifications
collectors
```

Separar módulos logicamente.

Microserviços só devem ser considerados quando houver necessidade real.

## 2.3 Qualidade desde o início

Todo recurso deve ter:

- teste unitário;
- teste de integração quando necessário;
- teste de API quando aplicável;
- documentação;
- tratamento de erros;
- logs adequados.

---

# 3. Stack oficial do projeto

## 3.1 Mobile

- React Native
- TypeScript
- React Navigation
- TanStack Query
- Zustand
- Axios
- Firebase Cloud Messaging
- Android Studio
- Gradle

A versão exata das dependências deve ser definida no início da implementação e congelada em `docs/tech-stack.md`.

## 3.2 Backend

- Java LTS compatível com o Spring Boot escolhido
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- Bean Validation
- Flyway
- PostgreSQL
- Redis
- OpenAPI
- Actuator

## 3.3 Testes

- JUnit 5
- Mockito
- AssertJ
- RestAssured
- Testcontainers

## 3.4 Infra

- Docker
- Docker Compose
- GitHub
- GitHub Actions
- ambiente cloud para produção
- HTTPS
- Firebase

---

# 4. Arquitetura

```text
                         ┌────────────────────┐
                         │    Google Play     │
                         └─────────┬──────────┘
                                   │
                                   ▼
                         ┌────────────────────┐
                         │ Android App        │
                         │ React Native       │
                         └─────────┬──────────┘
                                   │ HTTPS
                                   ▼
                         ┌────────────────────┐
                         │ Spring Boot API    │
                         └─────────┬──────────┘
                                   │
             ┌─────────────────────┼─────────────────────┐
             │                     │                     │
             ▼                     ▼                     ▼
       PostgreSQL               Redis               Firebase
             │                                           │
             ▼                                           ▼
      Price History                                   Push
             ▲
             │
       Deal Engine
             ▲
             │
        Collectors
       ┌─────┼─────┐
       ▼     ▼     ▼
     Loja A Loja B Loja C
```

---

# 5. Fluxo principal do produto

```text
Loja
 ↓
Collector
 ↓
Raw Product
 ↓
Normalizer
 ↓
Product Matcher
 ↓
Offer
 ↓
Price History
 ↓
Deal Engine
 ↓
Deal Score
 ↓
Alert Engine
 ↓
Notification
 ↓
Mobile
 ↓
User
 ↓
External Store
```

---

# 6. Fluxo do usuário

```text
Instala aplicativo
        ↓
Abre
        ↓
Home
        ↓
Pesquisa "RX 9070 XT"
        ↓
Seleciona produto
        ↓
Visualiza:
- preço
- histórico
- lojas
- Deal Score
        ↓
Favorita
        ↓
Cria alerta
        ↓
Preço cai
        ↓
Push notification
        ↓
Usuário abre oferta
        ↓
Redirecionamento para loja
```

---

# 7. Escopo do MVP

## 7.1 Categorias

Começar com:

- GPU
- CPU
- RAM
- SSD
- placa-mãe

Depois:

- fonte
- gabinete
- monitor
- cooler
- notebook
- periféricos

## 7.2 Lojas iniciais

Começar com **uma única loja** para validar a arquitetura.

A escolha da primeira loja deve considerar:

- existência de API/feed;
- termos de uso;
- estabilidade;
- disponibilidade dos dados;
- possibilidade de afiliados;
- facilidade de normalização.

Depois adicionar:

1. segunda loja;
2. terceira loja;
3. Amazon;
4. demais fontes compatíveis.

---

# 8. Regras importantes para coleta

A coleta deve respeitar:

- termos de uso;
- robots/políticas aplicáveis;
- limites de requisição;
- APIs oficiais quando disponíveis;
- mecanismos de autenticação;
- CAPTCHAs;
- bloqueios.

**Não implementar mecanismos para burlar CAPTCHA, autenticação, rate limits ou controles de acesso.**

Prioridade:

```text
API oficial
↓
Feed oficial
↓
Integração autorizada
↓
Página pública permitida
```

---

# 9. Estrutura do repositório

```text
hardware-deals/
│
├── mobile/
│
├── backend/
│
├── infrastructure/
│
├── docs/
│   ├── IMPLEMENTATION.md
│   ├── ARCHITECTURE.md
│   ├── DATABASE.md
│   ├── API.md
│   ├── SECURITY.md
│   ├── TESTING.md
│   ├── DEPLOYMENT.md
│   ├── PLAYSTORE.md
│   └── ADR/
│
├── scripts/
│
├── .github/
│   └── workflows/
│
├── .gitignore
├── README.md
└── docker-compose.yml
```

---

# 10. Documentação que a IA deve manter

## README.md

Deve conter:

- objetivo;
- arquitetura;
- requisitos;
- execução local;
- testes;
- links.

## docs/ARCHITECTURE.md

Explicar:

- componentes;
- dependências;
- fluxos;
- decisões.

## docs/DATABASE.md

Explicar:

- entidades;
- relacionamentos;
- índices;
- migrations.

## docs/API.md

Documentar endpoints.

## docs/SECURITY.md

Documentar:

- autenticação;
- autorização;
- secrets;
- rate limiting;
- logs.

## docs/TESTING.md

Documentar:

- testes unitários;
- integração;
- API;
- E2E;
- performance.

## docs/PLAYSTORE.md

Documentar todo o processo de publicação.

---

# 11. Banco de dados

## 11.1 users

```text
id UUID PK
name VARCHAR
email VARCHAR UNIQUE
password_hash VARCHAR
status VARCHAR
created_at TIMESTAMP
updated_at TIMESTAMP
```

## 11.2 stores

```text
id UUID PK
name VARCHAR
slug VARCHAR UNIQUE
website VARCHAR
active BOOLEAN
created_at TIMESTAMP
updated_at TIMESTAMP
```

## 11.3 products

```text
id UUID PK
name VARCHAR
brand VARCHAR
model VARCHAR
category VARCHAR
ean VARCHAR
normalized_name VARCHAR
image_url VARCHAR
active BOOLEAN
created_at TIMESTAMP
updated_at TIMESTAMP
```

## 11.4 store_products

Representa o produto conforme aparece em uma loja.

```text
id UUID PK
store_id UUID FK
product_id UUID FK
external_id VARCHAR
sku VARCHAR
external_name VARCHAR
url VARCHAR
active BOOLEAN
```

## 11.5 offers

```text
id UUID PK
store_product_id UUID FK
price DECIMAL
original_price DECIMAL
coupon VARCHAR
available BOOLEAN
collected_at TIMESTAMP
```

## 11.6 price_history

```text
id UUID PK
product_id UUID FK
store_id UUID FK
price DECIMAL
collected_at TIMESTAMP
```

## 11.7 favorites

```text
id UUID PK
user_id UUID FK
product_id UUID FK
created_at TIMESTAMP
```

## 11.8 price_alerts

```text
id UUID PK
user_id UUID FK
product_id UUID FK
target_price DECIMAL
active BOOLEAN
last_notified_at TIMESTAMP
created_at TIMESTAMP
updated_at TIMESTAMP
```

## 11.9 device_tokens

```text
id UUID PK
user_id UUID FK
token VARCHAR
platform VARCHAR
active BOOLEAN
created_at TIMESTAMP
updated_at TIMESTAMP
```

## 11.10 notifications

```text
id UUID PK
user_id UUID FK
type VARCHAR
title VARCHAR
message VARCHAR
read BOOLEAN
created_at TIMESTAMP
```

---

# 12. Regras de banco

- UUID como identificador público;
- índices em campos de busca;
- foreign keys;
- unique constraints;
- timestamps;
- migrations versionadas;
- nenhuma alteração manual em produção.

Todas as alterações devem passar por Flyway.

---

# 13. Fases de implementação

---

# FASE 00 — Preparação

## Objetivo

Preparar máquina, ferramentas e contas.

## Instalar

- Git
- JDK
- Node.js
- npm/pnpm
- Android Studio
- Android SDK
- Docker
- Docker Compose
- IDE
- Firebase CLI quando necessário

## Verificar

```bash
git --version
java -version
node --version
npm --version
docker --version
docker compose version
```

## Critério de aceite

Todos os comandos devem funcionar.

---

# FASE 01 — Criar repositório

## Objetivo

Criar estrutura inicial.

## Tarefas

- [ ] Criar GitHub repository
- [ ] Criar README
- [ ] Criar `.gitignore`
- [ ] Criar `docs`
- [ ] Criar `backend`
- [ ] Criar `mobile`
- [ ] Criar `infrastructure`
- [ ] Criar GitHub Actions inicial

## Commit

```text
chore: initialize project
```

## Checkpoint

O repositório deve clonar e abrir sem erros.

---

# FASE 02 — Backend inicial

## Objetivo

Criar Spring Boot funcionando.

## Tarefas

- [ ] Criar projeto
- [ ] Configurar profiles
- [ ] Configurar Actuator
- [ ] Configurar OpenAPI
- [ ] Configurar tratamento global de erros
- [ ] Criar `/health`

## Endpoint

```http
GET /api/v1/health
```

Resposta:

```json
{
  "status": "UP"
}
```

## Testes

- [ ] Controller test
- [ ] Context test

---

# FASE 03 — PostgreSQL

## Objetivo

Conectar banco.

## Tarefas

- [ ] Docker PostgreSQL
- [ ] Configuração local
- [ ] Flyway
- [ ] Primeira migration
- [ ] Connection pool
- [ ] Health check

## Checkpoint

Aplicação sobe com banco vazio.

---

# FASE 04 — Modelo de domínio

Implementar:

- User
- Store
- Product
- StoreProduct
- Offer
- PriceHistory
- Favorite
- PriceAlert
- DeviceToken
- Notification

## Regras

Criar:

- entities;
- repositories;
- DTOs;
- services;
- migrations.

## Testes

Cada repository importante deve ter teste de integração.

---

# FASE 05 — Autenticação

## Implementar

```text
register
login
refresh
logout
forgot password
reset password
```

## Segurança

- senha com algoritmo de hash seguro;
- tokens;
- expiração;
- validação;
- rate limiting.

## Critérios

Usuário consegue:

```text
Cadastrar
↓
Login
↓
Receber token
↓
Consultar endpoint protegido
```

---

# FASE 06 — API de produtos

## Endpoints

```http
GET /products
GET /products/{id}
GET /products/search?q=
```

## Filtros

```text
category
brand
minPrice
maxPrice
store
sort
page
size
```

Implementar paginação.

---

# FASE 07 — Lojas

## Endpoints

```http
GET /stores
GET /stores/{id}
```

Admin posteriormente poderá cadastrar/desativar lojas.

---

# FASE 08 — Primeiro Collector

## Objetivo

Conectar uma única fonte.

## Interface

```java
public interface PriceCollector {
    List<CollectedOffer> collect();
}
```

## Pipeline

```text
Collector
 ↓
Validation
 ↓
Normalization
 ↓
Persistence
```

## Critérios

Uma execução deve:

- buscar produtos;
- identificar produto;
- salvar oferta;
- salvar histórico;
- registrar erro sem derrubar o job inteiro.

---

# FASE 09 — Normalização

## Objetivo

Resolver nomes diferentes.

Exemplo:

```text
ASRock Radeon RX 9070 XT Challenger 16GB
ASRock RX 9070 XT Challenger 16GB
RX9070XT Challenger
```

Podem representar o mesmo produto.

Mas:

```text
RX 9070
```

não pode ser automaticamente considerado:

```text
RX 9070 XT
```

## Regras

Normalizar:

- fabricante;
- modelo;
- capacidade;
- memória;
- SKU;
- EAN.

## Testes obrigatórios

Cobrir:

- espaços;
- maiúsculas/minúsculas;
- caracteres especiais;
- versões;
- XT;
- SUPER;
- Ti;
- capacidade;
- variantes.

---

# FASE 10 — Histórico

Toda coleta válida deve gerar histórico.

Calcular:

```text
currentPrice
lowestPrice
highestPrice
averagePrice
medianPrice
priceVariation
```

## Endpoint

```http
GET /products/{id}/price-history
```

---

# FASE 11 — Deal Engine

## Objetivo

Determinar se a oferta é boa.

Criar:

```text
DealEvaluationService
```

Entrada:

```text
currentPrice
averagePrice
lowestPrice
highestPrice
availability
coupon
```

Saída:

```text
score
classification
```

## Classificação

```text
90-100 = EXCELENTE
80-89  = ÓTIMA
70-79  = BOA
60-69  = INTERESSANTE
0-59   = NORMAL
```

A fórmula inicial deve ser simples, determinística e documentada.

Não utilizar IA para isso no MVP.

---

# FASE 12 — API de ofertas

## Endpoints

```http
GET /deals
GET /deals/{id}
GET /products/{id}/offers
```

Ordenação:

```text
score
price
discount
recent
```

---

# FASE 13 — Mobile inicial

## Criar

- projeto React Native;
- TypeScript;
- navegação;
- tema;
- componentes base;
- cliente HTTP;
- gerenciamento de estado.

## Estrutura

```text
mobile/src/

components/
screens/
navigation/
services/
hooks/
store/
types/
utils/
theme/
```

---

# FASE 14 — Tela Home

## Mostrar

```text
Melhores ofertas
Categorias
Ofertas recentes
```

## Card

```text
imagem
nome
preço
preço anterior
score
loja
```

---

# FASE 15 — Busca

Implementar:

```text
campo de pesquisa
debounce
resultados
loading
empty state
error state
```

---

# FASE 16 — Detalhes

Mostrar:

```text
produto
imagem
preço atual
menor preço
média
score
lojas
histórico
favoritar
criar alerta
```

---

# FASE 17 — Histórico visual

Criar gráfico.

Requisitos:

- período;
- preço mínimo;
- preço máximo;
- preço atual;
- loading;
- sem dados;
- erro.

---

# FASE 18 — Favoritos

Implementar:

```text
adicionar
remover
listar
```

Sincronizar com backend.

---

# FASE 19 — Alertas

Usuário escolhe:

```text
Produto
Preço desejado
```

Exemplo:

```text
RX 9070 XT
≤ R$ 3.700
```

---

# FASE 20 — Firebase Push

## Implementar

```text
mobile
 ↓
FCM token
 ↓
backend
 ↓
device_tokens
```

## Evento

```text
price <= target_price
```

Disparar push.

---

# FASE 21 — Alert Engine

Job:

```text
buscar alertas ativos
 ↓
buscar preço atual
 ↓
comparar
 ↓
se atingiu limite:
    notificar
```

Evitar spam.

Usar:

```text
last_notified_at
```

---

# FASE 22 — Redirecionamento

Ao tocar:

```text
Ver oferta
```

Registrar clique:

```text
user
product
store
offer
timestamp
```

Depois abrir URL externa.

---

# FASE 23 — Analytics

Medir:

```text
app_open
search
product_view
favorite
alert_created
notification_open
offer_click
```

Não coletar informações desnecessárias.

---

# FASE 24 — Administração

Criar posteriormente.

Funcionalidades:

```text
dashboard
products
stores
offers
reports
collectors
users
```

---

# FASE 25 — Testes completos

## Unitários

Cobrir:

- normalização;
- Deal Score;
- alertas;
- services;
- validações.

## Integração

Cobrir:

- PostgreSQL;
- Redis;
- repositories;
- jobs.

## API

RestAssured:

```text
register
login
products
deals
favorites
alerts
```

## Mobile

Testar:

```text
login
home
search
details
favorite
alert
notification
external link
```

---

# FASE 26 — Testes negativos

Testar:

```text
email inválido
senha inválida
token expirado
produto inexistente
alerta inexistente
preço inválido
usuário sem autorização
```

---

# FASE 27 — Segurança

Checklist:

- [ ] HTTPS
- [ ] Hash de senha
- [ ] JWT seguro
- [ ] Rate limiting
- [ ] CORS
- [ ] Input validation
- [ ] SQL Injection
- [ ] XSS
- [ ] Authorization
- [ ] Secrets
- [ ] Logs sem dados sensíveis
- [ ] Dependências atualizadas

---

# FASE 28 — Performance

Medir:

```text
API latency
DB latency
collector duration
push latency
```

Testar inicialmente:

```text
100 usuários
1.000 usuários
10.000 usuários
```

Escalar apenas quando os dados justificarem.

---

# FASE 29 — CI

Pipeline:

```text
git push
 ↓
lint
 ↓
compile
 ↓
unit tests
 ↓
integration tests
 ↓
security scan
 ↓
build
```

Falhou:

```text
não fazer deploy.
```

---

# FASE 30 — Staging

Criar ambiente:

```text
staging
```

Variáveis independentes.

Nunca apontar staging para banco de produção.

---

# FASE 31 — Produção

Criar:

```text
API
PostgreSQL
Redis
HTTPS
DNS
backup
monitoramento
```

## Backup

Definir:

- frequência;
- retenção;
- restauração;
- teste de restore.

---

# FASE 32 — Domínio

Registrar domínio.

Exemplo:

```text
hardwaredeals.com.br
```

Verificar disponibilidade antes.

Criar:

```text
www
api
privacy
terms
support
```

---

# FASE 33 — Site público

Criar página:

```text
/
```

Seções:

- produto;
- funcionalidades;
- contato.

Também criar:

```text
/privacy
/terms
/support
/delete-account
```

---

# FASE 34 — LGPD

Documentar:

- dados coletados;
- finalidade;
- armazenamento;
- retenção;
- compartilhamento;
- exclusão;
- contato.

Implementar exclusão de conta.

---

# FASE 35 — Política de privacidade

Deve refletir exatamente o comportamento real do app.

Não declarar:

```text
"não coletamos dados"
```

se o aplicativo coleta analytics, email, identificadores ou tokens de push.

---

# FASE 36 — Google Play Console

Criar conta de desenvolvedor.

Escolher:

```text
Personal
```

ou

```text
Organization
```

conforme a estrutura real do projeto.

A documentação oficial informa que contas pessoais novas possuem requisitos específicos de teste antes da distribuição em produção. Para contas pessoais criadas após 13/11/2023, a regra atual exige teste fechado com pelo menos **12 testers inscritos continuamente por 14 dias** antes de solicitar acesso à produção. citeturn0search3turn0search32

**Planejamento:** considerar esses 14 dias no cronograma.

---

# FASE 37 — Android Release

Configurar:

```text
applicationId
versionName
versionCode
```

Gerar:

```text
AAB
```

Usar Play App Signing.

O Google recomenda/configura o processo de assinatura do app pelo Play Console; proteger a chave de upload e não armazená-la no repositório. citeturn0search24

---

# FASE 38 — Target API

Como o projeto será iniciado em agosto de 2026, configurar o Android visando **Android 16 / API 36**.

A documentação oficial do Google informa que, a partir de **31/08/2026**, novos apps e atualizações precisam targetear Android 16 (API 36) ou superior para submissão ao Google Play. citeturn0search8turn0search27

Não criar o projeto visando API 35 apenas para depois precisar migrar antes do lançamento.

---

# FASE 39 — Store Listing

Preparar:

- nome;
- descrição curta;
- descrição completa;
- ícone;
- screenshots;
- categoria;
- classificação;
- contato;
- política de privacidade.

---

# FASE 40 — Data Safety

Mapear exatamente:

```text
nome
email
analytics
identificadores
tokens
dados de uso
```

A declaração deve corresponder ao comportamento real.

---

# FASE 41 — Permissões

Solicitar somente permissões necessárias.

O Google Play exige justificativa para permissões e APIs que acessam informações sensíveis. citeturn0search28

Para esse aplicativo, evitar permissões desnecessárias como:

```text
contatos
SMS
localização
microfone
câmera
```

se não houver funcionalidade que realmente precise delas.

---

# FASE 42 — Screenshots

Preparar:

```text
01 Home
02 Busca
03 Produto
04 Histórico
05 Comparação
06 Alertas
```

Utilizar telas reais.

---

# FASE 43 — Teste interno

Publicar primeiro em:

```text
Internal testing
```

Validar:

- instalação;
- login;
- API;
- notificações;
- links;
- crashes.

---

# FASE 44 — Teste fechado

Se a conta pessoal estiver sujeita ao requisito:

```text
12 testers
14 dias contínuos
```

Planejar:

```text
Dia 1 → testers entram
Dia 7 → monitoramento
Dia 14 → requisito cumprido
```

Os testers devem realmente testar o aplicativo.

Não tratar o requisito apenas como burocracia.

---

# FASE 45 — Produção

Após cumprir os requisitos:

```text
Production access
 ↓
Production release
 ↓
Review
 ↓
Published
```

---

# FASE 46 — Pós-lançamento

Monitorar:

```text
crashes
ANR
reviews
downloads
DAU
MAU
retention
offer_click
alert_created
notification_open
```

---

# FASE 47 — Operação dos collectors

Criar dashboard:

```text
Collector
Status
Última execução
Produtos encontrados
Ofertas encontradas
Erros
Duração
```

Se um collector falhar:

```text
não derrubar a API.
```

---

# FASE 48 — Qualidade das promoções

Estados:

```text
VALID
SUSPICIOUS
EXPIRED
OUT_OF_STOCK
```

Uma promoção inválida deve desaparecer ou ser marcada adequadamente.

---

# FASE 49 — Reports do usuário

Permitir:

```text
Reportar oferta
```

Motivos:

```text
Preço incorreto
Produto incorreto
Link quebrado
Esgotado
Informação desatualizada
```

---

# FASE 50 — Monetização futura

## Afiliados

Fluxo:

```text
Usuário
 ↓
Oferta
 ↓
Link rastreado
 ↓
Loja
 ↓
Compra
 ↓
Comissão
```

Implementar somente utilizando programas e integrações permitidos.

## Premium

Futuro:

```text
R$ 9,90/mês
```

Possibilidades:

- alertas ilimitados;
- filtros;
- histórico avançado;
- sem anúncios;
- alertas prioritários.

Se o app vender recursos digitais dentro da Play Store, revisar as políticas atuais de pagamentos do Google Play antes da implementação.

---

# 51. Fase 51 — IA no produto

Não usar IA para substituir regras determinísticas no MVP.

Depois:

```text
Usuário:
"Quero uma GPU até R$ 4.000"

IA:
- interpreta intenção
- consulta produtos
- consulta histórico
- compara
- explica opções
```

Outro caso:

```text
"Monte um PC de R$ 5.000"
```

O sistema pode considerar:

- orçamento;
- compatibilidade;
- promoções;
- performance;
- histórico.

---

# 52. Fase 52 — Discord e Telegram

Depois do app validado:

```text
Backend
 ├── Android
 ├── Discord
 └── Telegram
```

Os bots consomem a mesma API.

Não criar lógica duplicada.

---

# 53. Fase 53 — Segunda loja

Após a primeira integração estar estável:

```text
Collector A
Collector B
```

Todos implementam a mesma interface.

---

# 54. Fase 54 — Comparação

Mostrar:

```text
RX 9070 XT

Loja A  R$ 3.699
Loja B  R$ 3.749
Loja C  R$ 3.799
```

---

# 55. Fase 55 — Histórico avançado

Períodos:

```text
7 dias
30 dias
90 dias
180 dias
1 ano
```

---

# 56. Fase 56 — Ranking

Criar:

```text
Melhores GPUs
Melhores CPUs
Melhores SSDs
```

Ordenado por:

```text
Deal Score
```

---

# 57. Fase 57 — Compatibilidade de PC

Futuramente:

```text
CPU
GPU
Motherboard
RAM
PSU
Case
Cooler
```

Validar:

```text
socket
chipset
RAM
dimensões
consumo
conectores
```

---

# 58. Estratégia de desenvolvimento com IA

## Regra

A IA nunca deve assumir o estado do projeto.

Sempre começar pedindo:

```text
"Analise o repositório atual antes de modificar qualquer arquivo."
```

## Depois

```text
"Leia docs/IMPLEMENTATION.md."
```

## Em seguida

```text
"Estamos na Fase X."
```

## Finalmente

```text
"Implemente apenas essa fase."
```

---

# 59. Prompt mestre para a IA

Copiar este prompt para o agente de programação:

```text
Você é o principal engenheiro de software deste projeto.

Leia primeiro:
- docs/IMPLEMENTATION.md
- README.md
- docs/ARCHITECTURE.md
- docs/DATABASE.md
- docs/API.md

Antes de modificar qualquer arquivo:
1. analise a estrutura atual;
2. identifique tecnologias e versões;
3. identifique o que já está implementado;
4. identifique testes existentes;
5. identifique problemas.

Regras:
- não reescreva o projeto sem necessidade;
- não remova funcionalidades existentes sem justificativa;
- não invente APIs externas;
- não invente dados de lojas;
- não coloque secrets no código;
- não implemente scraping que burle CAPTCHA, autenticação, rate limits ou controles de acesso;
- prefira APIs oficiais e integrações permitidas;
- mantenha arquitetura simples;
- escreva testes;
- execute os testes após as alterações;
- corrija falhas;
- atualize documentação;
- não avance para outra fase sem autorização.

Para a fase solicitada:
1. descreva o plano;
2. implemente;
3. execute testes;
4. mostre arquivos alterados;
5. mostre comandos executados;
6. mostre resultados;
7. informe pendências;
8. pare.

Não diga apenas que algo foi implementado.
Demonstre por testes e execução.
```

---

# 60. Prompt para cada fase

```text
Leia docs/IMPLEMENTATION.md.

Estamos na FASE [NÚMERO] — [NOME].

Analise primeiro o estado atual do projeto.

Implemente somente os requisitos dessa fase.

Não avance para fases futuras.

Critérios de aceite:
[COLE OS CRITÉRIOS DA FASE]

Obrigatório:
- criar/alterar testes;
- executar testes;
- corrigir falhas;
- atualizar documentação;
- não criar código morto;
- não deixar TODOs para requisitos obrigatórios.

No final responda:
1. O que foi implementado?
2. Quais arquivos foram criados?
3. Quais arquivos foram alterados?
4. Quais testes foram executados?
5. Resultado dos testes?
6. Há pendências?
7. Qual é o próximo checkpoint?

Pare após isso.
```

---

# 61. Prompt de auditoria

Depois de cada 3-5 fases:

```text
Faça uma auditoria técnica do projeto.

Leia:
- docs/IMPLEMENTATION.md
- README.md
- documentação
- código
- testes

Verifique:
- arquitetura;
- duplicação;
- segurança;
- cobertura;
- qualidade;
- performance;
- dependências;
- tratamento de erros;
- logs;
- banco;
- API;
- mobile.

Não implemente alterações ainda.

Gere:
CRÍTICO
ALTO
MÉDIO
BAIXO

Para cada problema:
- localização;
- impacto;
- causa;
- recomendação.
```

---

# 62. Prompt de correção

```text
Corrija somente os problemas classificados como CRÍTICO e ALTO no relatório anterior.

Antes:
- reproduza o problema;
- crie ou ajuste teste.

Depois:
- implemente correção;
- execute testes;
- verifique regressão.

Não altere comportamento não relacionado.
```

---

# 63. Prompt de testes

```text
Analise a funcionalidade implementada.

Crie uma estratégia de testes cobrindo:
- happy path;
- edge cases;
- validações;
- erros;
- autorização;
- concorrência quando aplicável;
- regressão.

Implemente os testes.

Execute todos os testes relevantes.

Não altere produção somente para fazer o teste passar.
```

---

# 64. Prompt de release

```text
Prepare o projeto para release.

Verifique:
- versão;
- changelog;
- testes;
- build;
- secrets;
- configuração;
- API;
- banco;
- Android;
- Firebase;
- permissões;
- política de privacidade;
- Data Safety;
- Play Store.

Não publique.

Gere apenas um relatório de readiness.
```

---

# 65. Prompt de publicação

```text
Leia docs/PLAYSTORE.md.

Estamos no processo de publicação.

Verifique o estado atual do projeto e produza uma checklist objetiva de tudo que ainda falta para enviar o AAB ao Google Play Console.

Não faça alterações irreversíveis.
Não publique.
Não invente informações.
```

---

# 66. Definition of Done

Uma tarefa somente está pronta quando:

```text
[ ] código implementado
[ ] teste criado
[ ] teste executado
[ ] teste passou
[ ] erro tratado
[ ] logs adequados
[ ] documentação atualizada
[ ] sem secrets
[ ] sem TODO obrigatório
[ ] sem regressão
```

---

# 67. Definition of Done do MVP

```text
[ ] Usuário consegue cadastrar
[ ] Usuário consegue login
[ ] Home funciona
[ ] Busca funciona
[ ] Produto funciona
[ ] Histórico funciona
[ ] Ofertas funcionam
[ ] Deal Score funciona
[ ] Favoritos funcionam
[ ] Alertas funcionam
[ ] Push funciona
[ ] Link externo funciona
[ ] Collector funciona
[ ] Banco funciona
[ ] Backup funciona
[ ] HTTPS funciona
[ ] Testes passam
[ ] Política de privacidade publicada
[ ] Termos publicados
[ ] Data Safety preenchido
[ ] AAB gerado
[ ] Teste interno realizado
[ ] Teste fechado realizado quando exigido
[ ] App enviado para revisão
```

---

# 68. Checklist final de Play Store

## Conta

- [ ] Conta criada
- [ ] Identidade verificada
- [ ] Dados do desenvolvedor
- [ ] Requisitos de conta atendidos

## Aplicativo

- [ ] Nome
- [ ] Package ID
- [ ] Ícone
- [ ] Descrição
- [ ] Screenshots
- [ ] Categoria
- [ ] Classificação
- [ ] Política de privacidade

## Android

- [ ] Release build
- [ ] AAB
- [ ] Assinatura
- [ ] Play App Signing
- [ ] Target API adequado
- [ ] Sem secrets

## Compliance

- [ ] Data Safety
- [ ] Permissões
- [ ] Conteúdo
- [ ] Privacidade
- [ ] Exclusão de conta
- [ ] Termos

## Testes

- [ ] Internal testing
- [ ] Closed testing quando exigido
- [ ] 12 testers / 14 dias quando aplicável
- [ ] Bugs críticos corrigidos
- [ ] Crashes verificados

## Publicação

- [ ] Release criado
- [ ] Países definidos
- [ ] Preço definido
- [ ] Review enviado
- [ ] Produção aprovada
- [ ] App disponível

---

# 69. Pós-publicação

Primeiras 24 horas:

```text
monitorar crashes
monitorar API
monitorar collectors
monitorar push
monitorar reviews
```

Primeiros 7 dias:

```text
analisar retenção
analisar buscas
analisar cliques
analisar alertas
analisar erros
```

Primeiros 30 dias:

```text
decidir quais funcionalidades realmente devem ser priorizadas.
```

---

# 70. Roadmap pós-MVP

```text
MVP
 ↓
2ª loja
 ↓
Comparação
 ↓
Mais categorias
 ↓
Afiliados
 ↓
Discord
 ↓
Telegram
 ↓
Premium
 ↓
IA
 ↓
Montador de PC
 ↓
Compatibilidade
 ↓
Previsão de preço
```

---

# 71. Ordem recomendada para trabalhar com IA

Não começar pela interface.

Sequência:

```text
01 Preparação
02 Git
03 Backend
04 Banco
05 Domínio
06 Auth
07 API
08 Collector
09 Normalização
10 Histórico
11 Deal Engine
12 Mobile
13 Favoritos
14 Alertas
15 Push
16 Analytics
17 Testes
18 Segurança
19 Staging
20 Produção
21 Play Store
```

---

# 72. Primeiro objetivo real

Não é:

> “Construir o aplicativo completo.”

É:

> **“Conseguir coletar uma oferta real, salvar seu histórico, calcular se é uma boa oferta, exibir no Android e permitir que o usuário crie um alerta.”**

Quando esse fluxo funcionar ponta a ponta, o produto já terá o núcleo necessário para crescer.

---

# 73. Primeiro milestone

## M1 — Oferta real funcionando

```text
Loja
 ↓
Collector
 ↓
PostgreSQL
 ↓
Histórico
 ↓
Deal Score
 ↓
API
 ↓
Android
 ↓
Usuário
```

Critério:

> Um usuário consegue abrir o aplicativo e visualizar uma oferta real cuja informação foi coletada automaticamente pelo backend.

---

# 74. Segundo milestone

## M2 — Alertas

```text
Usuário
 ↓
Cria alerta
 ↓
Preço cai
 ↓
Backend detecta
 ↓
Firebase
 ↓
Push
```

---

# 75. Terceiro milestone

## M3 — Comparador

```text
Produto
 ↓
Loja A
Loja B
Loja C
 ↓
Melhor preço
```

---

# 76. Quarto milestone

## M4 — Publicação

```text
MVP
 ↓
QA
 ↓
Staging
 ↓
Internal test
 ↓
Closed test
 ↓
Production access
 ↓
Review
 ↓
Google Play
```

---

# 77. Observação sobre requisitos do Google Play

Os requisitos do Google Play são mutáveis.

Antes de cada release, consultar as páginas oficiais do Google Play Console.

Em agosto de 2026, a documentação oficial indica:

- novos apps e atualizações precisam targetear Android 16 / API 36 a partir de 31/08/2026; citeturn0search8turn0search27
- contas pessoais novas possuem requisitos específicos de testes; a documentação informa atualmente 12 testers durante 14 dias para o acesso à produção. citeturn0search3turn0search32
- o processo de criação/configuração do app deve atender os requisitos de target API e demais requisitos exibidos pelo Play Console. citeturn0search11

Nunca congelar esses requisitos no código sem verificar a documentação oficial no momento do lançamento.

---

# 78. Resultado esperado

Ao concluir todas as fases, teremos:

```text
                    HARDWARE DEALS

                       ┌───────┐
                       │ USER  │
                       └───┬───┘
                           │
                           ▼
                    ┌─────────────┐
                    │ Android App │
                    └──────┬──────┘
                           │
                           ▼
                    ┌─────────────┐
                    │ Spring API  │
                    └──────┬──────┘
                           │
           ┌───────────────┼────────────────┐
           ▼               ▼                ▼
      PostgreSQL         Redis           Firebase
           │
           ▼
     Price History
           │
           ▼
      Deal Engine
           │
           ▼
       Collectors
           │
       ┌───┼───┐
       ▼   ▼   ▼
     Loja Loja Loja
```

O produto estará preparado para:

- crescer de uma para várias lojas;
- aumentar o número de categorias;
- adicionar Discord;
- adicionar Telegram;
- adicionar afiliados;
- adicionar Premium;
- adicionar IA;
- criar uma plataforma de comparação de preços especializada em hardware.

---

# 79. Regra final para a IA

**Nunca pule checkpoints.**

**Nunca considere código “pronto” sem executar testes.**

**Nunca invente integração de loja.**

**Nunca coloque credenciais no código.**

**Nunca implemente todas as fases em uma única execução.**

**Sempre leia o estado atual do projeto antes de modificar arquivos.**

**Sempre deixe o projeto em estado executável após cada fase.**

**Sempre priorize o menor incremento funcional possível.**

---

# 80. Próximo comando para iniciar o projeto

Depois de criar este arquivo no repositório:

```text
Leia docs/IMPLEMENTATION.md.

Estamos começando pela FASE 00.

Não implemente funcionalidades do produto ainda.

Analise minha máquina/projeto atual, verifique os pré-requisitos necessários para desenvolver o Hardware Deals e me diga exatamente o que está instalado, o que está faltando e quais versões você recomenda.

Não avance para a FASE 01 até que todos os pré-requisitos estejam definidos.
```

Depois de concluir a FASE 00:

```text
Leia docs/IMPLEMENTATION.md.

Estamos na FASE 01.

Analise o estado atual do repositório.

Implemente somente a FASE 01.

Execute os testes/checks disponíveis.

Não avance para a FASE 02.
```

Esse padrão deve ser repetido até a publicação.
