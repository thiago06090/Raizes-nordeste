CREATE TABLE produtos (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          descricao VARCHAR(255),
                          preco DECIMAL(10,2) NOT NULL,
                          disponivel BOOLEAN NOT NULL DEFAULT TRUE
);