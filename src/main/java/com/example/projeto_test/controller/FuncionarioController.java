package com.example.projeto_test.controller;
// ↑ Declara que este arquivo pertence ao pacote de controllers
//   Controllers são responsáveis por receber requisições HTTP e devolver respostas

import com.example.projeto_test.model.Funcionario;
// ↑ Importa a entidade Funcionario (representa um funcionário do restaurante)
import com.example.projeto_test.service.FuncionarioService;
// ↑ Importa o serviço que contém a lógica de negócio dos funcionários
import lombok.RequiredArgsConstructor;
// ↑ Anotação Lombok que gera automaticamente construtor com todos os campos finais
import org.springframework.http.HttpStatus;
// ↑ Importa enum com códigos de status HTTP (200, 201, 404, etc.)
import org.springframework.http.ResponseEntity;
// ↑ Importa classe para criar respostas HTTP estruturadas
import org.springframework.web.bind.annotation.*;
// ↑ Importa todas as anotações para mapeamento de endpoints REST

import java.util.List;
// ↑ Importa interface List para trabalhar com coleções

/**
 * Controller REST para operações com funcionários do restaurante.
 *
 * Esta classe expõe endpoints HTTP para:
 * - Criar novos funcionários
 * - Listar funcionários existentes
 * - Buscar funcionário por ID
 * - Atualizar dados de funcionários
 * - Remover funcionários do sistema
 *
 * Gerencia informações como nome, email, turno e salário dos colaboradores.
 *
 * @author Aristocracia do Java
 * @version 1.0
 * @since 2025-01-01
 */
@RestController // ← Anotação que marca esta classe como um controller REST
//   - Todas as respostas são automaticamente convertidas para JSON
@RequiredArgsConstructor // ← Lombok: gera construtor com parâmetros para campos finais
@RequestMapping("/api/funcionarios") // ← Define a URL base para todos os endpoints desta classe
//   - Todos os endpoints começarão com /api/funcionarios
public class FuncionarioController {

    private final FuncionarioService funcionarioService;
    // ↑ Campo que armazena a referência para o serviço de funcionários
    //   final = não pode ser alterado após inicialização
    //   @RequiredArgsConstructor gera o construtor automaticamente

    /**
     * Cria um novo funcionário no sistema.
     *
     * Recebe os dados de um novo funcionário via JSON e o registra
     * no banco de dados. O email deve ser único no sistema.
     *
     * @param funcionario Dados do funcionário (nome, email, senha, turno, salário)
     * @return Mensagem de confirmação com status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<String> adicionarFuncionario(@RequestBody Funcionario funcionario) {
        funcionarioService.create(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED) // ← Status 201 (Created)
                .body("Funcionario criado!");
    }

    /**
     * Busca um funcionário específico por ID.
     *
     * Retorna todos os dados de um funcionário identificado pelo ID único.
     * Se o funcionário não existir, retorna erro 404 (Not Found).
     *
     * @param id Identificador único do funcionário
     * @return Dados completos do funcionário encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> findFuncionarioById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK) // ← Status 200 (OK)
                .body(funcionarioService.findById(id));
    }

    /**
     * Lista todos os funcionários cadastrados.
     *
     * Retorna uma lista completa com todos os funcionários
     * registrados no sistema, incluindo seus dados pessoais
     * e contratuais (nome, email, turno, salário).
     *
     * @return Lista completa de funcionários
     */
    @GetMapping
    public ResponseEntity<List<Funcionario>> findAllFuncionarios() {
        return ResponseEntity.status(HttpStatus.OK) // ← Status 200 (OK)
                .body(funcionarioService.findAll());
    }

    /**
     * Atualiza os dados de um funcionário existente.
     *
     * Recebe dados atualizados de um funcionário e substitui
     * as informações existentes no banco de dados. Apenas os
     * campos enviados são atualizados (merge inteligente).
     *
     * @param funcionario Dados atualizados do funcionário
     * @param id Identificador único do funcionário a ser atualizado
     * @return Dados do funcionário após atualização
     */
    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizarFuncionario(@RequestBody Funcionario funcionario,
            @PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK) // ← Status 200 (OK)
                .body(funcionarioService.update(funcionario, id));
    }

    /**
     * Remove um funcionário do sistema.
     *
     * Exclui permanentemente um funcionário do banco de dados.
     * Esta operação não pode ser desfeita.
     *
     * @param id Identificador único do funcionário a ser removido
     * @return Status 204 (No Content) confirmando a exclusão
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuncionarioById(@PathVariable long id) {
        funcionarioService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT) // ← Status 204 (No Content)
                .build();
    }
}
