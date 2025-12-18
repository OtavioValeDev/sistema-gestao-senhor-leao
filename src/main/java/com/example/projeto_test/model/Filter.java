package com.example.projeto_test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa uma categoria/filtro de produtos.
 *
 * Esta classe define as categorias que podem ser associadas aos produtos
 * do cardápio, permitindo organização e filtragem (ex: "Bebidas", "Lanches", "Sobremesas").
 *
 * Relacionamento:
 * - Many-to-Many com Product: um filtro pode ter vários produtos, um produto pode ter vários filtros
 * - O relacionamento é bidirecional, gerenciado pela entidade Product
 *
 * IMPORTANTE: O campo products é ignorado no JSON (@JsonIgnore) para evitar
 * recursão infinita ou payloads massivos ao serializar filtros.
 *
 * @author Aristocracia do Java
 * @version 1.0
 * @since 2025-01-01
 */
@Entity
@Table(name = "filters")
public class Filter {

    /**
     * Identificador único do filtro no banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    /**
     * Nome da categoria/filtro (obrigatório e único).
     *
     * Exemplos: "Bebidas", "Lanches", "Sobremesas", "Vegetariano"
     * Máximo de 50 caracteres.
     */
    @NotBlank(message = "Name is required")
    @Column(nullable = false, unique = true, length = 50)
    @JsonProperty("name")
    private String name;

    /**
     * Lista de produtos associados a este filtro.
     *
     * Relacionamento Many-to-Many bidirecional com Product.
     * Este campo é ignorado na serialização JSON para evitar recursão.
     */
    @ManyToMany(mappedBy = "filters")
    @JsonIgnore  // Evita recursão infinita na serialização JSON
    private Set<Product> products = new HashSet<>();

    public Filter() {
    }

    public Filter(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // We typically don't expose products in the filter JSON to avoid infinite
    // recursion or massive payloads
    @JsonIgnore
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
