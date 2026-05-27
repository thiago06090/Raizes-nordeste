package com.raizes.backend.application.service;

import com.raizes.backend.domain.model.Estoque;
import com.raizes.backend.domain.model.Produto;
import com.raizes.backend.domain.model.Unidade;
import com.raizes.backend.domain.repository.EstoqueRepository;
import com.raizes.backend.domain.repository.ProdutoRepository;
import com.raizes.backend.domain.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueService {
    private final EstoqueRepository estoqueRepository;
    private final UnidadeRepository unidadeRepository;
    private final ProdutoRepository produtoRepository;

    public List<Estoque> listarPorUnidade(Long unidadeId) {
        return estoqueRepository.findAll().stream()
                .filter(e -> e.getUnidade().getId().equals(unidadeId))
                .toList();
    }

    public Estoque buscarPorUnidadeEProduto(
            Long unidadeId, Long produtoId) {
        return estoqueRepository
                .findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .orElseThrow(() -> new RuntimeException(
                        "Estoque não encontrado para essa unidade e produto"
                ));
    }

    public Estoque entrada(
            Long unidadeId, Long produtoId, Integer quantidade) {

        Estoque estoque = estoqueRepository
                .findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .orElseGet(() -> criarEstoque(unidadeId, produtoId));

        estoque.setQuantidade(estoque.getQuantidade() + quantidade);
        return estoqueRepository.save(estoque);
    }

    public Estoque saida(
            Long unidadeId, Long produtoId, Integer quantidade) {

        Estoque estoque = buscarPorUnidadeEProduto(unidadeId, produtoId);

        if (estoque.getQuantidade() < quantidade) {
            throw new RuntimeException(
                    "Estoque insuficiente. Disponível: "
                            + estoque.getQuantidade()
            );
        }

        estoque.setQuantidade(estoque.getQuantidade() - quantidade);
        return estoqueRepository.save(estoque);
    }

    private Estoque criarEstoque(Long unidadeId, Long produtoId) {
        Unidade unidade = unidadeRepository.findById(unidadeId)
                .orElseThrow(() -> new RuntimeException(
                        "Unidade não encontrada"
                ));
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException(
                        "Produto não encontrado"
                ));

        Estoque estoque = new Estoque();
        estoque.setUnidade(unidade);
        estoque.setProduto(produto);
        estoque.setQuantidade(0);
        return estoqueRepository.save(estoque);
    }
}
