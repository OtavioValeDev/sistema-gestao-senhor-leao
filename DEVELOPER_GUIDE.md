# 👨‍💻 Guia do Desenvolvedor - Sistema de Gestão Senhor Leão

Este guia foi criado para ajudar novos desenvolvedores a entender a estrutura do projeto, padrões de código e como contribuir efetivamente.

## 📋 Índice

1. [Estrutura do Projeto](#estrutura-do-projeto)
2. [Arquitetura](#arquitetura)
3. [Padrões de Código](#padrões-de-código)
4. [Como Contribuir](#como-contribuir)
5. [Fluxo de Desenvolvimento](#fluxo-de-desenvolvimento)
6. [Testes](#testes)
7. [Troubleshooting](#troubleshooting)

---

## 🏗️ Estrutura do Projeto

```
sistema-gestao-senhor-leao/
├── src/
│   ├── main/
│   │   ├── java/com/example/projeto_test/
│   │   │   ├── config/          # Configurações (CORS, Web, Data)
│   │   │   ├── controller/      # Controllers REST (API endpoints)
│   │   │   ├── exception/       # Tratamento de exceções
│   │   │   ├── model/           # Entidades JPA (Product, Recibo, etc.)
│   │   │   ├── repository/      # Interfaces JPA Repository
│   │   │   ├── service/         # Lógica de negócio
│   │   │   └── SistemaGestaoSenhorLeaoApplication.java
│   │   └── resources/
│   │       └── static/          # Frontend (HTML, CSS, JS)
│   └── test/                    # Testes unitários e de integração
├── pom.xml                      # Dependências Maven
└── README.md                    # Documentação principal
```

### 📦 Pacotes e Responsabilidades

#### **Controllers** (`controller/`)
- **Responsabilidade**: Receber requisições HTTP e retornar respostas
- **Padrão**: Um controller por entidade principal
- **Exemplo**: `ProductController` gerencia todos os endpoints de `/api/products`

#### **Services** (`service/`)
- **Responsabilidade**: Lógica de negócio e validações
- **Padrão**: Um service por controller
- **Exemplo**: `ProductService` contém regras de negócio para produtos

#### **Repositories** (`repository/`)
- **Responsabilidade**: Acesso ao banco de dados
- **Padrão**: Interfaces que estendem `JpaRepository`
- **Exemplo**: `ProductRepository` fornece métodos CRUD para produtos

#### **Models** (`model/`)
- **Responsabilidade**: Entidades JPA que representam tabelas do banco
- **Padrão**: Classes anotadas com `@Entity` e `@Table`
- **Exemplo**: `Product` representa a tabela `products`

---

## 🏛️ Arquitetura

### Padrão MVC (Model-View-Controller)

```
Cliente (Frontend)
    ↓ HTTP Request
Controller (REST API)
    ↓ Chama
Service (Lógica de Negócio)
    ↓ Usa
Repository (Acesso a Dados)
    ↓ Consulta
Banco de Dados (H2/MySQL)
```

### Fluxo de Dados

1. **Frontend** faz requisição HTTP para `/api/products`
2. **Controller** recebe e valida a requisição
3. **Service** executa lógica de negócio (validações, cálculos)
4. **Repository** persiste ou busca dados no banco
5. **Service** retorna resultado processado
6. **Controller** formata resposta HTTP (JSON)
7. **Frontend** recebe e renderiza dados

---

## 📝 Padrões de Código

### 1. Nomenclatura

- **Classes**: PascalCase (`ProductController`, `ReciboService`)
- **Métodos**: camelCase (`createProduct`, `getAllRecibos`)
- **Variáveis**: camelCase (`productName`, `totalAmount`)
- **Constantes**: UPPER_SNAKE_CASE (`MOCK_QR_CODE_BASE64`)

### 2. Documentação JavaDoc

**Sempre documente:**
- Classes públicas
- Métodos públicos
- Parâmetros complexos
- Lógica de negócio não óbvia

**Exemplo:**
```java
/**
 * Cria um novo produto no cardápio.
 *
 * Valida o nome e preço antes de salvar. Se o produto
 * tiver filtros associados, busca os objetos completos
 * do banco antes de persistir.
 *
 * @param product Produto a ser criado (deve ter nome e preço válidos)
 * @return Produto criado com ID gerado
 * @throws IllegalArgumentException Se nome ou preço forem inválidos
 */
public Product createProduct(Product product) {
    // ...
}
```

### 3. Estrutura de Controller

```java
@RestController
@RequestMapping("/api/entidade")
public class EntidadeController {
    
    private final EntidadeService service;
    
    // Construtor com injeção de dependência
    public EntidadeController(EntidadeService service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity<List<Entidade>> listar() {
        // ...
    }
    
    @PostMapping
    public ResponseEntity<Entidade> criar(@RequestBody Entidade entidade) {
        // ...
    }
}
```

### 4. Tratamento de Erros

- Use `ResponseEntity` com status HTTP apropriado
- Retorne mensagens de erro claras
- Use `GlobalExceptionHandler` para erros globais

```java
try {
    // Operação
    return ResponseEntity.ok(result);
} catch (IllegalArgumentException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
} catch (RuntimeException e) {
    return ResponseEntity.notFound().build();
}
```

---

## 🚀 Como Contribuir

### 1. Setup do Ambiente

```bash
# Clone o repositório
git clone [URL_DO_REPOSITORIO]
cd sistema-gestao-senhor-leao

# Execute a aplicação
./mvnw spring-boot:run
```

### 2. Criando uma Nova Funcionalidade

#### Passo 1: Criar a Entidade (Model)
```java
@Entity
@Table(name = "nova_entidade")
public class NovaEntidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Campos...
}
```

#### Passo 2: Criar o Repository
```java
public interface NovaEntidadeRepository extends JpaRepository<NovaEntidade, Long> {
    // Métodos customizados se necessário
}
```

#### Passo 3: Criar o Service
```java
@Service
public class NovaEntidadeService {
    
    @Autowired
    private NovaEntidadeRepository repository;
    
    public NovaEntidade criar(NovaEntidade entidade) {
        // Lógica de negócio
        return repository.save(entidade);
    }
}
```

#### Passo 4: Criar o Controller
```java
@RestController
@RequestMapping("/api/nova-entidade")
public class NovaEntidadeController {
    
    private final NovaEntidadeService service;
    
    @PostMapping
    public ResponseEntity<NovaEntidade> criar(@RequestBody NovaEntidade entidade) {
        return ResponseEntity.ok(service.criar(entidade));
    }
}
```

### 3. Adicionando Novo Endpoint

1. **Defina o endpoint** no controller
2. **Implemente a lógica** no service
3. **Adicione JavaDoc** explicando o propósito
4. **Teste manualmente** via Postman/curl
5. **Atualize o README.md** com a nova rota

---

## 🔄 Fluxo de Desenvolvimento

### 1. Feature Branch
```bash
git checkout -b feature/nova-funcionalidade
```

### 2. Desenvolvimento
- Escreva código seguindo os padrões
- Adicione documentação JavaDoc
- Teste localmente

### 3. Commit
```bash
git add .
git commit -m "feat: adiciona funcionalidade X"
```

### 4. Push e Pull Request
```bash
git push origin feature/nova-funcionalidade
# Criar PR no GitHub/GitLab
```

### Convenções de Commit

- `feat:` Nova funcionalidade
- `fix:` Correção de bug
- `docs:` Documentação
- `refactor:` Refatoração de código
- `test:` Testes
- `chore:` Tarefas de manutenção

---

## 🧪 Testes

### Executar Testes
```bash
./mvnw test
```

### Estrutura de Testes
```
src/test/java/
└── com/example/projeto_test/
    ├── controller/
    ├── service/
    └── repository/
```

### Exemplo de Teste
```java
@SpringBootTest
class ProductServiceTest {
    
    @Autowired
    private ProductService service;
    
    @Test
    void deveCriarProdutoComSucesso() {
        Product product = new Product();
        product.setName("Hambúrguer");
        product.setPriceInCents(1500);
        
        Product criado = service.createProduct(product);
        
        assertNotNull(criado.getId());
        assertEquals("Hambúrguer", criado.getName());
    }
}
```

---

## 🔧 Troubleshooting

### Problema: Porta 8080 já em uso

**Solução:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID [PID] /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### Problema: Erro de compilação

**Solução:**
```bash
# Limpar e recompilar
./mvnw clean compile

# Reinstalar dependências
./mvnw clean install
```

### Problema: Banco H2 não conecta

**Verificar:**
- URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: (vazio)

### Problema: CORS bloqueando requisições

**Verificar:**
- `CorsConfig.java` está configurado
- Headers CORS nos controllers
- Frontend usando URL correta

---

## 📚 Recursos Úteis

### Documentação Oficial
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [H2 Database](https://www.h2database.com/html/main.html)

### Ferramentas Recomendadas
- **IDE**: IntelliJ IDEA ou Eclipse
- **API Testing**: Postman ou Insomnia
- **Database**: H2 Console (http://localhost:8080/h2-console)

---

## ❓ Dúvidas Frequentes

### Q: Onde adicionar validações de negócio?
**R:** No Service, não no Controller. Controllers apenas recebem/retornam dados.

### Q: Como adicionar um novo campo em uma entidade?
**R:** 
1. Adicione o campo na classe Model
2. Execute migration (ou recrie tabela em H2)
3. Atualize DTOs se necessário

### Q: Como testar endpoints localmente?
**R:** Use Postman, curl ou o próprio frontend. Exemplo:
```bash
curl -X GET http://localhost:8080/api/products
```

### Q: Onde ficam os arquivos de upload?
**R:** `src/main/resources/static/uploads/` - acessíveis via `/uploads/nome-arquivo.jpg`

---

## 📞 Contato

Para dúvidas ou sugestões, consulte o README.md principal ou entre em contato com a equipe de desenvolvimento.

**Desenvolvido por:** Aristocracia do Java 👑

