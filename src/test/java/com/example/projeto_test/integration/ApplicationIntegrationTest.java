package com.example.projeto_test.integration;

import com.example.projeto_test.model.Product;
import com.example.projeto_test.model.Recibo;
import com.example.projeto_test.repository.ProductRepository;
import com.example.projeto_test.repository.ReciboRepository;
import com.example.projeto_test.service.ProductService;
import com.example.projeto_test.service.ReciboService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ApplicationIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReciboService reciboService;

    @Autowired
    private ReciboRepository reciboRepository;

    @BeforeEach
    void setUp() {
        reciboRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void testCompleteFlow_CreateProductAndGenerateRecibo() {
        // 1. Criar produtos
        Product hamburguer = new Product("Hambúrguer", 1500);
        Product refrigerante = new Product("Refrigerante", 500);
        
        Product savedHamburguer = productService.createProduct(hamburguer);
        Product savedRefrigerante = productService.createProduct(refrigerante);

        assertNotNull(savedHamburguer.getId());
        assertNotNull(savedRefrigerante.getId());
        assertEquals("Hambúrguer", savedHamburguer.getName());
        assertEquals("Refrigerante", savedRefrigerante.getName());

        // 2. Buscar produtos
        List<Product> allProducts = productService.getAllProducts();
        assertEquals(2, allProducts.size());

        // 3. Criar recibo com os produtos
        List<Recibo.ItemCompra> itens = Arrays.asList(
                new Recibo.ItemCompra(savedHamburguer.getName(), 2, savedHamburguer.getPriceInCents()),
                new Recibo.ItemCompra(savedRefrigerante.getName(), 1, savedRefrigerante.getPriceInCents())
        );

        Recibo recibo = reciboService.createRecibo(itens, "Sem cebola", "DINHEIRO");

        assertNotNull(recibo.getId());
        assertNotNull(recibo.getNumeroChamada());
        assertEquals(4, recibo.getNumeroChamada().length());
        assertEquals(3500, recibo.getTotal()); // (2 * 1500) + (1 * 500) = 3500
        assertEquals("DINHEIRO", recibo.getFormaPagamento());
        assertEquals("Sem cebola", recibo.getObservacoes());
        assertEquals(2, recibo.getItens().size());

        // 4. Buscar recibo por número de chamada
        Recibo foundRecibo = reciboService.getReciboByNumeroChamada(recibo.getNumeroChamada());
        assertNotNull(foundRecibo);
        assertEquals(recibo.getId(), foundRecibo.getId());
        assertEquals(recibo.getTotal(), foundRecibo.getTotal());

        // 5. Listar todos os recibos
        List<Recibo> allRecibos = reciboService.getAllRecibos();
        assertEquals(1, allRecibos.size());
    }

    @Test
    void testProductCRUD_CompleteCycle() {
        // Create
        Product product = new Product("Batata Frita", 800);
        Product created = productService.createProduct(product);
        assertNotNull(created.getId());
        assertEquals("Batata Frita", created.getName());
        assertEquals(800, created.getPriceInCents());

        // Read
        Product found = productService.getProductById(created.getId());
        assertEquals(created.getId(), found.getId());
        assertEquals("Batata Frita", found.getName());

        // Update
        Product updated = new Product("Batata Frita Grande", 1200);
        Product result = productService.updateProduct(created.getId(), updated);
        assertEquals(created.getId(), result.getId());
        assertEquals("Batata Frita Grande", result.getName());
        assertEquals(1200, result.getPriceInCents());

        // Delete
        productService.deleteProduct(created.getId());
        assertThrows(RuntimeException.class, () -> {
            productService.getProductById(created.getId());
        });
    }

    @Test
    void testReciboPreferencial_WithoutItems() {
        // Criar recibo de atendimento preferencial sem itens
        Recibo recibo = reciboService.createRecibo(
                Arrays.asList(),
                "Preciso de ajuda para escolher",
                "ATENDIMENTO_PREFERENCIAL",
                "PREFERENCIAL"
        );

        assertNotNull(recibo.getId());
        assertEquals("PREFERENCIAL", recibo.getTipoAtendimento());
        assertEquals(0, recibo.getTotal());
        assertEquals(0, recibo.getItens().size());
        assertNotNull(recibo.getNumeroChamada());
    }

    @Test
    void testMultipleRecibos_UniqueNumbers() {
        List<Recibo.ItemCompra> itens = Arrays.asList(
                new Recibo.ItemCompra("Hambúrguer", 1, 1500)
        );

        Recibo recibo1 = reciboService.createRecibo(itens, "", "DINHEIRO");
        Recibo recibo2 = reciboService.createRecibo(itens, "", "CARTAO");
        Recibo recibo3 = reciboService.createRecibo(itens, "", "PIX");

        assertNotEquals(recibo1.getNumeroChamada(), recibo2.getNumeroChamada());
        assertNotEquals(recibo2.getNumeroChamada(), recibo3.getNumeroChamada());
        assertNotEquals(recibo1.getNumeroChamada(), recibo3.getNumeroChamada());

        List<Recibo> allRecibos = reciboService.getAllRecibos();
        assertEquals(3, allRecibos.size());
    }

    @Test
    void testReciboCalculation_MultipleItems() {
        List<Recibo.ItemCompra> itens = Arrays.asList(
                new Recibo.ItemCompra("Hambúrguer", 3, 1500),      // 4500
                new Recibo.ItemCompra("Refrigerante", 2, 500),    // 1000
                new Recibo.ItemCompra("Batata Frita", 1, 800)     // 800
        );

        Recibo recibo = reciboService.createRecibo(itens, "", "DINHEIRO");

        assertEquals(6300, recibo.getTotal()); // 4500 + 1000 + 800
        assertEquals(3, recibo.getItens().size());
    }
}
