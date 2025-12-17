package com.example.projeto_test.funcionario.repository;

import com.example.projeto_test.funcionario.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepo extends JpaRepository<Funcionario,Long> {
}
