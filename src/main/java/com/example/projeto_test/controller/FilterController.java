package com.example.projeto_test.controller;
// ↑ Declara que este arquivo pertence ao pacote de controllers
//   Controllers são responsáveis por receber requisições HTTP e devolver respostas

import com.example.projeto_test.model.Filter;
// ↑ Importa a entidade Filter (representa uma categoria/filtro de produtos)
import com.example.projeto_test.service.FilterService;
// ↑ Importa o serviço que contém a lógica de negócio dos filtros
import org.springframework.http.HttpStatus;
// ↑ Importa enum com códigos de status HTTP (200, 201, 404, etc.)
import org.springframework.http.ResponseEntity;
// ↑ Importa classe para criar respostas HTTP estruturadas
import org.springframework.web.bind.annotation.*;
// ↑ Importa todas as anotações para mapeamento de endpoints REST
import org.springframework.web.bind.annotation.RequestMethod;
// ↑ Importa especificamente para métodos HTTP (GET, POST, etc.)

import java.util.List;
// ↑ Importa interface List para trabalhar com coleções

/**
 * Controller REST para operações com filtros/categorias de produtos.
 *
 * Esta classe expõe endpoints HTTP para:
 * - Listar todas as categorias disponíveis
 * - Criar novas categorias
 * - Remover categorias existentes
 * - Gerenciar CORS para requisições cross-origin
 *
 * As categorias ajudam a organizar os produtos do cardápio (ex: "Bebidas", "Lanches", "Sobremesas").
 *
 * @author Aristocracia do Java
 * @version 1.0
 * @since 2025-01-01
 */
@RestController // ← Anotação que marca esta classe como um controller REST
//   - Todas as respostas são automaticamente convertidas para JSON
//   - Não precisa usar @ResponseBody em cada método
@RequestMapping("/api/filters") // ← Define a URL base para todos os endpoints desta classe
//   - Todos os endpoints começarão com /api/filters
public class FilterController {

    private final FilterService filterService;
    // ↑ Campo que armazena a referência para o serviço de filtros
    //   final = não pode ser alterado após inicialização

    /**
     * Construtor da classe com injeção de dependência.
     *
     * @param filterService Serviço de filtros injetado pelo Spring
     */
    public FilterController(FilterService filterService) {
        // ↑ Construtor da classe (injeção de dependência)
        //   Spring Boot automaticamente passa uma instância do FilterService
        this.filterService = filterService;
    }

    /**
     * Trata requisições OPTIONS para CORS (Cross-Origin Resource Sharing).
     *
     * Este método permite que o frontend (executado em domínio diferente)
     * faça requisições para este servidor, configurando os headers necessários
     * para permitir origem, métodos e headers customizados.
     *
     * @return Resposta vazia com headers CORS configurados
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")     // ← Permite qualquer origem
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS") // ← Métodos permitidos
                .header("Access-Control-Allow-Headers", "*")    // ← Headers permitidos
                .build();
    }

    /**
     * Lista todas as categorias/filtros disponíveis.
     *
     * Retorna uma lista completa de todas as categorias cadastradas
     * no sistema, que podem ser usadas para organizar os produtos
     * do cardápio (ex: "Bebidas", "Lanches", "Sobremesas").
     *
     * @return Lista de filtros com headers CORS configurados
     */
    @GetMapping
    public ResponseEntity<List<Filter>> getAllFilters() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .body(filterService.getAllFilters());
    }

    /**
     * Cria uma nova categoria/filtro.
     *
     * Recebe os dados de uma nova categoria via JSON no corpo da requisição
     * e a persiste no banco de dados. Validações de negócio são feitas
     * no serviço (FilterService).
     *
     * @param filter Dados da categoria a ser criada (nome obrigatório)
     * @return Categoria criada com status 201, ou erro 400 se inválida
     */
    @PostMapping
    public ResponseEntity<?> createFilter(@RequestBody Filter filter) {
        try {
            Filter created = filterService.createFilter(filter);
            return ResponseEntity.status(HttpStatus.CREATED) // ← Status 201 (Created)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .body(created);
        } catch (IllegalArgumentException e) {
            // ← Captura erros de validação (ex: nome vazio, duplicado)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Remove uma categoria/filtro existente.
     *
     * Exclui permanentemente uma categoria do sistema. Antes de excluir,
     * o serviço verifica se a categoria não está sendo usada por produtos.
     *
     * @param id Identificador único da categoria a ser removida
     * @return Status 204 (No Content) se removida com sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilter(@PathVariable Long id) {
        filterService.deleteFilter(id);
        return ResponseEntity.noContent() // ← Status 204 (No Content)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .build();
    }
}
