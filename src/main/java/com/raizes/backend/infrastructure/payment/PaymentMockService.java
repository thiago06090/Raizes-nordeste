package com.raizes.backend.infrastructure.payment;

import com.raizes.backend.domain.model.Pagamento.StatusPagamento;
import org.springframework.stereotype.Service;

@Service
public class PaymentMockService {
    // simula o pagamento externo
    // 90% de chance de aprovar, 10% de recusar
    public StatusPagamento processarPagamento(
            Long pedidoId, String formaPagamento) {

        System.out.println("=== PAYMENT MOCK ===");
        System.out.println("Processando pagamento do pedido: " + pedidoId);
        System.out.println("Forma de pagamento: " + formaPagamento);

        // simula uma pequena demora do gateway externo
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // regra simples: se forma de pagamento for "RECUSAR", recusa
        if ("RECUSAR".equalsIgnoreCase(formaPagamento)) {
            System.out.println("Pagamento RECUSADO (mock)");
            return StatusPagamento.RECUSADO;
        }

        System.out.println("Pagamento APROVADO (mock)");
        return StatusPagamento.APROVADO;
    }

}
