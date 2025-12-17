package com.example.projeto_test.service;

import com.example.projeto_test.model.Filter;
import com.example.projeto_test.repository.FilterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilterService {

    private final FilterRepository filterRepository;

    public FilterService(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }

    public List<Filter> getAllFilters() {
        return filterRepository.findAll();
    }

    public Filter createFilter(Filter filter) {
        // Simple check to avoid duplicates if not handled by DB constraint gracefully
        Optional<Filter> existing = filterRepository.findByName(filter.getName());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Filter with this name already exists");
        }
        return filterRepository.save(filter);
    }

    public void deleteFilter(Long id) {
        filterRepository.deleteById(id);
    }

    public Filter getFilterById(Long id) {
        return filterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filter not found"));
    }
}
