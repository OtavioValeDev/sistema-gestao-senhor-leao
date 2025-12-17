package com.example.projeto_test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/pagamento")
public class PagamentoController {

    // Armazena o status das transações em memória (Simula o Banco)
    private final Map<String, String> transacoes = new ConcurrentHashMap<>();

    // Imagem de QR Code genérico (Placeholder válido - Padrão visível 150x150)
    private final String MOCK_QR_CODE_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAAJQAAACUCAQAAAB8N04MAAAAyklEQVR42u3TQQrAMAwDwX7J/39LTw70IAwEmx72Ea1Gg6v5d8T+I/Yfsf+I/UfsP2L/EfuP2H/E/iP2H7H/iP1H7D9i/xH7j9h/xP4j9h+x/4j9R+w/Yv8R+4/Yf8T+I/Yfsf+I/UfsP2L/EfuP2H/E/iP2H7H/iP1H7D9i/xH7j9h/xP4j9h+x/4j9R+w/Yv8R+4/Yf8T+I/Yfsf+I/UfsP2L/EfuP2H/E/iP2H7H/iP1H7D9i/xH7j9h/xP4j9h+x/4j9R+w/Yv8R+4/Yf8T+I/Yfsf+I/UfsP2L/EfuP2H/E/iP2H/kBh3Rz5Qd4n48AAAAASUVORK5CYII=";

    @PostMapping("/pix")
    public ResponseEntity<Map<String, Object>> criarPagamentoPix(@RequestBody Map<String, Object> payload) {
        System.out.println("[PagamentoController] Recebendo pedido de Pix: " + payload);
        try {
            Object valorObj = payload.get("valor");
            Double valor = null;

            if (valorObj instanceof Number) {
                valor = ((Number) valorObj).doubleValue();
            } else if (valorObj instanceof String) {
                try {
                    valor = Double.parseDouble((String) valorObj);
                } catch (NumberFormatException e) {
                    // ignore
                }
            }

            if (valor == null || valor <= 0) {
                System.err.println("[PagamentoController] Valor invalido: " + valorObj);
                return ResponseEntity.badRequest().body(Map.of("error", "Valor inválido"));
            }

            // Gerar ID Simulado
            String transactionId = UUID.randomUUID().toString();
            System.out.println("[PagamentoController] Gerado ID: " + transactionId);

            // Salvar status PENDING
            transacoes.put(transactionId, "pending");

            Map<String, Object> response = new HashMap<>();
            response.put("id", transactionId);
            response.put("status", "pending");

            // Retornar QR Code Simulado
            response.put("qr_code", "00020126580014BR.GOV.BCB.PIX0136" + transactionId
                    + "5204000053039865802BR5913Sistema Leao6008Brasilia62070503***6304");
            response.put("qr_code_base64", MOCK_QR_CODE_BASE64);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> verificarStatus(@PathVariable String id) {
        String status = transacoes.getOrDefault(id, "unknown");
        // System.out.println("[PagamentoController] Check status " + id + ": " +
        // status); // Descomentar se quiser muito flood no log
        return ResponseEntity.ok(Map.of("status", status));
    }

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
