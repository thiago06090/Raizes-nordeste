CREATE TABLE unidades (
                          id BIGSERIAL PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          cidade VARCHAR(100) NOT NULL,
                          estado VARCHAR(2) NOT NULL,
                          ativa BOOLEAN NOT NULL DEFAULT TRUE
);