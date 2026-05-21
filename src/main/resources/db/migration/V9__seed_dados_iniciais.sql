-- Unidades
INSERT INTO unidades (nome, cidade, estado) VALUES
                                                ('Raízes Recife Centro', 'Recife', 'PE'),
                                                ('Raízes Fortaleza', 'Fortaleza', 'CE'),
                                                ('Raízes Salvador', 'Salvador', 'BA');

-- Produtos
INSERT INTO produtos (nome, descricao, preco) VALUES
                                                  ('Tapioca Simples', 'Tapioca com manteiga', 8.90),
                                                  ('Cuscuz Recheado', 'Cuscuz com ovo e queijo', 12.90),
                                                  ('Bolo de Macaxeira', 'Bolo de mandioca caseiro', 6.90),
                                                  ('Suco de Cajá', 'Suco natural de cajá', 7.50),
                                                  ('Café Nordestino', 'Café coado com rapadura', 4.00);

-- Estoque inicial (unidade 1)
INSERT INTO estoque (unidade_id, produto_id, quantidade) VALUES
                                                             (1, 1, 50), (1, 2, 30), (1, 3, 20), (1, 4, 40), (1, 5, 100);