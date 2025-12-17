package com.example.projeto_test.controller;
// ↑ Declara que este arquivo pertence ao pacote de controllers
//   Controllers são responsáveis por receber requisições HTTP e devolver respostas

import com.example.projeto_test.model.Product;
// ↑ Importa a entidade Product (representa um produto do cardápio)
import com.example.projeto_test.service.ProductService;
// ↑ Importa o serviço que contém a lógica de negócio dos produtos
import org.springframework.http.HttpStatus;
// ↑ Importa enum com códigos de status HTTP (200, 201, 404, etc.)
import org.springframework.http.ResponseEntity;
// ↑ Importa classe para criar respostas HTTP estruturadas
import org.springframework.lang.NonNull;
// ↑ Anotação que indica que um parâmetro não pode ser null
import org.springframework.web.bind.annotation.*;
// ↑ Importa todas as anotações para mapeamento de endpoints REST
import org.springframework.web.bind.annotation.RequestMethod;
// ↑ Importa especificamente para métodos HTTP (GET, POST, etc.)

import java.util.*;
// ↑ Importa classes utilitárias como List, Map, HashMap, etc.
import java.util.List;
// ↑ Importa especificamente a interface List

/**
 * Controller REST para operações com produtos do cardápio.
 *
 * Esta classe expõe endpoints HTTP para:
 * - Criar novos produtos
 * - Listar produtos existentes
 * - Buscar produto por ID
 * - Atualizar produtos
 * - Deletar produtos
 * - Gerenciar notificações de atendimento preferencial
 */
@RestController // ← Anotação que marca esta classe como um controller REST
//   - Todas as respostas são automaticamente convertidas para JSON
//   - Não precisa usar @ResponseBody em cada método
@RequestMapping("/api/products") // ← Define a URL base para todos os endpoints desta classe
//   - Todos os endpoints começarão com /api/products
public class ProductController {

    private final ProductService productService;
    // ↑ Campo que armazena a referência para o serviço de produtos
    //   final = não pode ser alterado após inicialização

    public ProductController(ProductService productService) {
        // ↑ Construtor da classe (injeção de dependência)
        //   Spring Boot automaticamente passa uma instância do ProductService
        this.productService = productService;
        // ↑ Atribui o serviço injetado ao campo da classe
    }

    @PostMapping // ← Anotação que mapeia requisições HTTP POST para este método
    //   POST é usado para criar novos recursos
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // ↑ Método público que cria um novo produto
        //   @RequestBody = converte JSON da requisição para objeto Product
        //   ResponseEntity<Product> = tipo de retorno que permite controle total da resposta HTTP

        System.out.println("POST /products chamado com: " + product.getName());
        // ↑ Log no console para debug - mostra qual produto está sendo criado
        //   Útil para acompanhar o fluxo da aplicação

        Product savedProduct = productService.createProduct(product);
        // ↑ Chama o serviço para salvar o produto no banco de dados
        //   O serviço retorna o produto salvo (com ID gerado)

