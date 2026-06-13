package com.raizes.backend.application.service;

import com.raizes.backend.api.dto.CriarPedidoRequest;
import com.raizes.backend.api.dto.CriarPedidoRequest.ItemPedidoRequest;
import com.raizes.backend.api.dto.PedidoResponse;
import com.raizes.backend.api.dto.PedidoResponse.ItemResponse;
import com.raizes.backend.domain.model.*;
import com.raizes.backend.domain.model.Pagamento.StatusPagamento;
import com.raizes.backend.domain.model.Pedido.CanalPedido;
import com.raizes.backend.domain.model.Pedido.StatusPedido;
import com.raizes.backend.domain.repository.*;
import com.raizes.backend.infrastructure.payment.PaymentMockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UnidadeRepository unidadeRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;
    private final PagamentoRepository pagamentoRepository;
    private final PontosFidelidadeRepository pontosRepository;
    private final PaymentMockService paymentMockService;
    private final AuditoriaService auditoriaService;

    @Transactional
    public PedidoResponse criarPedido(
            String emailCliente,
            CriarPedidoRequest request) {

        // busca o cliente
        Usuario cliente = usuarioRepository
                .findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException(
                        "Cliente não encontrado"
                ));

        // busca a unidade
        Unidade unidade = unidadeRepository
                .findById(request.getUnidadeId())
                .orElseThrow(() -> new RuntimeException(
                        "Unidade não encontrada"
                ));

        // valida e monta os itens
        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequest itemReq : request.getItens()) {

            // busca o produto
            Produto produto = produtoRepository
                    .findById(itemReq.getProdutoId())
                    .orElseThrow(() -> new RuntimeException(
                            "Produto não encontrado: "
                                    + itemReq.getProdutoId()
                    ));

            // verifica estoque
            Estoque estoque = estoqueRepository
                    .findByUnidadeIdAndProdutoId(
                            unidade.getId(),
                            produto.getId()
                    )
                    .orElseThrow(() -> new RuntimeException(
                            "Produto " + produto.getNome()
                                    + " não disponível nesta unidade"
                    ));

            if (estoque.getQuantidade() < itemReq.getQuantidade()) {
                throw new RuntimeException(
                        "Estoque insuficiente para: "
                                + produto.getNome()
                                + ". Disponível: "
                                + estoque.getQuantidade()
                );
            }

            // desconta o estoque
            estoque.setQuantidade(
                    estoque.getQuantidade() - itemReq.getQuantidade()
            );
            estoqueRepository.save(estoque);

            // monta o item
            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemReq.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            itens.add(item);

            // acumula o total
            total = total.add(
                    produto.getPreco().multiply(
                            BigDecimal.valueOf(itemReq.getQuantidade())
                    )
            );
        }

        // cria o pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setUnidade(unidade);
        pedido.setCanalPedido(request.getCanalPedido());
        pedido.setTotal(total);
        pedido = pedidoRepository.save(pedido);

        // associa os itens ao pedido
        for (ItemPedido item : itens) {
            item.setPedido(pedido);
        }
        pedido.setItens(itens);
        pedidoRepository.save(pedido);

        auditoriaService.registrar(
                "PEDIDO_CRIADO",
                emailCliente,
                "Pedido criado com sucesso",
                "pedidoId=" + pedido.getId()
                        + " | canal=" + request.getCanalPedido()
                        + " | total=R$" + total
        );


        // processa pagamento mock
        StatusPagamento statusPagamento = paymentMockService
                .processarPagamento(
                        pedido.getId(),
                        request.getFormaPagamento()
                );

        auditoriaService.registrar(
                "PAGAMENTO_PROCESSADO",
                emailCliente,
                "Pagamento processado via mock",
                "pedidoId=" + pedido.getId()
                        + " | status=" + statusPagamento.name()
                        + " | forma=" + request.getFormaPagamento()
        );

        // salva o pagamento
        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setStatus(statusPagamento);
        pagamento.setFormaPagamento(request.getFormaPagamento());
        pagamentoRepository.save(pagamento);

        // atualiza status do pedido
        if (statusPagamento == StatusPagamento.APROVADO) {
            pedido.setStatus(StatusPedido.PAGO);
            // adiciona pontos de fidelidade (1 ponto por real gasto)
            adicionarPontos(cliente, total);
        } else {
            pedido.setStatus(StatusPedido.CANCELADO);
            // devolve o estoque se pagamento recusado
            devolverEstoque(itens, unidade);
        }
        pedidoRepository.save(pedido);
        auditoriaService.registrar(
                "STATUS_PEDIDO_ATUALIZADO",
                emailCliente,
                "Status do pedido atualizado",
                "pedidoId=" + pedido.getId()
                        + " | novoStatus=" + pedido.getStatus().name()
        );


        return converterParaResponse(pedido, statusPagamento);
    }



    public List<PedidoResponse> listarPedidos(
            CanalPedido canal, StatusPedido status) {

        List<Pedido> pedidos;

        if (canal != null && status != null) {
            pedidos = pedidoRepository
                    .findByCanalPedidoAndStatus(canal, status);
        } else if (canal != null) {
            pedidos = pedidoRepository.findByCanalPedido(canal);
        } else if (status != null) {
            pedidos = pedidoRepository.findByStatus(status);
        } else {
            pedidos = pedidoRepository.findAll();
        }

        return pedidos.stream()
                .map(p -> converterParaResponse(p, null))
                .toList();
    }

    public PedidoResponse buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Pedido não encontrado"
                ));
        return converterParaResponse(pedido, null);
    }

    @Transactional
    public PedidoResponse atualizarStatus(
            Long id, StatusPedido novoStatus) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Pedido não encontrado"
                ));
        StatusPedido statusAnterior = pedido.getStatus();
        pedido.setStatus(novoStatus);
        pedidoRepository.save(pedido);

        auditoriaService.registrar(
                "STATUS_PEDIDO_ATUALIZADO",
                "sistema",
                "Status do pedido atualizado manualmente",
                "pedidoId=" + id
                        + " | de=" + statusAnterior.name()
                        + " | para=" + novoStatus.name()
        );

        return converterParaResponse(pedido, null);
    }

    // métodos auxiliares
    private void adicionarPontos(
            Usuario cliente, BigDecimal total) {

        pontosRepository.findByUsuarioId(cliente.getId())
                .ifPresent(pontos -> {
                    int pontosGanhos = total.intValue();
                    pontos.setSaldo(pontos.getSaldo() + pontosGanhos);
                    pontosRepository.save(pontos);
                });
    }

    private void devolverEstoque(
            List<ItemPedido> itens, Unidade unidade) {

        for (ItemPedido item : itens) {
            estoqueRepository.findByUnidadeIdAndProdutoId(
                    unidade.getId(),
                    item.getProduto().getId()
            ).ifPresent(estoque -> {
                estoque.setQuantidade(
                        estoque.getQuantidade() + item.getQuantidade()
                );
                estoqueRepository.save(estoque);
            });
        }
    }

    private PedidoResponse converterParaResponse(
            Pedido pedido, StatusPagamento statusPagamento) {

        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setNomeCliente(pedido.getCliente().getNome());
        response.setNomeUnidade(pedido.getUnidade().getNome());
        response.setCanalPedido(pedido.getCanalPedido());
        response.setStatus(pedido.getStatus());
        response.setTotal(pedido.getTotal());
        response.setCreatedAt(pedido.getCreatedAt());

        if (statusPagamento != null) {
            response.setStatusPagamento(statusPagamento.name());
        }

        if (pedido.getItens() != null) {
            List<ItemResponse> itensResponse = pedido.getItens()
                    .stream()
                    .map(item -> {
                        ItemResponse ir = new ItemResponse();
                        ir.setProdutoId(item.getProduto().getId());
                        ir.setNomeProduto(item.getProduto().getNome());
                        ir.setQuantidade(item.getQuantidade());
                        ir.setPrecoUnitario(item.getPrecoUnitario());
                        ir.setSubtotal(
                                item.getPrecoUnitario().multiply(
                                        BigDecimal.valueOf(item.getQuantidade())
                                )
                        );
                        return ir;
                    })
                    .toList();
            response.setItens(itensResponse);
        }

        return response;
    }

}
