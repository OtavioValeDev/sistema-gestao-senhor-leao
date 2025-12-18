# Sistema de Gestão - Senhor Leão

Sistema completo de gerenciamento de pedidos e cardápio para estabelecimentos de alimentação.
API REST moderna desenvolvida em Java Spring Boot com frontend responsivo.

![Logo](src/main/resources/static/logo_srLeao.png)

## 🎯 Visão Geral

O Sistema de Gestão Senhor Leão é uma solução completa para modernizar e otimizar operações de lanchonetes e restaurantes, oferecendo experiência digital completa para clientes e gestores.

### Contexto do Negócio

A digitalização do setor de alimentação é essencial para acompanhar a demanda crescente por experiências modernas de consumo. Este sistema transforma operações tradicionais em processos digitais eficientes, reduzindo custos operacionais e melhorando a satisfação do cliente.

## 🛠️ Tecnologias Utilizadas

- **Backend**: Java 21 + Spring Boot 3.5.8
- **Banco de Dados**: H2 (desenvolvimento) / MySQL/PostgreSQL (produção)
- **Frontend**: HTML5, CSS3, JavaScript (Tailwind CSS + Feather Icons)
- **APIs**: RESTful com documentação completa
- **Arquitetura**: MVC com separação clara de responsabilidades

## 📊 Funcionalidades Principais

### 👥 Área do Cliente
- **Navegação intuitiva** do cardápio com filtros por categoria
- **Sistema de carrinho** de compras interativo
- **Pagamento via PIX** com QR Code gerado dinamicamente
- **Solicitação de atendimento preferencial** para acessibilidade
- **Acompanhamento de pedidos** em tempo real

### 🛍️ Gestão de Produtos
- **CRUD completo** de produtos com upload de imagens
- **Gestão de categorias** e filtros dinâmicos
- **Controle de notificações** de atendimento preferencial
- **Histórico completo** de pedidos com edição
- **Interface administrativa** responsiva

### 👷 Gestão de Equipe
- **Cadastro e gestão** completa de funcionários
- **Controle de turnos** e salários
- **Sistema de notificações** integrado

## 🚀 Instalação e Execução

### Pré-requisitos
- **Java JDK 21** ou superior
- **Maven 3.6+** (wrapper incluído no projeto)

### Execução Local
```bash
# Clone o repositório
git clone [URL_DO_REPOSITORIO]
cd sistema-gestao-senhor-leao

# Execute a aplicação
./mvnw spring-boot:run
```

