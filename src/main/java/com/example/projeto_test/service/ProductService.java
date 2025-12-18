package com.example.projeto_test.service;

import com.example.projeto_test.model.Filter;
import com.example.projeto_test.model.Product;
import com.example.projeto_test.repository.FilterRepository;
import com.example.projeto_test.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Serviço de negócio para operações com produtos.
 *
 * Esta classe contém toda a lógica de negócio relacionada aos produtos
 * do cardápio, incluindo validações e regras específicas do domínio.
 * Atua como intermediário entre o controller e o repository.
 *
 * @author Sistema de Gestão de Restaurante
 * @version 1.0
 * @since 2025-01-01
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FilterRepository filterRepository;

    /**
     * Cria um novo produto no cardápio.
     *
     * Este método processa e valida os dados antes de persistir no banco.
     * A lógica principal está no processamento de filtros/categorias:
     * o frontend envia apenas IDs de filtros, mas o banco precisa dos
     * objetos completos para estabelecer o relacionamento.
     *
     * Lógica de negócio:
     * 1. Valida se o produto tem filtros associados
     * 2. Para cada filtro recebido (apenas com ID):
     *    - Busca o objeto Filter completo no banco
     *    - Valida se o filtro existe (lança exceção se não)
     *    - Adiciona à lista de filtros processados
     * 3. Substitui a lista de filtros do produto pelos objetos completos
     * 4. Persiste o produto no banco (com relacionamentos corretos)
     *
     * IMPORTANTE: Esta etapa é necessária porque o frontend envia apenas
     * IDs, mas JPA precisa dos objetos completos para criar o relacionamento
     * Many-to-Many entre Product e Filter.
     *
     * @param product Produto a ser criado (deve ter nome e preço válidos)
     * @return Produto criado com ID gerado e filtros associados
     * @throws RuntimeException Se algum filtro informado não existir
     */
    public Product createProduct(@NonNull Product product) {
        // Processar filtros: converter IDs em objetos completos do banco
        // O frontend envia apenas IDs, mas JPA precisa dos objetos completos
        if (product.getFilters() != null && !product.getFilters().isEmpty()) {
            Set<Filter> processedFilters = new HashSet<>();
            
            // Para cada filtro recebido (contendo apenas ID)
            for (Filter filter : product.getFilters()) {
                Long filterId = filter.getId();
                if (filterId != null) {
                    // Buscar o objeto Filter completo no banco
                    Filter fullFilter = filterRepository.findById(filterId)
                            .orElseThrow(() -> new RuntimeException("Filter not found with id: " + filterId));
                    processedFilters.add(fullFilter);
                }
            }
            
            // Substituir lista de filtros por objetos completos
            product.setFilters(processedFilters);
        }
        
        // Persistir produto no banco (com relacionamentos corretos)
        return productRepository.save(product);
    }

    /**
     * Retorna todos os produtos do cardápio.
     *
     * @return Lista completa de produtos disponíveis
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Busca um produto específico pelo ID.
     *
     * @param id Identificador único do produto
     * @return Produto encontrado
     * @throws RuntimeException Se o produto não for encontrado
     */
    public Product getProductById(@NonNull Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    /**
     * Atualiza os dados de um produto existente.
     *
     * Este método permite modificar nome, preço, imagem e categorias de um produto.
     * Implementa lógica similar ao createProduct para processar filtros corretamente.
     *
     * Lógica de negócio:
     * 1. Busca o produto existente no banco
     * 2. Atualiza nome e preço (sempre atualizados)
     * 3. Atualiza imagem se fornecida (opcional)
     * 4. Processa filtros se fornecidos:
     *    - Converte IDs em objetos Filter completos
     *    - Valida existência de cada filtro
     *    - Substitui lista de filtros do produto
     * 5. Persiste alterações no banco
     *
     * IMPORTANTE: A imagem é opcional na atualização. Se não for fornecida,
     * a imagem atual é mantida. Para remover imagem, envie null explicitamente.
     *
     * @param id ID do produto a ser atualizado
     * @param productDetails Novos dados do produto (nome e preço obrigatórios)
     * @return Produto atualizado com novos dados
     * @throws RuntimeException Se o produto ou algum filtro não for encontrado
     */
    public Product updateProduct(@NonNull Long id, @NonNull Product productDetails) {
        // Buscar produto existente (lança exceção se não encontrado)
        Product product = getProductById(id);
        
        // Atualizar campos obrigatórios
        product.setName(productDetails.getName());
        product.setPriceInCents(productDetails.getPriceInCents());
        
        // Atualizar imagem se fornecida (opcional - mantém atual se null)
        if (productDetails.getImageUrl() != null) {
            product.setImageUrl(productDetails.getImageUrl());
        }
        
        // Processar filtros: converter IDs em objetos completos (mesma lógica do create)
        if (productDetails.getFilters() != null) {
            Set<Filter> processedFilters = new HashSet<>();
            for (Filter filter : productDetails.getFilters()) {
                Long filterId = filter.getId();
                if (filterId != null) {
                    Filter fullFilter = filterRepository.findById(filterId)
                            .orElseThrow(() -> new RuntimeException("Filter not found with id: " + filterId));
                    processedFilters.add(fullFilter);
                }
            }
            product.setFilters(processedFilters);
        }
        
        return productRepository.save(product);
    }

    /**
     * Remove um produto do cardápio.
     *
     * @param id ID do produto a ser removido
     * @throws RuntimeException Se o produto não for encontrado
     */
    public void deleteProduct(@NonNull Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }
}

