# 🌵 Raízes do Nordeste — API Back-end

API REST desenvolvida como Projeto Multidisciplinar do curso de
Análise e Desenvolvimento de Sistemas da UNINTER.

O sistema gerencia toda a operação da rede de lanchonetes
"Raízes do Nordeste": pedidos, estoque, pagamentos,
fidelização e muito mais.

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Como Executar](#-como-executar)
- [Acessando o Sistema](#-acessando-o-sistema)
- [Primeiros Passos na API](#-primeiros-passos-na-api)
- [Endpoints](#-endpoints)
- [Perfis de Usuário](#-perfis-de-usuário)
- [Fluxo de Pedidos](#-fluxo-de-pedidos)
- [Testes](#-testes)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Dados Iniciais](#-dados-iniciais)
- [Monitoramento e Auditoria](#-monitoramento-e-auditoria)
- [Problemas Comuns](#️-problemas-comuns)

---

## 📖 Sobre o Projeto

Esta API foi desenvolvida para gerenciar toda a operação
digital da rede, desde o momento em que o cliente faz um
pedido até a confirmação do pagamento e entrega do produto.

Um dos principais diferenciais do sistema é o suporte à
**multicanalidade**: independentemente de onde o cliente
fizer o pedido, o sistema registra, processa e rastreia
tudo da mesma forma. Os canais suportados são:

| Canal | Onde é usado | Descrição |
|---|---|---|
| `APP` | Aplicativo móvel | Cliente faz o pedido pelo app antes de chegar à loja |
| `TOTEM` | Dentro da loja | Cliente faz o pedido no totem de autoatendimento |
| `BALCAO` | Dentro da loja | Atendente registra o pedido no balcão |
| `PICKUP` | App ou Web | Cliente faz o pedido online e retira na loja |
| `WEB` | Navegador | Cliente faz o pedido pelo site da rede |

 💡 Cada pedido registra obrigatoriamente seu canal de
origem. Isso permite que a matriz da rede acompanhe
quais canais geram mais vendas em cada unidade.

---

## 🛠 Tecnologias

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Security + JWT**
- **Spring Data JPA + Hibernate**
- **PostgreSQL 16**
- **Flyway** (controle de versão do banco)
- **Swagger/OpenAPI** (documentação interativa)
- **Maven** (gerenciamento de dependências)
- **IntelliJ** (Ambiente de desenvolvimento)

---


## ✅ Pré-requisitos

Antes de começar, você vai precisar ter instalado:

### 1. Java 21
- Baixe em: https://www.oracle.com/br/java/technologies/downloads/#java21 ou https://adoptium.net
- Após instalar, verifique no terminal:

**java -version**
# deve aparecer: openjdk version "21..."


### 2. PostgreSQL 16
- Baixe em: https://www.postgresql.org/download
- Durante a instalação, defina uma senha para o usuário `postgres`
- Anote essa senha — você vai precisar dela!
- Após instalar, o PostgreSQL já inicia automaticamente

### 3. Git
- Baixe em: https://git-scm.com/downloads
- Após instalar, verifique no terminal:

**git --version**


> 💡 **Não precisa instalar o Maven!** O projeto já inclui
> o `mvnw` (Maven Wrapper) que baixa tudo automaticamente.

---

## 🚀 Como Executar

### Passo 1 — Clonar o repositório

Abra o terminal (Prompt de Comando ou PowerShell no Windows)
e execute:


**git clone https://github.com/thiago06090/Raizes-nordeste.git**

**cd Raizes-nordeste**


---

### Passo 2 — Criar o banco de dados

Abra o **pgAdmin 4** (instalado junto com o PostgreSQL) e:

1. Clique com botão direito em **Databases**
2. Clique em **Create → Database**
3. Nome: `raizes_db`
4. Clique em **Save**

### Passo 3 — Configurar a senha do banco

Abra o arquivo `src/main/resources/application.properties`
e edite a linha da senha:


**spring.datasource.password=SUA_SENHA_AQUI**


Substitua `SUA_SENHA_AQUI` pela senha que você definiu
durante a instalação do PostgreSQL.

---

### Passo 4 — Executar a aplicação

No terminal, dentro da pasta do projeto, execute:

**Windows:**

**mvnw.cmd spring-boot:run**


**Mac/Linux:**

**./mvnw spring-boot:run**


> ⏳ Na primeira execução, o Maven vai baixar todas as
> dependências. Isso pode levar alguns minutos dependendo
> da sua conexão com a internet.

---

### Passo 5 — Verificar se subiu corretamente

Se tudo deu certo, você verá no terminal:
Started RaizesBackendApplication in X seconds

## 🌐 Acessando o Sistema

Com a aplicação rodando, acesse pelo navegador:

| O que | Endereço |
|---|---|
| **Swagger (documentação)** | http://localhost:8080/swagger-ui/index.html |
| **API (base)** | http://localhost:8080 |

---

## 🔑 Primeiros Passos na API

### 1. Criar um usuário

Acesse o Swagger e clique em `POST /api/auth/registrar`:

```json
{
  "nome": "Seu Nome",
  "email": "seu@email.com",
  "senha": "Senha@123",
  "consentimentoLgpd": true,
  "perfil": "CLIENTE"
}
```

> 💡 O campo `perfil` é opcional. Quando não informado,
> o padrão é `CLIENTE`. Veja a seção
> [Perfis de Usuário](#-perfis-de-usuário) para mais detalhes.

---

### 2. Fazer login

Clique em `POST /api/auth/login`:

```json
{
  "email": "seu@email.com",
  "senha": "Senha@123"
}
```

Você receberá um `accessToken` — copie esse valor!

---

### 3. Autorizar no Swagger

1. Clique no botão **Authorize 🔒** no topo da página
2. Digite: `SEU_TOKEN_AQUI`
3. Clique em **Authorize** e depois **Close**

Agora você pode usar todos os endpoints protegidos! ✅

---

### 4. Consultar o cardápio

Antes de fazer um pedido, consulte os produtos disponíveis
na unidade desejada:
GET /api/cardapio/unidade/1

Retorna os produtos com preço e quantidade em estoque.

---

### 5. Criar um pedido

Clique em `POST /api/pedidos`:

```json
{
  "unidadeId": 1,
  "canalPedido": "APP",
  "formaPagamento": "PIX",
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2
    }
  ]
}
```

> 💡 **Dica:** Use `"formaPagamento": "RECUSAR"` para
> simular um pagamento recusado e testar esse cenário!

---

## 📡 Endpoints

### 🔐 Autenticação (`/api/auth`)
| Método | Rota | Descrição |
|---|---|---|
| POST | /api/auth/registrar | Cadastrar novo usuário |
| POST | /api/auth/login | Login e geração do token |

### 🍽 Cardápio (`/api/cardapio`)
| Método | Rota | Descrição |
|---|---|---|
| GET | /api/cardapio/unidade/{id} | Consultar cardápio da unidade |

### 🛒 Pedidos (`/api/pedidos`)
| Método | Rota | Descrição |
|---|---|---|
| POST | /api/pedidos | Criar novo pedido |
| GET | /api/pedidos | Listar pedidos (filtro por canal e status) |
| GET | /api/pedidos/{id} | Buscar pedido por ID |
| PATCH | /api/pedidos/{id}/status | Atualizar status do pedido |


**Exemplos de filtro:**
GET /api/pedidos?canalPedido=APP
GET /api/pedidos?canalPedido=TOTEM&status=PAGO

### 🍽 Produtos (`/api/produtos`)
| Método | Rota | Descrição |
|---|---|---|
| GET | /api/produtos | Listar produtos disponíveis |
| GET | /api/produtos/{id} | Buscar produto por ID |
| POST | /api/produtos | Criar produto |
| PUT | /api/produtos/{id} | Atualizar produto |
| DELETE | /api/produtos/{id} | Desativar produto |

### 🏪 Unidades (`/api/unidades`)
| Método | Rota | Descrição |
|---|---|---|
| GET | /api/unidades | Listar unidades ativas |
| GET | /api/unidades/{id} | Buscar unidade por ID |
| POST | /api/unidades | Criar unidade |
| PUT | /api/unidades/{id} | Atualizar unidade |

### 📦 Estoque (`/api/estoque`)
| Método | Rota | Descrição |
|---|---|---|
| GET | /api/estoque/unidade/{id} | Consultar estoque da unidade |
| POST | /api/estoque/unidade/{uid}/produto/{pid}/entrada | Registrar entrada |
| POST | /api/estoque/unidade/{uid}/produto/{pid}/saida | Registrar saída |

### 🎯 Fidelidade (`/api/fidelidade`)
| Método | Rota | Descrição |
|---|---|---|
| GET | /api/fidelidade/saldo | Consultar saldo de pontos |
| POST | /api/fidelidade/resgatar?pontos=X | Resgatar pontos |

### 🔍 Auditoria (`/api/auditoria`)

O sistema registra automaticamente todas as ações sensíveis
para garantir rastreabilidade e conformidade com a LGPD.

| Método | Rota | Descrição |
|---|---|---|
| GET | /api/auditoria | Listar todos os registros |
| GET | /api/auditoria/usuario/{email} | Filtrar por usuário |
| GET | /api/auditoria/acao/{acao} | Filtrar por tipo de ação |


**Ações registradas automaticamente:**

| Ação | Quando ocorre |
|---|---|
| `LOGIN` | Usuário realiza login |
| `USUARIO_CADASTRADO` | Novo usuário é criado |
| `PEDIDO_CRIADO` | Pedido registrado com sucesso |
| `PAGAMENTO_PROCESSADO` | Pagamento mock processado |
| `STATUS_PEDIDO_ATUALIZADO` | Status do pedido alterado |

---

## 👥 Perfis de Usuário

| Perfil | Como criar | Permissões |
|---|---|---|
| **CLIENTE** | Padrão — não precisa informar o perfil | Pedidos, cardápio e fidelidade |
| **GERENTE** | Informar `"perfil": "GERENTE"` no cadastro | Estoque e status de pedidos |
| **ADMIN** | Informar `"perfil": "ADMIN"` no cadastro | Acesso completo |

O campo `perfil` é opcional no cadastro. Quando não informado,
o sistema atribui **CLIENTE** automaticamente — garantindo que
cadastros públicos não obtenham privilégios indevidos.

**Exemplo de cadastro de gerente:**
```json
{
  "nome": "Maria Gerente",
  "email": "gerente@raizes.com",
  "senha": "Senha@123",
  "consentimentoLgpd": true,
  "perfil": "GERENTE"
}
```

> 💡 **Por que isso importa?** Os três perfis previstos no
> sistema (CLIENTE, GERENTE e ADMIN) podem ser criados via
> API, refletindo corretamente os atores do estudo de caso.

---

## 🔄 Fluxo de Pedidos
Cliente consulta cardápio da unidade --> Cliente faz pedido informando canal
--> API valida produtos e estoque --> Estoque é decrementado --> Pedido salvo (AGUARDANDO_PAGAMENTO)-->
PaymentMockService processa pagamento -->
                                        **|APROVADO --> PAGO --> Pontos --> somados --> Ação registrada na Auditoria**
                                        **|RECUSADO --> CANCELADO --> Estoque --> devolvido --> Ação registrada na Auditoria**



## 🧪 Testes

O projeto inclui uma coleção Postman com **12 cenários de teste**
(8 positivos e 4 negativos), disponível no arquivo:
raizes-postman-collection.json

### Como importar no Postman

1. Abra o Postman
2. Clique em **Import**
3. Selecione o arquivo `raizes-postman-collection.json`
4. Configure o ambiente com as variáveis:
   - **base_url = http://localhost:8080**
   - **token = (deixe vazio — será preenchido automaticamente pelo T01)**


### Cenários cobertos

| ID | Cenário | Tipo |
|---|---|---|
| T01 | Login válido | ✅ Positivo |
| T02 | Login com senha errada | ❌ Negativo |
| T03 | Acesso sem token | ❌ Negativo |
| T04 | Listar produtos | ✅ Positivo |
| T05 | Listar unidades | ✅ Positivo |
| T06 | Criar pedido aprovado | ✅ Positivo |
| T07 | Criar pedido recusado | ❌ Negativo |
| T08 | Filtrar pedidos por canal | ✅ Positivo |
| T09 | Consultar estoque | ✅ Positivo |
| T10 | Pedido sem itens | ❌ Negativo |
| T11 | Consultar saldo de pontos | ✅ Positivo |
| T12 | Resgatar pontos insuficientes | ❌ Negativo |

> 💡 **Dica:** Execute sempre o T01 primeiro — ele faz o
> login e salva o token automaticamente para os demais testes!

---

## 📁 Estrutura do Projeto
src/main/java/com/raizes/backend/

├── api/

│   ├── controller/     <-- endpoints REST

│   ├── dto/            <-- objetos de transferência

│   └── exception/      <-- tratamento de erros

├── application/

│   └── service/        <-- regras de negócio

├── domain/

│   ├── model/          <-- entidades JPA

│   └── repository/     <-- interfaces do banco

└── infrastructure/

├── payment/        <-- pagamento mock

└── security/       <-- JWT e Spring Security
src/main/resources/

├── db/migration/       <-- scripts Flyway (V1 a V11)

└── application.properties


## 🗄 Dados Iniciais

Ao subir a aplicação, o Flyway insere automaticamente:

**Unidades:**
- Raízes Recife Centro (PE)
- Raízes Fortaleza (CE)
- Raízes Salvador (BA)

**Produtos:**
- Tapioca Simples — R$ 8,90
- Cuscuz Recheado — R$ 12,90
- Bolo de Macaxeira — R$ 6,90
- Suco de Cajá — R$ 7,50
- Café Nordestino — R$ 4,00

**Estoque inicial por unidade:**

| Produto | Unidade 1 (Recife) | Unidade 2 (Fortaleza) | Unidade 3 (Salvador) |
|---|---|---|---|
| Tapioca Simples | 50 | 40 | 30 |
| Cuscuz Recheado | 30 | 25 | 20 |
| Bolo de Macaxeira | 20 | 15 | 10 |
| Suco de Cajá | 40 | 35 | 25 |
| Café Nordestino | 100 | 80 | 60 |

---

## 📊 Monitoramento e Auditoria

Enquanto a aplicação está rodando, todas as ações sensíveis
aparecem automaticamente no console do IntelliJ:
INFO [AUDITORIA] acao=LOGIN | usuario=admin@raizes.com |

Usuário realizou login no sistema | null

INFO [AUDITORIA] acao=PEDIDO_CRIADO | usuario=admin@raizes.com |

Pedido criado com sucesso | pedidoId=1 | canal=APP | total=R$30.70

INFO [AUDITORIA] acao=PAGAMENTO_PROCESSADO | usuario=admin@raizes.com |

Pagamento processado via mock | pedidoId=1 | status=APROVADO | forma=PIX

INFO [AUDITORIA] acao=STATUS_PEDIDO_ATUALIZADO | usuario=admin@raizes.com |

Status do pedido atualizado | pedidoId=1 | novoStatus=PAGO


Os registros também ficam salvos no banco e podem ser
consultados via API em `/api/auditoria`.

---

## ⚠️ Problemas Comuns

**Erro: "Connection refused" ao subir**
→ Verifique se o PostgreSQL está rodando e se a senha
está correta no **application.properties**

**Erro: "Flyway migration failed"**
→ Verifique se o banco **raizes_db** foi criado corretamente

**Erro: 403 no Swagger**
→ Verifique se clicou em Authorize e inseriu o token
no formato **Bearer SEU_TOKEN**

**Token expirado**
→ Faça login novamente para obter um novo token
(validade de 24 horas)

**Lista de auditoria vazia para ações como PEDIDO_CRIADO**
→ Os registros só aparecem para ações realizadas após
a aplicação ser iniciada. Crie um novo pedido e consulte
novamente em **/api/auditoria/acao/PEDIDO_CRIADO**

---




*Projeto Multidisciplinar — UNINTER 2026*
