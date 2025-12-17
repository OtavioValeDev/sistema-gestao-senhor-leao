# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.7/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.7/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.7/reference/web/servlet.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.5.7/reference/using/devtools.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.7/reference/data/sql.html#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

---

# CRUD com Spring Boot - Documentação

## Estrutura criada

### 1. Dependências adicionadas (`pom.xml`)

- **Spring Boot Starter Validation** (validações)
- **Lombok** (redução de boilerplate)

### 2. Configuração do banco (`application.properties`)

- H2 Database configurado em memória
- Console H2 habilitado em `/h2-console`
- JPA configurado com logs SQL

### 3. Entidade Product (`model/Product.java`)

- Anotações JPA (`@Entity`, `@Id`, `@GeneratedValue`)
- Validações (`@NotBlank`, `@Positive`)
- Lombok para getters/setters

### 4. Repositório (`repository/ProductRepository.java`)

- Interface que estende `JpaRepository`
- Métodos CRUD automáticos

### 5. Controller (`controller/ProductController.java`)

Endpoints REST:

- `POST /products` - Criar produto
- `GET /products` - Listar todos
- `GET /products/{id}` - Buscar por ID
- `PUT /products/{id}` - Atualizar produto
- `DELETE /products/{id}` - Deletar produto

### 6. Tratamento de exceções (`exception/GlobalExceptionHandler.java`)

- Handler global para exceções
- Validações com mensagens de erro

## Como testar

### 1. Execute a aplicação:

```bash
mvn spring-boot:run
```

### 2. Acesse o console H2: `http://localhost:8080/h2-console`

- **JDBC URL:** `jdbc:h2:mem:testdb`
- **User:** `sa`
- **Password:** (vazio)

### 3. Teste os endpoints:

#### Criar: `POST http://localhost:8080/products`

```json
{
  "name": "Notebook",
  "priceInCents": 250000
}
```

#### Listar: `GET http://localhost:8080/products`

#### Buscar: `GET http://localhost:8080/products/1`

#### Atualizar: `PUT http://localhost:8080/products/1`

```json
{
  "name": "Notebook Atualizado",
  "priceInCents": 300000
}
```

#### Deletar: `DELETE http://localhost:8080/products/1`

---

# 📚 DOCUMENTAÇÃO DETALHADA LINHA POR LINHA

## 🔧 `pom.xml` - Configuração Maven

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- Declaração XML - versão e encoding -->
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
<!-- Elemento raiz com namespaces XML para validação -->

<modelVersion>4.0.0</modelVersion>
<!-- Versão do modelo POM (sempre 4.0.0) -->

<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.8</version>
    <!-- Herda configurações padrão do Spring Boot -->
</parent>

<groupId>com.example</groupId>
<!-- Identificador único do grupo/projeto -->
<artifactId>projeto_test</artifactId>
<!-- Nome do artefato -->
<version>0.0.1-SNAPSHOT</version>
<!-- Versão (SNAPSHOT = desenvolvimento) -->
<name>projeto_test</name>
<description>Project test for Spring Boot</description>

<properties>
    <java.version>22</java.version>
    <!-- Versão do Java a usar -->
</properties>

<dependencies>
    <!-- DEPENDÊNCIAS ESSENCIAIS -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
        <!-- JPA/Hibernate para banco de dados -->
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <!-- Spring MVC + Tomcat para APIs REST -->
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
        <!-- Ferramentas de desenvolvimento (hot reload) -->
    </dependency>

    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
        <!-- Banco H2 em memória -->
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
        <!-- Validações (@NotBlank, @Positive, etc.) -->
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
        <!-- Reduz código boilerplate (getters/setters) -->
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
        <!-- Dependências para testes unitários -->
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <!-- Plugin para executar: mvn spring-boot:run -->
        </plugin>
    </plugins>
</build>

</project>
```

## 🚀 `ProjetoTestApplication.java` - Classe Principal

```java
package com.example.projeto_test;
// Declara pacote da aplicação

import org.springframework.boot.SpringApplication;
// Import da classe que inicializa Spring Boot
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
// Imports para funcionalidades REST
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
// Imports para configuração

@SpringBootApplication
// ANOTAÇÃO CRÍTICA: configura automaticamente:
// - Component scanning (@Controller, @Service, @Repository)
// - Auto-configuration (banco, servidor web)
// - Bean creation
public class ProjetoTestApplication {

    // MÉTODO PRINCIPAL - PONTO DE ENTRADA DA APLICAÇÃO
    public static void main(String[] args) {
        // args = argumentos da linha de comando (não usados)
        SpringApplication.run(ProjetoTestApplication.class, args);
        // LINHA CRÍTICA: inicializa toda a aplicação Spring Boot
        // Primeiro parâmetro: classe principal
        // Segundo parâmetro: argumentos
    }

