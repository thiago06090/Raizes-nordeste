CREATE TABLE pedidos (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         cliente_id BIGINT NOT NULL,
                         unidade_id BIGINT NOT NULL,
                         canal_pedido VARCHAR(20) NOT NULL,
                         status VARCHAR(30) NOT NULL,
                         total DECIMAL(10,2) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (cliente_id) REFERENCES usuarios(id),
                         FOREIGN KEY (unidade_id) REFERENCES unidades(id)
);