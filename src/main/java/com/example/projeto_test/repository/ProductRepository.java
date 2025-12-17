package com.example.projeto_test.repository;

import com.example.projeto_test.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório JPA para a entidade Product.
 *
 * Esta interface fornece métodos para acessar e manipular dados
 * de produtos no banco de dados. Herda operações CRUD básicas
 * do JpaRepository e pode ser estendida com consultas customizadas.
 *
 * @author Sistema de Gestão de Restaurante
 * @version 1.0
 * @since 2025-01-01
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Métodos customizados podem ser adicionados aqui seguindo convenções:
    // - findByName(String name) - busca por nome exato
    // - findByNameContaining(String name) - busca por nome parcial
    // - findByPriceInCentsLessThan(Integer price) - busca por preço máximo
    // - findByNameContainingIgnoreCaseOrderByName(String name) - busca ordenada

    // Exemplos (descomentados para uso):
    // List<Product> findByNameContainingIgnoreCase(String name);
    // List<Product> findByPriceInCentsLessThanEqual(Integer maxPrice);
}

