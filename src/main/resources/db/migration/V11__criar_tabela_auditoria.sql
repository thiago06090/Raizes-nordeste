CREATE TABLE auditoria (
                           id BIGSERIAL PRIMARY KEY,
                           acao VARCHAR(50) NOT NULL,
                           usuario_email VARCHAR(150),
                           descricao VARCHAR(500) NOT NULL,
                           dados_extras VARCHAR(500),
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);