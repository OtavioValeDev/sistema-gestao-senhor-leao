package com.example.projeto_test.funcionario.service;

import com.example.projeto_test.funcionario.exceptions.EmailAlreadyRegisteredException;
import com.example.projeto_test.funcionario.model.Funcionario;
import com.example.projeto_test.funcionario.repository.FuncionarioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepo funcionarioRepo;

    public void create(Funcionario funcionario){
        try{
        funcionarioRepo.save(funcionario);
        }catch(DataIntegrityViolationException e){
            throw new EmailAlreadyRegisteredException("Email already registered");
        }
    }

    public Funcionario findById(long id){
        return funcionarioRepo.findById(id).orElseThrow(()
                -> new RuntimeException("Employee Not Found"));
    }

    public List<Funcionario> findAll(){
        return funcionarioRepo.findAll();
    }

    public Funcionario update(Funcionario funcionario, long id){
        Funcionario funcionarioAntigo = findById(id);
        Funcionario funcionarioAtualizado = Funcionario.builder()
                .nome(funcionario.getNome() != null ?
                        funcionario.getNome() : funcionarioAntigo.getNome())
                .email(funcionario.getEmail() != null ?
                        funcionario.getEmail() : funcionarioAntigo.getEmail())
                .turno(funcionario.getTurno() != null ?
                        funcionario.getTurno() : funcionarioAntigo.getTurno())
                .senha(funcionario.getSenha() != null ?
                        funcionario.getSenha() : funcionarioAntigo.getSenha())
                .salario(funcionario.getSalario() != 0 ?
                        funcionario.getSalario() : funcionarioAntigo.getSalario())
                .id(funcionarioAntigo.getId())
                .build();
        return funcionarioRepo.save(funcionarioAtualizado);
    }

    public void deleteById(long id){
        funcionarioRepo.deleteById(id);
    }

}
