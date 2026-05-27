CREATE TABLE estoque (
                         id BIGSERIAL PRIMARY KEY,
                         unidade_id BIGINT NOT NULL,
                         produto_id BIGINT NOT NULL,
                         quantidade INT NOT NULL DEFAULT 0,
                         FOREIGN KEY (unidade_id) REFERENCES unidades(id),
                         FOREIGN KEY (produto_id) REFERENCES produtos(id)
);