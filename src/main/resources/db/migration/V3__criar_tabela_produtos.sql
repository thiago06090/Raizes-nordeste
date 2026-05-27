CREATE TABLE produtos (
                          id BIGSERIAL PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          descricao VARCHAR(255),
                          preco DECIMAL(10,2) NOT NULL,
                          disponivel BOOLEAN NOT NULL DEFAULT TRUE
);