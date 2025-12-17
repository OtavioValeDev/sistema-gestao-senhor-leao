package com.example.projeto_test.controller;

import com.example.projeto_test.model.Funcionario;
import com.example.projeto_test.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    /**
     * Cria um novo funcionário.
     */
    @PostMapping
    public ResponseEntity<String> adicionarFuncionario(@RequestBody Funcionario funcionario) {
        funcionarioService.create(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Funcionario criado!");
    }

    /**
     * Busca um funcionário por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> findFuncionarioById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(funcionarioService.findById(id));
    }

    /**
     * Lista todos os funcionários.
     */
    @GetMapping
    public ResponseEntity<List<Funcionario>> findAllFuncionarios() {
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioService.findAll());
    }

    /**
     * Atualiza um funcionário existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizarFuncionario(@RequestBody Funcionario funcionario,
            @PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioService.update(funcionario, id));
    }

    /**
     * Remove um funcionário por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuncionarioById(@PathVariable long id) {
        funcionarioService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
