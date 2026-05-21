CREATE TABLE pontos_fidelidade (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   usuario_id BIGINT NOT NULL UNIQUE,
                                   saldo INT NOT NULL DEFAULT 0,
                                   FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);