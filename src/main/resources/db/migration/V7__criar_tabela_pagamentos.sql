CREATE TABLE pagamentos (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            pedido_id BIGINT NOT NULL,
                            status VARCHAR(20) NOT NULL,
                            forma_pagamento VARCHAR(20) NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (pedido_id) REFERENCES pedidos(id)
);
