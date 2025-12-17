package com.example.projeto_test.funcionario.controller;

import com.example.projeto_test.funcionario.model.Funcionario;
import com.example.projeto_test.funcionario.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;


    @PostMapping("/create")
    public ResponseEntity<String> adicionarFuncionario(@RequestBody Funcionario funcionario){
        funcionarioService.create(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Funcionario criado!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> findFuncionarioById(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.FOUND).body(funcionarioService.findById(id));
    }

    @GetMapping("list")
    public ResponseEntity <List<Funcionario>> findAllFuncionarios(){
        return ResponseEntity.status(HttpStatus.FOUND).body(funcionarioService.findAll());
    }

    @PutMapping
    public ResponseEntity <Funcionario> atualizarFuncionario(@RequestBody Funcionario funcionario, @RequestParam long id){
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioService.update(funcionario, id));
    }

    @DeleteMapping
    public ResponseEntity<Funcionario> deleteFuncionarioById(@RequestParam long id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
