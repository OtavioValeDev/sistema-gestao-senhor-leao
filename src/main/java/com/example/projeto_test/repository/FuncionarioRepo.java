package com.example.projeto_test.repository;

import com.example.projeto_test.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório para operações de banco de dados da entidade Funcionario.
 * 
 * Estende JpaRepository para fornecer métodos CRUD padrão.
 */
public interface FuncionarioRepo extends JpaRepository<Funcionario, Long> {
}
