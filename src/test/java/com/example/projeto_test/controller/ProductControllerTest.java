package com.example.projeto_test.controller;

import com.example.projeto_test.model.Product;
import com.example.projeto_test.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testCreateProduct() throws Exception {
        Product newProduct = new Product("Batata Frita", 800);
        Product savedProduct = new Product("Batata Frita", 800);
        savedProduct.setId(3L);

        when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Batata Frita"))
                .andExpect(jsonPath("$.priceInCents").value(800))
                .andExpect(header().exists("Access-Control-Allow-Origin"));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Hambúrguer"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Refrigerante"))
                .andExpect(header().exists("Access-Control-Allow-Origin"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(product1);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Hambúrguer"))
                .andExpect(jsonPath("$.priceInCents").value(1500));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product updatedProduct = new Product("Hambúrguer Grande", 2000);
        updatedProduct.setId(1L);

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Hambúrguer Grande"))
                .andExpect(jsonPath("$.priceInCents").value(2000))
                .andExpect(header().exists("Access-Control-Allow-Origin"));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Access-Control-Allow-Origin"));

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void testHandleOptions() throws Exception {
        mockMvc.perform(options("/api/products"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"));
    }

    @Test
    void testSolicitarAtendimentoPreferencial() throws Exception {
        mockMvc.perform(post("/api/products/notificacao-preferencial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.notificationId").exists())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }

    @Test
    void testGetNotificacoesPendentes() throws Exception {
        mockMvc.perform(get("/api/products/notificacoes-pendentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }
}
