package com.example.projeto_test.service;

import com.example.projeto_test.model.Filter;
import com.example.projeto_test.repository.FilterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço de negócio para operações com filtros/categorias.
 *
 * Esta classe contém a lógica de negócio relacionada aos filtros,
 * incluindo validações de duplicatas e regras específicas do domínio.
 * Atua como intermediário entre o controller e o repository.
 *
 * @author Aristocracia do Java
 * @version 1.0
 * @since 2025-01-01
 */
@Service
public class FilterService {

    private final FilterRepository filterRepository;

    public FilterService(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }

    /**
     * Retorna todas as categorias/filtros cadastrados.
     *
     * @return Lista completa de filtros
     */
    public List<Filter> getAllFilters() {
        return filterRepository.findAll();
    }

    /**
     * Cria uma nova categoria/filtro.
     *
     * Valida se já existe um filtro com o mesmo nome antes de criar,
     * evitando duplicatas. O nome deve ser único no sistema.
     *
     * Lógica de negócio:
     * 1. Verifica se já existe filtro com o mesmo nome
     * 2. Se existir, lança exceção IllegalArgumentException
     * 3. Se não existir, persiste o novo filtro
     *
     * @param filter Filtro a ser criado (deve ter nome válido)
     * @return Filtro criado com ID gerado
     * @throws IllegalArgumentException Se já existir filtro com o mesmo nome
     */
    public Filter createFilter(Filter filter) {
        // Validação de duplicata: verificar se nome já existe
        // Isso evita criar categorias duplicadas mesmo que a constraint do banco também valide
        Optional<Filter> existing = filterRepository.findByName(filter.getName());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Filter with this name already exists");
        }
        return filterRepository.save(filter);
    }

    /**
     * Remove uma categoria/filtro do sistema.
     *
     * IMPORTANTE: Ao remover um filtro, os produtos associados não são
     * automaticamente atualizados. Considere implementar lógica para
     * remover associações antes de deletar o filtro.
     *
     * @param id Identificador único do filtro a ser removido
     */
    public void deleteFilter(Long id) {
        filterRepository.deleteById(id);
    }

    /**
     * Busca um filtro específico pelo ID.
     *
     * @param id Identificador único do filtro
     * @return Filtro encontrado
     * @throws RuntimeException Se o filtro não for encontrado
     */
    public Filter getFilterById(Long id) {
        return filterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filter not found"));
    }
}
