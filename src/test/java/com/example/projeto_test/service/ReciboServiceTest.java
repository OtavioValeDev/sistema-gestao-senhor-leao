package com.example.projeto_test.service;

import com.example.projeto_test.model.Recibo;
import com.example.projeto_test.repository.ReciboRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class ReciboServiceTest {

    @Mock
    private ReciboRepository reciboRepository;

    @InjectMocks
    private ReciboService reciboService;

    private List<Recibo.ItemCompra> itens;

    @BeforeEach
    void setUp() {
        itens = Arrays.asList(
                new Recibo.ItemCompra("Hambúrguer", 1, 1500),
                new Recibo.ItemCompra("Refrigerante", 1, 500)
        );
    }

    @Test
    void testCreateRecibo() {
        Recibo recibo = new Recibo();
        recibo.setId(1L);
        recibo.setNumeroChamada("1234");
        recibo.setTotal(2000);

        when(reciboRepository.save(any(Recibo.class))).thenAnswer(invocation -> {
            Recibo r = invocation.getArgument(0);
            r.setId(1L);
            return r;
        });

        Recibo result = reciboService.createRecibo(itens, "Sem cebola", "DINHEIRO");

        assertNotNull(result);
        assertNotNull(result.getNumeroChamada());
        assertEquals(4, result.getNumeroChamada().length());
        assertEquals(2000, result.getTotal());
        assertEquals("DINHEIRO", result.getFormaPagamento());
        assertEquals("Sem cebola", result.getObservacoes());
        assertEquals("NORMAL", result.getTipoAtendimento());
        assertEquals(2, result.getItens().size());
        verify(reciboRepository, times(1)).save(any(Recibo.class));
    }

    @Test
    void testCreateReciboWithTipoAtendimento() {
        Recibo recibo = new Recibo();
        recibo.setId(2L);
        recibo.setNumeroChamada("5678");
        recibo.setTotal(2000);

        when(reciboRepository.save(any(Recibo.class))).thenAnswer(invocation -> {
            Recibo r = invocation.getArgument(0);
            r.setId(2L);
            return r;
        });

        Recibo result = reciboService.createRecibo(itens, "Sem cebola", "DINHEIRO", "PREFERENCIAL");

        assertNotNull(result);
        assertEquals("PREFERENCIAL", result.getTipoAtendimento());
        verify(reciboRepository, times(1)).save(any(Recibo.class));
    }

    @Test
    void testCreateReciboWithEmptyCart() {
        assertThrows(IllegalArgumentException.class, () -> {
            reciboService.createRecibo(Arrays.asList(), "Observação", "DINHEIRO");
        });

        verify(reciboRepository, never()).save(any(Recibo.class));
    }

    @Test
    void testCreateReciboPreferencialWithEmptyCart() {
        Recibo recibo = new Recibo();
        recibo.setId(3L);
        recibo.setNumeroChamada("9999");
        recibo.setTotal(0);

        when(reciboRepository.save(any(Recibo.class))).thenAnswer(invocation -> {
            Recibo r = invocation.getArgument(0);
            r.setId(3L);
            return r;
        });

        // Atendimento preferencial permite carrinho vazio
        Recibo result = reciboService.createRecibo(
                Arrays.asList(), 
                "Preciso de ajuda", 
                "ATENDIMENTO_PREFERENCIAL", 
                "PREFERENCIAL"
        );

        assertNotNull(result);
        assertEquals("PREFERENCIAL", result.getTipoAtendimento());
        assertEquals(0, result.getTotal());
        verify(reciboRepository, times(1)).save(any(Recibo.class));
    }

    @Test
    void testGetAllRecibos() {
        Recibo recibo1 = new Recibo();
        recibo1.setId(1L);
        recibo1.setNumeroChamada("1234");

        Recibo recibo2 = new Recibo();
        recibo2.setId(2L);
        recibo2.setNumeroChamada("5678");

        List<Recibo> recibos = Arrays.asList(recibo1, recibo2);

        when(reciboRepository.findAll()).thenReturn(recibos);

        List<Recibo> result = reciboService.getAllRecibos();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1234", result.get(0).getNumeroChamada());
        assertEquals("5678", result.get(1).getNumeroChamada());
        verify(reciboRepository, times(1)).findAll();
    }

    @Test
    void testGetReciboById() {
        Recibo recibo = new Recibo();
        recibo.setId(1L);
        recibo.setNumeroChamada("1234");
        recibo.setTotal(2000);

        when(reciboRepository.findById(1L)).thenReturn(Optional.of(recibo));

        Recibo result = reciboService.getReciboById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("1234", result.getNumeroChamada());
        assertEquals(2000, result.getTotal());
        verify(reciboRepository, times(1)).findById(1L);
    }

    @Test
    void testGetReciboByIdNotFound() {
        when(reciboRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            reciboService.getReciboById(999L);
        });

        verify(reciboRepository, times(1)).findById(999L);
    }

    @Test
    void testGetReciboByNumeroChamada() {
        Recibo recibo = new Recibo();
        recibo.setId(1L);
        recibo.setNumeroChamada("1234");
        recibo.setTotal(2000);

        when(reciboRepository.findByNumeroChamada("1234")).thenReturn(Optional.of(recibo));

        Recibo result = reciboService.getReciboByNumeroChamada("1234");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("1234", result.getNumeroChamada());
        verify(reciboRepository, times(1)).findByNumeroChamada("1234");
    }

    @Test
    void testGetReciboByNumeroChamadaNotFound() {
        when(reciboRepository.findByNumeroChamada("9999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            reciboService.getReciboByNumeroChamada("9999");
        });

        verify(reciboRepository, times(1)).findByNumeroChamada("9999");
    }

    @Test
    void testDeleteAllRecibos() {
        doNothing().when(reciboRepository).deleteAll();

        reciboService.deleteAllRecibos();

        verify(reciboRepository, times(1)).deleteAll();
    }

    @Test
    void testCreateReciboCalculatesTotalCorrectly() {
        List<Recibo.ItemCompra> itens = Arrays.asList(
                new Recibo.ItemCompra("Hambúrguer", 2, 1500), // 3000
                new Recibo.ItemCompra("Refrigerante", 3, 500)  // 1500
        );

        when(reciboRepository.save(any(Recibo.class))).thenAnswer(invocation -> {
            Recibo r = invocation.getArgument(0);
            r.setId(1L);
            return r;
        });

        Recibo result = reciboService.createRecibo(itens, "", "DINHEIRO");

        assertNotNull(result);
        assertEquals(4500, result.getTotal()); // 3000 + 1500
        verify(reciboRepository, times(1)).save(any(Recibo.class));
    }
}
