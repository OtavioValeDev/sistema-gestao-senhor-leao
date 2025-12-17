package com.example.projeto_test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa um produto do cardápio do restaurante.
 *
 * Esta classe define os produtos disponíveis para venda, contendo
 * informações básicas como nome e preço. O preço é armazenado
 * em centavos para evitar problemas de arredondamento com decimais.
 *
 * Exemplos de produtos: Hambúrguer, Refrigerante, Batata Frita, etc.
 *
 * @author Sistema de Gestão de Restaurante
 * @version 1.0
 * @since 2025-01-01
 */
@Entity
@Table(name = "products")
public class Product {

    /** Identificador único do produto no banco de dados */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    /**
     * Nome do produto (obrigatório).
     * Deve ser único e descritivo (ex: "Hambúrguer", "Refrigerante 350ml").
     */
    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 100)
    @JsonProperty("name")
    private String name;

    /**
     * Preço do produto em centavos (obrigatório).
     * Exemplo: R$ 15,00 = 1500 centavos
     * Isso evita problemas de arredondamento com números decimais.
     */
    @Positive(message = "Price must be positive")
    @Column(nullable = false)
    @JsonProperty("priceInCents")
    private Integer priceInCents;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_filters", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "filter_id"))
    @JsonProperty("filters")
    private Set<Filter> filters = new HashSet<>();

    /**
     * URL ou caminho da imagem do produto (opcional).
     * Pode ser uma URL externa ou caminho relativo para imagem local.
     */
    @Column(length = 500)
    @JsonProperty("imageUrl")
    private String imageUrl;

    /** Construtor padrão necessário para JPA */
    public Product() {
    }

    /**
     * Construtor completo para criar um produto.
     *
     * @param name         Nome do produto
     * @param priceInCents Preço em centavos
     */
    public Product(String name, Integer priceInCents) {
        this.name = name;
        this.priceInCents = priceInCents;
    }

    // ===== GETTERS E SETTERS =====

    /** @return Identificador único do produto */
    public Long getId() {
        return id;
    }

    /** @param id Identificador único do produto */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return Nome do produto */
    public String getName() {
        return name;
    }

    /** @param name Nome do produto */
    public void setName(String name) {
        this.name = name;
    }

    /** @return Preço em centavos */
    public Integer getPriceInCents() {
        return priceInCents;
    }

    /** @param priceInCents Preço em centavos */
    public void setPriceInCents(Integer priceInCents) {
        this.priceInCents = priceInCents;
    }

    public Set<Filter> getFilters() {
        return filters;
    }

    public void setFilters(Set<Filter> filters) {
        this.filters = filters;
    }

    /** @return URL da imagem do produto */
    public String getImageUrl() {
        return imageUrl;
    }

    /** @param imageUrl URL da imagem do produto */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}