    // CLASSE INTERNA PARA CONFIGURAÇÕES ADICIONAIS
    @Configuration
    // @Configuration = esta classe contém configurações
    static class AdditionalConfig implements WebMvcConfigurer {
        // WebMvcConfigurer = interface para customizar Spring MVC
        // static = compartilhada por todas instâncias
    }
}
```

## 🎯 `ProductController.java` - API REST dos Produtos

```java
package com.example.projeto_test.controller;
// Pacote dos controllers (APIs REST)

import com.example.projeto_test.model.Product;
// Import da entidade Product
import com.example.projeto_test.service.ProductService;
// Import do serviço de negócio
import org.springframework.http.HttpStatus;
// Códigos de status HTTP (200, 201, 404, etc.)
import org.springframework.http.ResponseEntity;
// Classe para respostas HTTP estruturadas
import org.springframework.lang.NonNull;
// Anotação para parâmetros obrigatórios
import org.springframework.web.bind.annotation.*;
// Todas anotações REST (@GetMapping, @PostMapping, etc.)
import org.springframework.web.bind.annotation.RequestMethod;
// Métodos HTTP específicos

import java.util.*;
// Utilitários (List, Map, HashMap)
import java.util.List;
// Interface List

@RestController
// ANOTAÇÃO CRÍTICA: marca classe como controller REST
// Respostas são automaticamente JSON
@RequestMapping("/api/products")
// URL base: todos endpoints começam com /api/products
public class ProductController {

    private final ProductService productService;
    // Campo para injeção do serviço
    // final = não pode ser alterado após inicialização

    public ProductController(ProductService productService) {
        // CONSTRUTOR: injeção de dependência automática
        // Spring encontra instância de ProductService
        this.productService = productService;
    }

    @PostMapping
    // Mapeia HTTP POST para este método
    // POST = criar novos recursos
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // @RequestBody = converte JSON da requisição para Product
        // ResponseEntity<Product> = controle total da resposta HTTP

        System.out.println("POST /products chamado com: " + product.getName());
        // LOG para debug - mostra produto sendo criado

        Product savedProduct = productService.createProduct(product);
        // Chama serviço para salvar no banco
        // Retorna produto com ID gerado

        return ResponseEntity.status(HttpStatus.CREATED)
                // Status HTTP 201 (Created) - recurso criado
                .header("Access-Control-Allow-Origin", "*")
                // CORS: permite requisições de qualquer origem
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                // Métodos HTTP permitidos
                .header("Access-Control-Allow-Headers", "*")
                // Qualquer cabeçalho permitido
                .body(savedProduct);
                // Corpo da resposta = produto criado
    }

    @RequestMapping(method = RequestMethod.GET)
    // Mapeia GET - mais flexível que @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        // Retorna lista de todos produtos

        System.out.println("GET /products chamado");
        // Log da requisição

        List<Product> products = productService.getAllProducts();
        // Busca todos produtos via serviço

        System.out.println("Produtos encontrados: " + products.size());
        // Log quantidade de produtos

        for (Product p : products) {
            // LOOP para log detalhado de cada produto
            System.out.println("Produto: " + p.getId() + " - " + p.getName() + " - " + p.getPriceInCents());
        }

        return ResponseEntity.ok()
                // .ok() = status HTTP 200 (OK)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .header("Content-Type", "application/json")
                // Especifica resposta como JSON
                .body(products);
                // Corpo = lista de produtos
    }
}
```

## 📦 `Product.java` - Entidade JPA

```java
package com.example.projeto_test.model;
// Pacote das entidades (modelo de dados)

import com.fasterxml.jackson.annotation.JsonProperty;
// Jackson = conversão JSON automático
import jakarta.persistence.*;
// Todas anotações JPA (banco de dados)
import jakarta.validation.constraints.NotBlank;
// Validação: campo não pode ser vazio
import jakarta.validation.constraints.Positive;
// Validação: valor deve ser positivo

@Entity
// ANOTAÇÃO CRÍTICA: marca classe como entidade JPA
// Será mapeada para tabela no banco
@Table(name = "products")
// Nome da tabela no banco
public class Product {

    @Id
    // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Geração automática de ID (AUTO_INCREMENT)
    @JsonProperty("id")
    // Nome do campo no JSON
    private Long id;

    @NotBlank(message = "Name is required")
    // VALIDAÇÃO: campo obrigatório
    @Column(nullable = false, length = 100)
    // Coluna não nula, tamanho máximo 100
    @JsonProperty("name")
    private String name;

    @Positive(message = "Price must be positive")
    // VALIDAÇÃO: preço deve ser positivo
    @Column(nullable = false)
    // Coluna obrigatória
    @JsonProperty("priceInCents")
    private Integer priceInCents;

    public Product() {}
    // CONSTRUTOR PADRÃO: necessário para JPA

    public Product(String name, Integer priceInCents) {
        // Construtor completo
        this.name = name;
        this.priceInCents = priceInCents;
    }

    // GETTERS E SETTERS
    // Necessários para JPA acessar campos privados
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getPriceInCents() { return priceInCents; }
    public void setPriceInCents(Integer priceInCents) { this.priceInCents = priceInCents; }
}
```

## ⚙️ `ProductService.java` - Lógica de Negócio

```java
package com.example.projeto_test.service;
// Pacote dos serviços (lógica de negócio)

