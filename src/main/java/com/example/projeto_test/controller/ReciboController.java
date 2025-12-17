package com.example.projeto_test.controller;

import com.example.projeto_test.model.Recibo;
import com.example.projeto_test.service.ReciboService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações com recibos.
 *
 * Esta classe expõe endpoints REST para gerenciar recibos/notinhas,
 * permitindo criação, consulta e busca por número de chamada.
 *
 * @author Sistema de Gestão de Restaurante
 * @version 1.0
 * @since 2025-01-01
 */
@RestController
@RequestMapping("/api/recibos")
public class ReciboController {

    private final ReciboService reciboService;

    /**
     * Construtor com injeção de dependência.
     *
     * @param reciboService Serviço de lógica de negócio para recibos
     */
    public ReciboController(ReciboService reciboService) {
        this.reciboService = reciboService;
    }

    /**
     * Record para representar uma requisição de pagamento.
     *
     * Utilizado para receber dados do frontend quando um cliente
     * finaliza um pedido e gera um recibo.
     */
    public record PagamentoRequest(
            List<Recibo.ItemCompra> itens,
            String observacoes,
            String formaPagamento) {
    }

    /**
     * Record para representar uma requisição de atendimento preferencial.
     *
     * Utilizado quando um cliente solicita atendimento especial
     * sem fazer pedido de produtos.
     */
    public record AtendimentoPreferencialRequest(
            String observacoes) {
    }

    /**
     * Endpoint para gerar um novo recibo.
     *
     * Recebe os itens do carrinho, observações e forma de pagamento,
     * criando um recibo com número de chamada aleatório.
     *
     * @param req Dados do pagamento e itens comprados
     * @return Recibo criado com status 201 (Created)
     */
    @PostMapping("/pagar")
    public ResponseEntity<Recibo> pagar(@RequestBody PagamentoRequest req) {
        try {
            Recibo recibo = reciboService.createRecibo(req.itens(), req.observacoes(), req.formaPagamento());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .body(recibo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .build();
        }
    }

    /**
     * Endpoint para criar um atendimento preferencial.
     *
     * Cria um recibo especial para atendimento preferencial sem itens,
     * gerando um número de chamada prioritário.
     *
     * @param req Dados do atendimento preferencial
     * @return Recibo de atendimento preferencial com status 201 (Created)
     */
    @PostMapping("/atendimento-preferencial")
    public ResponseEntity<Recibo> atendimentoPreferencial(@RequestBody AtendimentoPreferencialRequest req) {
        try {
            Recibo recibo = reciboService.createRecibo(
                    java.util.Collections.emptyList(), // Lista vazia
                    req.observacoes(),
                    "ATENDIMENTO_PREFERENCIAL",
                    "PREFERENCIAL");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .body(recibo);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .build();
        }
    }

    /**
     * Lista todos os recibos cadastrados.
     *
     * @return Lista completa de recibos com status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Recibo>> listar() {
        List<Recibo> recibos = reciboService.getAllRecibos();
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .body(recibos);
    }

    /**
     * Busca um recibo específico pelo ID.
     *
     * @param id Identificador único do recibo
     * @return Recibo encontrado ou 404 se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<Recibo> buscarPorId(@PathVariable @NonNull Long id) {
        try {
            Recibo recibo = reciboService.getReciboById(id);
            return ResponseEntity.ok()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .body(recibo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .build();
        }
    }

    /**
     * Atualiza um recibo existente.
     *
     * @param id     ID do recibo
     * @param recibo Dados para atualização
     * @return Recibo atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Recibo> atualizar(@PathVariable Long id, @RequestBody Recibo recibo) {
        try {
            Recibo atualizado = reciboService.updateRecibo(id, recibo);
            return ResponseEntity.ok()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .body(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .build();
        }
    }

    /**
     * Busca um recibo pelo número de chamada (notinha).
     *
     * Permite que clientes consultem seus recibos usando
     * o número impresso na notinha.
     *
     * @param numero Número de chamada de 4 dígitos
     * @return Recibo encontrado ou 404 se não existir
     */
    @GetMapping("/chamada/{numero}")
    public ResponseEntity<Recibo> buscarPorNumeroChamada(@PathVariable String numero) {
        try {
            Recibo recibo = reciboService.getReciboByNumeroChamada(numero);
            return ResponseEntity.ok()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .body(recibo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .build();
        }
    }

    /**
     * Remove um recibo específico do sistema.
     *
     * @param id Identificador único do recibo
     * @return 204 No Content se sucesso, ou 404 se não encontrado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            reciboService.deleteRecibo(id);
            return ResponseEntity.noContent()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .build();
        }
    }

    /**
     * Remove todos os recibos do sistema.
     *
     * Endpoint utilitário para desenvolvimento/testes.
     *
     * @return Mensagem de confirmação
     */
    @DeleteMapping("/limpar")
    public ResponseEntity<String> limpar() {
        reciboService.deleteAllRecibos();
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .body("Recibos deletados com sucesso.");
    }

    /**
     * Endpoint para lidar com requisições OPTIONS (preflight CORS).
     *
     * @return Resposta vazia com headers CORS
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .build();
    }
}