### Acesso ao Sistema
- **Página Inicial**: [http://localhost:8080](http://localhost:8080)
- **Área do Cliente**: [http://localhost:8080/cliente.html](http://localhost:8080/cliente.html)
- **Gestão de Produtos**: [http://localhost:8080/gestao-produtos.html](http://localhost:8080/gestao-produtos.html)
- **Gestão de Funcionários**: [http://localhost:8080/gestao-funcionarios.html](http://localhost:8080/gestao-funcionarios.html)
- **Console do Banco H2**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - *JDBC URL*: `jdbc:h2:mem:testdb`
  - *User*: `sa`
  - *Password*: (deixe em branco)

### Documentação para Desenvolvedores
Para desenvolvedores que desejam contribuir ou entender a arquitetura do projeto, consulte o **[Guia do Desenvolvedor](DEVELOPER_GUIDE.md)** que contém:
- Estrutura detalhada do projeto
- Padrões de código e convenções
- Fluxo de desenvolvimento
- Guia de contribuição
- Troubleshooting comum

## 🎯 Benefícios para o Negócio

### Eficiência Operacional
- **Redução de custos** com papel e processos manuais
- **Aumento da velocidade** de atendimento
- **Minimização de erros** humanos em pedidos

### Experiência do Cliente
- **Interface moderna** e intuitiva
- **Pagamento digital** conveniente
- **Acessibilidade** garantida com atendimento preferencial

### Escalabilidade
- **Suporte a múltiplas unidades** simultaneamente
- **Integração com marketplaces** (iFood, UberEats)
- **Dashboard analítico** para tomada de decisões

### Segurança e Conformidade
- **Tecnologia atual** e segura
- **Backup automático** de dados
- **Conformidade** com LGPD

## 🔮 Roadmap de Desenvolvimento

### Próximas Implementações (6-12 meses)
1. **Banco de Dados Persistente**
   - Migração para MySQL/PostgreSQL
   - Configuração de backup automático
   - Otimização de performance

2. **Segurança e Autenticação**
   - Spring Security com autenticação JWT
   - Controle de permissões por usuário
   - Logs de auditoria

3. **Gateway de Pagamento Real**
   - Integração com APIs oficiais (Mercado Pago, PagSeguro)
   - Suporte a múltiplas formas de pagamento
   - Webhooks para confirmação automática

### Expansão Futura (12-24 meses)
4. **Analytics e Relatórios**
   - Dashboard executivo com KPIs
   - Relatórios financeiros detalhados
   - Análise de vendas por período

5. **Aplicativo Mobile**
   - Versão nativa para iOS/Android
   - Integração com delivery
   - Sistema de fidelidade

6. **Integrações Avançadas**
   - Marketplace integrations
   - Sistema de delivery próprio
   - Integração com ERPs

## 📡 Documentação da API

### Produtos (`/api/products`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/products` | Lista todos os produtos cadastrados |
| `GET` | `/api/products/{id}` | Busca detalhes de um produto específico |
| `POST` | `/api/products` | Cria um novo produto |
| `PUT` | `/api/products/{id}` | Atualiza um produto existente |
| `DELETE` | `/api/products/{id}` | Remove um produto do cardápio |
| `POST` | `/api/products/notificacao-preferencial` | Cliente solicita atendimento preferencial |
| `GET` | `/api/products/notificacoes-pendentes` | Lista notificações pendentes |
| `POST` | `/api/products/notificacao/{id}/atender` | Marca notificação como atendida |

### Pedidos (`/api/recibos`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/recibos/pagar` | Finaliza um pedido e gera recibo |
| `GET` | `/api/recibos` | Lista histórico de pedidos |
| `GET` | `/api/recibos/{id}` | Busca detalhes de um pedido |
| `GET` | `/api/recibos/chamada/{numero}` | Busca pedido por número de chamada |
| `PUT` | `/api/recibos/{id}` | Edita um pedido existente |
| `DELETE` | `/api/recibos/{id}` | Remove pedido da lista |
| `DELETE` | `/api/recibos/limpar` | **[DEV]** Limpa histórico completo |

### Categorias (`/api/filters`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/filters` | Lista todas as categorias |
| `POST` | `/api/filters` | Cria uma nova categoria |
| `DELETE` | `/api/filters/{id}` | Remove uma categoria |

### Imagens (`/api/images`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/images/upload` | Upload de imagem de produto |

### Funcionários (`/api/funcionarios`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/funcionarios` | Lista todos os funcionários |
| `GET` | `/api/funcionarios/{id}` | Busca funcionário por ID |
| `POST` | `/api/funcionarios` | Cria novo funcionário |
| `PUT` | `/api/funcionarios/{id}` | Atualiza funcionário |
| `DELETE` | `/api/funcionarios/{id}` | Remove funcionário |

### Pagamentos (`/api/pagamento`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/pagamento/pix` | Gera transação PIX |
| `GET` | `/api/pagamento/{id}/status` | Verifica status do pagamento |
| `POST` | `/api/pagamento/{id}/confirmar` | Confirma pagamento |

## 🚀 Deployment em Produção

### Pré-requisitos de Produção
- Servidor com Java 21+
- Banco de dados MySQL/PostgreSQL
- Servidor web (Nginx/Apache) para arquivos estáticos
- SSL Certificate para HTTPS

### Configuração
```bash
# Build da aplicação
./mvnw clean package

# Executar em produção
java -jar target/*.jar --spring.profiles.active=prod
```

### Variáveis de Ambiente
```bash
# Banco de dados
DB_URL=jdbc:mysql://localhost:3306/senhor_leao
DB_USER=usuario
DB_PASS=senha

# Segurança
JWT_SECRET=chave-secreta-jwt

# Gateway de pagamento (futuro)
MERCADO_PAGO_ACCESS_TOKEN=token
```

---

**Desenvolvido por:** Aristocracia do Java