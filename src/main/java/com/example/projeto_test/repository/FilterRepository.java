package com.example.projeto_test.repository;

import com.example.projeto_test.model.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório JPA para a entidade Filter.
 *
 * Esta interface fornece métodos para acessar e manipular dados
 * de filtros/categorias no banco de dados. Herda operações CRUD básicas
 * do JpaRepository e adiciona consultas customizadas.
 *
 * @author Aristocracia do Java
 * @version 1.0
 * @since 2025-01-01
 */
@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {
    
    /**
     * Busca um filtro pelo nome exato.
     *
     * Este método é útil para validar se uma categoria já existe
     * antes de criar uma nova, evitando duplicatas.
     *
     * @param name Nome do filtro a ser buscado (case-sensitive)
     * @return Optional contendo o filtro se encontrado, vazio caso contrário
     */
    Optional<Filter> findByName(String name);
}
