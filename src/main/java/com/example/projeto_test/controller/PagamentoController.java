package com.example.projeto_test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller REST para gerenciamento de pagamentos via PIX.
 *
 * Esta classe expõe endpoints HTTP para:
 * - Criar transações PIX simuladas
 * - Verificar status de pagamentos
 * - Confirmar pagamentos manualmente (modo de teste)
 *
 * IMPORTANTE: Este é um sistema SIMULADO para demonstração e testes.
 * Em produção, deve ser substituído por integração real com gateway de pagamento
 * (Mercado Pago, PagSeguro, ou API oficial do banco).
 *
 * O sistema atual:
 * - Gera QR Codes estáticos (não funcionais)
 * - Armazena transações em memória (perdidas ao reiniciar)
 * - Permite simulação de aprovação via endpoint de confirmação
 *
 * @author Aristocracia do Java
 * @version 1.0
 * @since 2025-01-01
 */
@RestController
@RequestMapping("/api/pagamento")
public class PagamentoController {

    /**
     * Armazena o status das transações em memória (simula banco de dados).
     *
     * Estrutura: Map<transactionId, status>
     * Status possíveis: "pending", "approved", "unknown"
     *
     * NOTA: Em produção, isso deve ser substituído por banco de dados persistente.
     */
    private final Map<String, String> transacoes = new ConcurrentHashMap<>();

    /**
     * Imagem de QR Code genérico em Base64 (placeholder para demonstração).
     *
     * Este é um QR Code estático que não funciona para pagamento real.
     * Em produção, o QR Code deve ser gerado pela API do gateway de pagamento.
     */
    private final String MOCK_QR_CODE_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAAJQAAACUCAQAAAB8N04MAAAAyklEQVR42u3TQQrAMAwDwX7J/39LTw70IAwEmx72Ea1Gg6v5d8T+I/Yfsf+I/UfsP2L/EfuP2H/E/iP2H7H/iP1H7D9i/xH7j9h+x/4j9R+w/Yv8R+4/Yf8T+I/Yfsf+I/UfsP2L/EfuP2H/E/iP2H7H/iP1H7D9i/xH7j9h+x/4j9R+w/Yv8R+4/Yf8T+I/Yfsf+I/UfsP2L/EfuP2H/E/iP2H7H/iP1H7D9i/xH7j9h+x/4j9R+w/Yv8R+4/Yf8T+I/Yfsf+I/UfsP2L/EfuP2H/E/iP2H7H/iP1H7D9i/xH7j9h+x/4j9R+w/Yv8R+4/Yf8T+I/Yfsf+I/UfsP2L/EfuP2H/E/iP2H/kBh3Rz5Qd4n48AAAAASUVORK5CYII=";

    /**
     * Cria uma nova transação PIX simulada.
     *
     * Recebe o valor do pagamento e gera uma transação com:
     * - ID único (UUID)
     * - Status inicial "pending"
     * - QR Code estático (não funcional)
     * - Código PIX simulado
     *
     * Lógica de negócio:
     * 1. Valida o valor recebido (deve ser > 0)
     * 2. Gera ID único para a transação
     * 3. Armazena status "pending" em memória
     * 4. Retorna QR Code e dados da transação
     *
     * @param payload Mapa contendo o valor do pagamento: {"valor": 15.50}
     * @return Resposta com ID da transação, status, QR Code e código PIX
     */
    @PostMapping("/pix")
    public ResponseEntity<Map<String, Object>> criarPagamentoPix(@RequestBody Map<String, Object> payload) {
        System.out.println("[PagamentoController] Recebendo pedido de Pix: " + payload);
        try {
            // Extrair e validar valor do payload
            Object valorObj = payload.get("valor");
            Double valor = null;

            // Suporta valor como Number ou String
            if (valorObj instanceof Number) {
                valor = ((Number) valorObj).doubleValue();
            } else if (valorObj instanceof String) {
                try {
                    valor = Double.parseDouble((String) valorObj);
                } catch (NumberFormatException e) {
                    // Valor inválido como string
                }
            }

            // Validação: valor deve ser positivo
            if (valor == null || valor <= 0) {
                System.err.println("[PagamentoController] Valor invalido: " + valorObj);
                return ResponseEntity.badRequest().body(Map.of("error", "Valor inválido"));
            }

            // Gerar ID único para a transação
            String transactionId = UUID.randomUUID().toString();
            System.out.println("[PagamentoController] Gerado ID: " + transactionId);

            // Salvar status inicial como "pending"
            transacoes.put(transactionId, "pending");

            // Montar resposta com dados da transação
            Map<String, Object> response = new HashMap<>();
            response.put("id", transactionId);
            response.put("status", "pending");

            // Retornar QR Code simulado (não funcional)
            response.put("qr_code", "00020126580014BR.GOV.BCB.PIX0136" + transactionId
                    + "5204000053039865802BR5913Sistema Leao6008Brasilia62070503***6304");
            response.put("qr_code_base64", MOCK_QR_CODE_BASE64);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Verifica o status atual de uma transação PIX.
     *
     * Este endpoint é usado pelo frontend para fazer polling e verificar
     * se o pagamento foi aprovado. O frontend chama este endpoint periodicamente
     * até que o status mude para "approved".
     *
     * Status possíveis:
     * - "pending": Aguardando pagamento
     * - "approved": Pagamento confirmado
     * - "unknown": Transação não encontrada
     *
     * @param id Identificador único da transação
     * @return Status atual da transação
     */
    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> verificarStatus(@PathVariable String id) {
        String status = transacoes.getOrDefault(id, "unknown");
        // System.out.println("[PagamentoController] Check status " + id + ": " +
        // status); // Descomentar se quiser muito flood no log
        return ResponseEntity.ok(Map.of("status", status));
    }

    /**
     * Confirma manualmente um pagamento (modo de teste/simulação).
     *
     * Este endpoint simula a aprovação de um pagamento pelo banco.
     * Em produção, isso seria feito automaticamente via webhook do gateway.
     *
     * IMPORTANTE: Este é um endpoint de TESTE. Em produção, a confirmação
     * deve vir do gateway de pagamento via webhook.
     *
     * @param id Identificador único da transação a ser confirmada
     * @return Status "approved" se a transação existir, 404 caso contrário
     */
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<Map<String, String>> confirmarPagamento(@PathVariable String id) {
        System.out.println("[PagamentoController] Confirmando pagamento: " + id);
        if (transacoes.containsKey(id)) {
            transacoes.put(id, "approved");
            return ResponseEntity.ok(Map.of("status", "approved", "message", "Pagamento confirmado com sucesso!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
