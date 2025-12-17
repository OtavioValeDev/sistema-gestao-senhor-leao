package com.example.projeto_test.service;

import com.example.projeto_test.model.Product;
import com.example.projeto_test.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product("Hambúrguer", 1500);
        product1.setId(1L);

        product2 = new Product("Refrigerante", 500);
        product2.setId(2L);
    }

    @Test
    void testCreateProduct() {
        Product newProduct = new Product("Batata Frita", 800);
        Product savedProduct = new Product("Batata Frita", 800);
        savedProduct.setId(3L);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.createProduct(newProduct);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Batata Frita", result.getName());
        assertEquals(800, result.getPriceInCents());
        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Hambúrguer", result.get(0).getName());
        assertEquals("Refrigerante", result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Hambúrguer", result.getName());
        assertEquals(1500, result.getPriceInCents());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            productService.getProductById(999L);
        });

        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void testUpdateProduct() {
        Product updatedDetails = new Product("Hambúrguer Grande", 2000);
        Product existingProduct = new Product("Hambúrguer", 1500);
        existingProduct.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return p;
        });

        Product result = productService.updateProduct(1L, updatedDetails);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Hambúrguer Grande", result.getName());
        assertEquals(2000, result.getPriceInCents());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() {
        Product updatedDetails = new Product("Hambúrguer Grande", 2000);

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(999L, updatedDetails);
        });

        verify(productRepository, times(1)).findById(999L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(999L);
        });

        verify(productRepository, times(1)).existsById(999L);
        verify(productRepository, never()).deleteById(any());
    }
}
