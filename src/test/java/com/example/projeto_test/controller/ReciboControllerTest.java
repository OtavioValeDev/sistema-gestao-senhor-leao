package com.example.projeto_test.controller;

import com.example.projeto_test.model.Recibo;
import com.example.projeto_test.service.ReciboService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReciboController.class)
class ReciboControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReciboService reciboService;

    @Autowired
    private ObjectMapper objectMapper;

    private Recibo recibo1;
    private Recibo recibo2;

    @BeforeEach
    void setUp() {
        recibo1 = new Recibo();
        recibo1.setId(1L);
        recibo1.setNumeroChamada("1234");
        recibo1.setDataCriacao(LocalDateTime.now());
        recibo1.setFormaPagamento("DINHEIRO");
        recibo1.setTotal(2000);
        recibo1.setTipoAtendimento("NORMAL");

        Recibo.ItemCompra item1 = new Recibo.ItemCompra("Hambúrguer", 1, 1500);
        Recibo.ItemCompra item2 = new Recibo.ItemCompra("Refrigerante", 1, 500);
        recibo1.setItens(Arrays.asList(item1, item2));

        recibo2 = new Recibo();
        recibo2.setId(2L);
        recibo2.setNumeroChamada("5678");
        recibo2.setDataCriacao(LocalDateTime.now());
        recibo2.setFormaPagamento("CARTAO");
        recibo2.setTotal(3000);
        recibo2.setTipoAtendimento("NORMAL");
    }

    @Test
    void testPagar() throws Exception {
        ReciboController.PagamentoRequest request = new ReciboController.PagamentoRequest(
                Arrays.asList(
                        new Recibo.ItemCompra("Hambúrguer", 1, 1500),
                        new Recibo.ItemCompra("Refrigerante", 1, 500)
                ),
                "Sem cebola",
                "DINHEIRO"
        );

        when(reciboService.createRecibo(any(), any(), any())).thenReturn(recibo1);

        mockMvc.perform(post("/api/recibos/pagar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numeroChamada").value("1234"))
                .andExpect(jsonPath("$.total").value(2000))
                .andExpect(header().exists("Access-Control-Allow-Origin"));

        verify(reciboService, times(1)).createRecibo(any(), any(), any());
    }

    @Test
    void testAtendimentoPreferencial() throws Exception {
        ReciboController.AtendimentoPreferencialRequest request = 
                new ReciboController.AtendimentoPreferencialRequest("Preciso de ajuda");

        Recibo reciboPreferencial = new Recibo();
        reciboPreferencial.setId(3L);
        reciboPreferencial.setNumeroChamada("9999");
        reciboPreferencial.setTipoAtendimento("PREFERENCIAL");
        reciboPreferencial.setFormaPagamento("ATENDIMENTO_PREFERENCIAL");
        reciboPreferencial.setTotal(0);

        when(reciboService.createRecibo(any(), any(), any(), any())).thenReturn(reciboPreferencial);

        mockMvc.perform(post("/api/recibos/atendimento-preferencial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoAtendimento").value("PREFERENCIAL"))
                .andExpect(header().exists("Access-Control-Allow-Origin"));

        verify(reciboService, times(1)).createRecibo(any(), any(), any(), any());
    }

    @Test
    void testListar() throws Exception {
        List<Recibo> recibos = Arrays.asList(recibo1, recibo2);

        when(reciboService.getAllRecibos()).thenReturn(recibos);

        mockMvc.perform(get("/api/recibos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(header().exists("Access-Control-Allow-Origin"));

        verify(reciboService, times(1)).getAllRecibos();
    }

    @Test
    void testBuscarPorId() throws Exception {
        when(reciboService.getReciboById(1L)).thenReturn(recibo1);

        mockMvc.perform(get("/api/recibos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numeroChamada").value("1234"))
                .andExpect(jsonPath("$.total").value(2000))
                .andExpect(header().exists("Access-Control-Allow-Origin"));

        verify(reciboService, times(1)).getReciboById(1L);
    }

    @Test
    void testBuscarPorIdNotFound() throws Exception {
        when(reciboService.getReciboById(999L))
                .thenThrow(new RuntimeException("Recibo not found"));

        mockMvc.perform(get("/api/recibos/999"))
                .andExpect(status().isNotFound())
                .andExpect(header().exists("Access-Control-Allow-Origin"));

        verify(reciboService, times(1)).getReciboById(999L);
    }

    @Test
    void testBuscarPorNumeroChamada() throws Exception {
        when(reciboService.getReciboByNumeroChamada("1234")).thenReturn(recibo1);

        mockMvc.perform(get("/api/recibos/chamada/1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numeroChamada").value("1234"))
                .andExpect(header().exists("Access-Control-Allow-Origin"));

        verify(reciboService, times(1)).getReciboByNumeroChamada("1234");
    }

    @Test
    void testLimpar() throws Exception {
        doNothing().when(reciboService).deleteAllRecibos();

        mockMvc.perform(delete("/api/recibos/limpar"))
                .andExpect(status().isOk())
                .andExpect(content().string("Recibos deletados com sucesso."))
                .andExpect(header().exists("Access-Control-Allow-Origin"));

        verify(reciboService, times(1)).deleteAllRecibos();
    }
}