        return ResponseEntity.status(HttpStatus.CREATED)
                // ↑ Define o status HTTP da resposta como 201 (Created)
                //   Padrão REST para indicar que um novo recurso foi criado
                .header("Access-Control-Allow-Origin", "*")
                // ↑ Permite requisições de qualquer origem (CORS)
                //   Necessário para frontend acessar a API
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                // ↑ Lista os métodos HTTP permitidos
                .header("Access-Control-Allow-Headers", "*")
                // ↑ Permite qualquer cabeçalho na requisição
                .body(savedProduct);
                // ↑ Define o corpo da resposta como o produto criado
                //   Será automaticamente convertido para JSON
    }

    @RequestMapping(method = RequestMethod.GET)
    // ↑ Mapeia requisições GET para este método
    //   @RequestMapping é mais flexível que @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        // ↑ Método que retorna todos os produtos do cardápio

        System.out.println("GET /products chamado");
        // ↑ Log para indicar que a requisição foi recebida

        List<Product> products = productService.getAllProducts();
        // ↑ Busca todos os produtos através do serviço
        //   O serviço faz a consulta no banco de dados

        System.out.println("Produtos encontrados: " + products.size());
        // ↑ Log mostrando quantos produtos foram encontrados

        for (Product p : products) {
            // ↑ Loop for-each para iterar sobre cada produto
            //   Imprime detalhes de cada produto no log
            System.out.println("Produto: " + p.getId() + " - " + p.getName() + " - " + p.getPriceInCents());
            // ↑ Log detalhado de cada produto (ID, nome, preço em centavos)
        }

        return ResponseEntity.ok()
                // ↑ .ok() define status HTTP 200 (OK) - sucesso
                .header("Access-Control-Allow-Origin", "*")
                // ↑ Permite acesso de qualquer domínio (importante para frontend)
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                // ↑ Lista métodos HTTP permitidos
                .header("Access-Control-Allow-Headers", "*")
                // ↑ Permite qualquer cabeçalho
                .header("Content-Type", "application/json")
                // ↑ Especifica que a resposta é JSON
                .body(products);
                // ↑ Define o corpo da resposta como a lista de produtos
                //   Spring Boot converte automaticamente para JSON
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable @NonNull Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable @NonNull Long id, @RequestBody @NonNull Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .body(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable @NonNull Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .build();
    }

    /**
     * Endpoint de debug para verificar se o cliente.html consegue carregar produtos
     * Retorna dados JSON simples para debug
     */
    @GetMapping("/debug")
    public ResponseEntity<String> debugProdutos() {
        List<Product> products = productService.getAllProducts();

        StringBuilder debug = new StringBuilder();
        debug.append("{\n");
        debug.append("  \"status\": \"OK\",\n");
        debug.append("  \"timestamp\": \"").append(java.time.Instant.now()).append("\",\n");
        debug.append("  \"products_count\": ").append(products.size()).append(",\n");
        debug.append("  \"products\": [\n");

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            debug.append("    {\n");
            debug.append("      \"id\": ").append(product.getId()).append(",\n");
            debug.append("      \"name\": \"").append(product.getName()).append("\",\n");
            debug.append("      \"priceInCents\": ").append(product.getPriceInCents()).append("\n");
            debug.append("    }");
            if (i < products.size() - 1) {
                debug.append(",");
            }
            debug.append("\n");
        }

        debug.append("  ],\n");
        debug.append("  \"message\": \"Se você vê isso, a API está funcionando!\"\n");
        debug.append("}");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Access-Control-Allow-Origin", "*")
                .body(debug.toString());
    }

    /**
     * Endpoint de teste CORS - simula exatamente o que o cliente.html faz
     */
    @GetMapping("/cors-test")
    public ResponseEntity<String> corsTest() {
        List<Product> products = productService.getAllProducts();

        // Simula exatamente a resposta que o cliente.html espera
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            json.append("{\"id\":").append(product.getId())
                .append(",\"name\":\"").append(product.getName())
                .append("\",\"priceInCents\":").append(product.getPriceInCents())
                .append("}");
            if (i < products.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .body(json.toString());
    }

    /**
     * Lista estática de notificações para atendimento preferencial.
     * Esta lista armazena todas as notificações ativas no sistema.
     * static = compartilhada por todas as instâncias da classe
     * final = não pode ser reatribuída, mas pode ser modificada
     */
    private static final List<Map<String, Object>> preferentialNotifications = new ArrayList<>();
    // ↑ Lista que armazena notificações como Map<String, Object>
    //   Cada notificação é um mapa com campos como id, timestamp, type, etc.

    /**
     * Endpoint para solicitar atendimento preferencial (sem observações).
     * Este método é chamado quando um cliente clica no botão de atendimento preferencial.
     */
    @PostMapping("/notificacao-preferencial")
    // ↑ Mapeia POST /api/products/notificacao-preferencial
    public ResponseEntity<Map<String, Object>> solicitarAtendimentoPreferencial() {
        // ↑ Método que cria uma nova notificação de atendimento preferencial
        Map<String, Object> notification = new HashMap<>();
        // ↑ Cria um novo mapa para armazenar os dados da notificação

        notification.put("id", System.currentTimeMillis());
        // ↑ Define um ID único baseado no timestamp atual
        //   currentTimeMillis() retorna milissegundos desde 1970

        notification.put("timestamp", java.time.Instant.now());
        // ↑ Registra o momento exato da criação da notificação
        //   Instant.now() dá o timestamp atual em UTC

        notification.put("type", "PREFERENTIAL_SERVICE");
        // ↑ Define o tipo da notificação como atendimento preferencial

        notification.put("message", "Cliente solicitou atendimento preferencial");
        // ↑ Mensagem descritiva da notificação

        notification.put("status", "PENDING");
        // ↑ Status inicial como "PENDING" (pendente)
        //   Será alterado para "ATTENDED" quando o funcionário atender

        preferentialNotifications.add(notification);
        // ↑ Adiciona a nova notificação à lista estática
        //   Todas as instâncias do controller compartilham esta lista

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Solicitação de atendimento preferencial enviada com sucesso!");
        response.put("notificationId", notification.get("id"));

        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .body(response);
    }

    /**
     * Endpoint para o funcionário ver notificações pendentes
     */
    @GetMapping("/notificacoes-pendentes")
    public ResponseEntity<List<Map<String, Object>>> getNotificacoesPendentes() {
        List<Map<String, Object>> pendingNotifications = preferentialNotifications.stream()
                .filter(notification -> "PENDING".equals(notification.get("status")))
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .body(pendingNotifications);
    }

    /**
     * Endpoint para marcar notificação como atendida
     */
    @PostMapping("/notificacao/{id}/atender")
    public ResponseEntity<Map<String, Object>> atenderNotificacao(@PathVariable String id) {
        for (Map<String, Object> notification : preferentialNotifications) {
            if (id.equals(String.valueOf(notification.get("id")))) {
                notification.put("status", "ATTENDED");
                notification.put("attendedAt", java.time.Instant.now());

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Notificação marcada como atendida");

                return ResponseEntity.ok()
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                        .header("Access-Control-Allow-Headers", "*")
                        .body(response);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Notificação não encontrada");

        return ResponseEntity.notFound().build();
    }
}

