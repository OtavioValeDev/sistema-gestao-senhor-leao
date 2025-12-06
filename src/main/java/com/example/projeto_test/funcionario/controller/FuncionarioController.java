package com.example.projeto_test.funcionario.controller;

import com.example.projeto_test.funcionario.model.Funcionario;
import com.example.projeto_test.funcionario.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok().body("Funcionario criado!");
    }

    @GetMapping("/{id}")
    public Funcionario findFuncionarioById(@PathVariable long id){
        return funcionarioService.findById(id);
    }

    @GetMapping("list")
    public List<Funcionario> findAllFuncionarios(){
        return funcionarioService.findAll();
    }

    @PutMapping
    public Funcionario atualizarFuncionario(@RequestBody Funcionario funcionario, @RequestParam long id){
        return funcionarioService.update(funcionario, id);
    }

    @DeleteMapping
    public void deleteFuncionarioById(@RequestParam long id){
        funcionarioService.deleteById(id);
    }
}
