package com.raizes.backend.application.service;

import com.raizes.backend.api.dto.CardapioItemResponse;
import com.raizes.backend.domain.model.Estoque;
import com.raizes.backend.domain.repository.EstoqueRepository;
import com.raizes.backend.domain.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardapioService {

    private final EstoqueRepository estoqueRepository;
    private final UnidadeRepository unidadeRepository;

    public List<CardapioItemResponse> buscarCardapioPorUnidade(
            Long unidadeId) {

        // verifica se a unidade existe
        unidadeRepository.findById(unidadeId)
                .orElseThrow(() -> new RuntimeException(
                        "Unidade não encontrada"
                ));

        // busca todos os itens do estoque da unidade
        List<Estoque> estoques = estoqueRepository
                .findAll()
                .stream()
                .filter(e -> e.getUnidade().getId().equals(unidadeId))
                .toList();

        // converte para CardapioItemResponse
        return estoques.stream()
                .filter(e -> e.getProduto().getDisponivel())
                .map(e -> {
                    CardapioItemResponse item =
                            new CardapioItemResponse();
                    item.setProdutoId(e.getProduto().getId());
                    item.setNome(e.getProduto().getNome());
                    item.setDescricao(e.getProduto().getDescricao());
                    item.setPreco(e.getProduto().getPreco());
                    item.setQuantidadeDisponivel(e.getQuantidade());
                    item.setDisponivel(e.getQuantidade() > 0);
                    return item;
                })
                .toList();
    }
}