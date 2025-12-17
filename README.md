# 🦁 Sistema de Gestão - Senhor Leão

Sistema de gerenciamento de pedidos e cardápio para a lanchonete Senhor Leão. O projeto consiste em uma API REST desenvolvida em Java com Spring Boot e um frontend web moderno e responsivo.

![Logo](src/main/resources/static/logo_srLeao.png)

## 🚀 Como Iniciar o Projeto

### Pré-requisitos
- **Java JDK 21** ou superior
- **Maven** (instalado ou via wrapper)

### Instalação e Execução
1.  **Clone o repositório** para sua máquina local.
2.  **Navegue até a pasta do projeto** via terminal.
3.  **Execute a aplicação**:
    - **Windows**:
      ```powershell
      ./mvnw spring-boot:run
      ```
    - **Linux/Mac**:
      ```bash
      ./mvnw spring-boot:run
      ```
4.  Acesse a aplicação no navegador:
    - **Página Inicial**: [http://localhost:8080](http://localhost:8080)
    - **Área do Cliente**: [http://localhost:8080/cliente.html](http://localhost:8080/cliente.html)
    - **Painel do Funcionário**: [http://localhost:8080/funcionario.html](http://localhost:8080/funcionario.html)
    - **Console do Banco H2**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
        - *JDBC URL*: `jdbc:h2:mem:testdb`
        - *User*: `sa`
        - *Password*: (deixe em branco)

---

## 🚧 Implementações Futuras (O que falta implementar)

Este projeto é um MVP (Produto Mínimo Viável) e possui pontos de melhoria planejados para futuras versões:

1.  **Persistência de Dados Real**: Atualmente o projeto utiliza **H2 Database em memória**. Todos os dados inseridos (pedidos, novos produtos) são perdidos ao reiniciar a aplicação. Necessário migrar para MySQL ou PostgreSQL.
2.  **Segurança e Autenticação**: O painel do funcionário (`funcionario.html`) é aberto. Necessário implementar **Spring Security** com login/senha ou JWT para proteger as rotas administrativas.
3.  **Gateway de Pagamento Real**: A integração com Pix é **simulada**. O fluxo atual gera um QR Code estático ou de teste e aprova transações localmente. Necessário integrar com a API oficial do banco.
4.  **Testes Automatizados**: A cobertura de testes unitários e de integração precisa ser expandida para garantir a estabilidade do sistema em produção.
5.  **Relatórios**: Ausência de um dashboard com relatórios financeiros e de vendas diárias.

---

## 📡 Documentação da API (Para Teste/Apresentação)

Abaixo estão listados os principais Endpoints da API para teste durante a apresentação.

### 📦 Produtos (`/api/products`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/api/products` | Lista todos os produtos cadastrados. |
| `GET` | `/api/products/{id}` | Busca detalhes de um produto específico. |
| `POST` | `/api/products` | Cria um novo produto (JSON body required). |
| `PUT` | `/api/products/{id}` | Atualiza um produto existente. |
| `DELETE` | `/api/products/{id}` | Remove um produto do cardápio. |
| `POST` | `/api/products/notificacao-preferencial` | Cliente solicita atendimento preferencial. |
| `GET` | `/api/products/notificacoes-pendentes` | Funcionário visualiza solicitações pendentes. |
| `POST` | `/api/products/notificacao/{id}/atender` | Funcionário marca solicitação como atendida. |

### 🧾 Recibos e Pedidos (`/api/recibos`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/recibos/pagar` | Finaliza um pedido e gera a notinha. |
| `GET` | `/api/recibos` | Lista histórico de todos os pedidos. |
| `GET` | `/api/recibos/{id}` | Busca detalhes de um pedido específico. |
| `GET` | `/api/recibos/chamada/{numero}` | Busca pedido pelo número da senha (ex: 0042). |
| `PUT` | `/api/recibos/{id}` | Edita um pedido (ex: correção de itens). |
| `DELETE` | `/api/recibos/{id}` | Marca um pedido como concluído (remove da lista). |
| `DELETE` | `/api/recibos/limpar` | **[DEV]** Apaga todo o histórico de pedidos. |

### 🏷️ Filtros/Categorias (`/api/filters`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/api/filters` | Lista todas as categorias disponíveis. |
| `POST` | `/api/filters` | Cria uma nova categoria. |
| `DELETE` | `/api/filters/{id}` | Remove uma categoria. |

### 🖼️ Imagens (`/api/images`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/images/upload` | Upload de imagem de produto (Multipart File). |

### 💲 Pagamento (`/api/pagamento`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/pagamento/pix` | Gera uma transação Pix simulada (retorna QR Code). |
| `GET` | `/api/pagamento/{id}/status` | Verifica status do pagamento (polling). |
| `POST` | `/api/pagamento/{id}/confirmar` | Simula a aprovação do pagamento pelo banco. |

---

**Desenvolvido por:** Aristocracia do Java 👑