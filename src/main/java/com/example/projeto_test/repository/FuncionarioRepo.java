package com.example.projeto_test.repository;

import com.example.projeto_test.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório JPA para a entidade Funcionario.
 *
 * Esta interface fornece métodos para acessar e manipular dados
 * de funcionários no banco de dados. Herda operações CRUD básicas
 * do JpaRepository.
 *
 * Métodos disponíveis automaticamente (herdados de JpaRepository):
 * - save(Funcionario) - Salva ou atualiza funcionário
 * - findById(Long) - Busca por ID
 * - findAll() - Lista todos os funcionários
 * - deleteById(Long) - Remove funcionário por ID
 * - count() - Conta total de funcionários
 *
 * Métodos customizados podem ser adicionados seguindo convenções:
 * - findByEmail(String email) - Busca por email
 * - findByTurno(Turno turno) - Busca por turno
 * - findByNomeContaining(String nome) - Busca parcial por nome
 *
 * @author Aristocracia do Java
 * @version 1.0
 * @since 2025-01-01
 */
public interface FuncionarioRepo extends JpaRepository<Funcionario, Long> {
}