import com.example.projeto_test.model.Product;
// Entidade Product
import com.example.projeto_test.repository.ProductRepository;
// Repository para acesso a dados
import org.springframework.beans.factory.annotation.Autowired;
// Injeção automática de dependências
import org.springframework.lang.NonNull;
// Parâmetros obrigatórios
import org.springframework.stereotype.Service;
// Marca classe como serviço

import java.util.List;
// Interface List

@Service
// ANOTAÇÃO: registra classe como serviço Spring
// Instâncias gerenciadas pelo container
public class ProductService {

    @Autowired
    // INJEÇÃO AUTOMÁTICA do repository
    private ProductRepository productRepository;

    public Product createProduct(@NonNull Product product) {
        // @NonNull = parâmetro não pode ser null
        return productRepository.save(product);
        // Salva e retorna produto com ID gerado
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
        // findAll() = busca todos registros
    }

    public Product getProductById(@NonNull Long id) {
        return productRepository.findById(id)
                // findById() retorna Optional
                .orElseThrow(() -> new RuntimeException("Product not found"));
                // Se não encontrar, lança exception
    }

    public Product updateProduct(@NonNull Long id, @NonNull Product productDetails) {
        Product product = getProductById(id);
        // Busca produto existente
        product.setName(productDetails.getName());
        // Atualiza nome
        product.setPriceInCents(productDetails.getPriceInCents());
        // Atualiza preço
        return productRepository.save(product);
        // Salva alterações
    }

    public void deleteProduct(@NonNull Long id) {
        if (!productRepository.existsById(id)) {
            // existsById() = verifica se produto existe
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
        // deleteById() = remove pelo ID
    }
}
```

## 💾 `ProductRepository.java` - Acesso a Dados

```java
package com.example.projeto_test.repository;
// Pacote dos repositories (acesso a dados)

import com.example.projeto_test.model.Product;
// Entidade Product
import org.springframework.data.jpa.repository.JpaRepository;
// Interface base com métodos CRUD

public interface ProductRepository extends JpaRepository<Product, Long> {
    // ESTENDE JpaRepository<Product, Long>
    // Product = entidade, Long = tipo da chave primária

    // MÉTODOS AUTOMÁTICOS INCLUÍDOS:
    // save(Product) - salvar
    // findById(Long) - buscar por ID
    // findAll() - buscar todos
    // deleteById(Long) - deletar
    // existsById(Long) - verificar existência
    // count() - contar registros

    // MÉTODOS CUSTOMIZADOS POSSÍVEIS:
    // List<Product> findByName(String name);
    // List<Product> findByPriceInCentsLessThan(Integer price);
    // List<Product> findByNameContainingIgnoreCase(String name);
}
```

## 🌐 `cliente.html` - Interface do Cliente (JavaScript)

```javascript
// CONFIGURAÇÃO DA API
const API_BASE = window.location.protocol + '//' + window.location.host + '/api/products';
// URL base da API de produtos
const RECIBOS_API = window.location.protocol + '//' + window.location.host + '/api/recibos';
// URL base da API de recibos

let allProducts = [];
// Array para armazenar produtos carregados
let cart = [];
// Array para carrinho de compras

// INICIALIZAÇÃO DA APLICAÇÃO
document.addEventListener('DOMContentLoaded', function() {
    // Event listener para quando DOM carrega
    console.log('🚀 Aplicação Cliente inicializando...');

    // Inicializa ícones Feather
    if (typeof feather !== 'undefined') {
        feather.replace();
        // Substitui <i data-feather="icon"> por SVGs
    }

    setupEventListeners();
    // Configura ouvintes de eventos (cliques)

    loadProducts();
    // Carrega produtos da API

    setInterval(loadProducts, 30000);
    // Atualização automática a cada 30 segundos

    console.log('✅ Aplicação Cliente inicializada!');
});

// FUNÇÃO PARA CARREGAR PRODUTOS
async function loadProducts() {
    // async = permite usar await
    console.log('🔄 Carregando produtos...');
    setProductsState('loading');
    // Muda interface para estado "carregando"

    try {
        // BLOCO TRY-CATCH para tratamento de erros
        const response = await fetch(API_BASE, {
            // fetch() = requisição HTTP assíncrona
            method: 'GET',
            // Método HTTP GET
            headers: {
                'Accept': 'application/json'
                // Aceita resposta JSON
            }
        });

        if (!response.ok) {
            // Verifica se resposta foi bem-sucedida
            throw new Error(`HTTP ${response.status}`);
        }

        const products = await response.json();
        // Converte resposta JSON para objeto JavaScript
        console.log(`✅ ${products.length} produtos carregados`);

        allProducts = products;
        // Armazena produtos globalmente
        displayProducts(products);
        // Exibe produtos na interface
        setProductsState('loaded');
        // Muda estado para "carregado"

    } catch (error) {
        // Se der erro
        console.error('❌ Erro ao carregar produtos:', error);
        setProductsState('error');
        // Mostra estado de erro
    }
}
```