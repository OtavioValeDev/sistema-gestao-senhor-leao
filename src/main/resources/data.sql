-- ============================================================================
-- DADOS INICIAIS - CARDÁPIO COMPLETO DA LANCHONETE SENHOR LEÃO
-- ============================================================================
-- Este arquivo é executado automaticamente pelo Spring Boot ao iniciar a aplicação
-- Todos os produtos do cardápio são inseridos no banco H2 em memória
-- ============================================================================

-- Limpar tabela existente (caso haja dados e para evitar duplicatas em reload)
DELETE FROM product_filters;
DELETE FROM products;
DELETE FROM filters;

-- ===========================================
-- 🏷️ FILTROS (CATEGORIAS)
-- ===========================================
INSERT INTO filters (name) VALUES ('Lanches');
INSERT INTO filters (name) VALUES ('Bebidas');
INSERT INTO filters (name) VALUES ('Sobremesas');
INSERT INTO filters (name) VALUES ('Pratos');
INSERT INTO filters (name) VALUES ('Porções');
INSERT INTO filters (name) VALUES ('Salgados');
INSERT INTO filters (name) VALUES ('Saudável');

-- ===========================================
-- 🥪 LANCHES
-- ===========================================
INSERT INTO products (name, price_in_cents) VALUES ('X-Burger', 2500);
INSERT INTO products (name, price_in_cents) VALUES ('X-Salada', 2200);
INSERT INTO products (name, price_in_cents) VALUES ('X-Bacon', 2800);
INSERT INTO products (name, price_in_cents) VALUES ('Frango Grelhado', 2400);
INSERT INTO products (name, price_in_cents) VALUES ('Sanduíche Natural', 1800);

-- VINCULAR LANCHES A CATEGORIA
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'X-Burger'), (SELECT id FROM filters WHERE name = 'Lanches'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'X-Salada'), (SELECT id FROM filters WHERE name = 'Lanches'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'X-Bacon'), (SELECT id FROM filters WHERE name = 'Lanches'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Frango Grelhado'), (SELECT id FROM filters WHERE name = 'Lanches'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Sanduíche Natural'), (SELECT id FROM filters WHERE name = 'Lanches'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Sanduíche Natural'), (SELECT id FROM filters WHERE name = 'Saudável'));

-- ===========================================
-- 🌭 ESPECIAIS RÁPIDOS
-- ===========================================
INSERT INTO products (name, price_in_cents) VALUES ('Cachorro-Quente Tradicional', 1500);
INSERT INTO products (name, price_in_cents) VALUES ('Cachorro-Quente Duplo', 2000);

-- VINCULAR ESPECIAIS A CATEGORIA LANCHES
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Cachorro-Quente Tradicional'), (SELECT id FROM filters WHERE name = 'Lanches'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Cachorro-Quente Duplo'), (SELECT id FROM filters WHERE name = 'Lanches'));

-- ===========================================
-- 🍟 PORÇÕES
-- ===========================================
INSERT INTO products (name, price_in_cents) VALUES ('Batata Frita (Pequena)', 1200);
INSERT INTO products (name, price_in_cents) VALUES ('Batata Frita (Média)', 1800);
INSERT INTO products (name, price_in_cents) VALUES ('Batata Frita (Grande)', 2400);
INSERT INTO products (name, price_in_cents) VALUES ('Batata com Cheddar e Bacon', 2800);
INSERT INTO products (name, price_in_cents) VALUES ('Onion Rings', 1600);
INSERT INTO products (name, price_in_cents) VALUES ('Frango Empanado Crocante', 2200);

-- VINCULAR PORÇÕES
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Batata Frita (Pequena)'), (SELECT id FROM filters WHERE name = 'Porções'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Batata Frita (Média)'), (SELECT id FROM filters WHERE name = 'Porções'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Batata Frita (Grande)'), (SELECT id FROM filters WHERE name = 'Porções'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Batata com Cheddar e Bacon'), (SELECT id FROM filters WHERE name = 'Porções'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Onion Rings'), (SELECT id FROM filters WHERE name = 'Porções'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Frango Empanado Crocante'), (SELECT id FROM filters WHERE name = 'Porções'));


-- ===========================================
-- 🍛 PRATOS FEITOS (PF)
-- ===========================================
INSERT INTO products (name, price_in_cents) VALUES ('PF de Frango', 3200);
INSERT INTO products (name, price_in_cents) VALUES ('PF de Bife Acebolado', 3500);
INSERT INTO products (name, price_in_cents) VALUES ('PF de Carne Moída', 3000);
INSERT INTO products (name, price_in_cents) VALUES ('PF de Omelete', 2800);

-- VINCULAR PRATOS
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'PF de Frango'), (SELECT id FROM filters WHERE name = 'Pratos'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'PF de Bife Acebolado'), (SELECT id FROM filters WHERE name = 'Pratos'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'PF de Carne Moída'), (SELECT id FROM filters WHERE name = 'Pratos'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'PF de Omelete'), (SELECT id FROM filters WHERE name = 'Pratos'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'PF de Omelete'), (SELECT id FROM filters WHERE name = 'Saudável'));


-- ===========================================
-- 🍝 PRATOS ESPECIAIS
-- ===========================================
INSERT INTO products (name, price_in_cents) VALUES ('Parmegiana de Frango', 3800);
INSERT INTO products (name, price_in_cents) VALUES ('Parmegiana de Carne', 4200);
INSERT INTO products (name, price_in_cents) VALUES ('Macarronada ao Sugo', 2800);
INSERT INTO products (name, price_in_cents) VALUES ('Macarronada Bolonhesa', 3200);
INSERT INTO products (name, price_in_cents) VALUES ('Estrogonofe de Frango', 3600);
INSERT INTO products (name, price_in_cents) VALUES ('Estrogonofe de Carne', 4000);

-- VINCULAR PRATOS ESPECIAIS
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Parmegiana de Frango'), (SELECT id FROM filters WHERE name = 'Pratos'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Parmegiana de Carne'), (SELECT id FROM filters WHERE name = 'Pratos'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Macarronada ao Sugo'), (SELECT id FROM filters WHERE name = 'Pratos'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Macarronada Bolonhesa'), (SELECT id FROM filters WHERE name = 'Pratos'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Estrogonofe de Frango'), (SELECT id FROM filters WHERE name = 'Pratos'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Estrogonofe de Carne'), (SELECT id FROM filters WHERE name = 'Pratos'));


-- ===========================================
-- 🥗 LIGHT
-- ===========================================
INSERT INTO products (name, price_in_cents) VALUES ('Salada Completa', 2600);
INSERT INTO products (name, price_in_cents) VALUES ('Salada Caesar', 2900);

-- VINCULAR SALADAS
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Salada Completa'), (SELECT id FROM filters WHERE name = 'Saudável'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Salada Caesar'), (SELECT id FROM filters WHERE name = 'Saudável'));


-- ===========================================
-- 🥟 SALGADOS
-- ===========================================
INSERT INTO products (name, price_in_cents) VALUES ('Coxinha (Frango)', 800);
INSERT INTO products (name, price_in_cents) VALUES ('Coxinha (Frango com Catupiry)', 900);
INSERT INTO products (name, price_in_cents) VALUES ('Risoles (Presunto e Queijo)', 700);
INSERT INTO products (name, price_in_cents) VALUES ('Risoles (Carne)', 800);
INSERT INTO products (name, price_in_cents) VALUES ('Enroladinho de Salsicha', 600);
INSERT INTO products (name, price_in_cents) VALUES ('Empada (Frango)', 1000);
INSERT INTO products (name, price_in_cents) VALUES ('Empada (Palmito)', 1100);
INSERT INTO products (name, price_in_cents) VALUES ('Empada (Camarão)', 1200);
INSERT INTO products (name, price_in_cents) VALUES ('Quibe Frito', 900);
INSERT INTO products (name, price_in_cents) VALUES ('Pastel Assado (Carne)', 800);
INSERT INTO products (name, price_in_cents) VALUES ('Pastel Assado (Queijo)', 700);
INSERT INTO products (name, price_in_cents) VALUES ('Pastel Assado (Pizza)', 900);
INSERT INTO products (name, price_in_cents) VALUES ('Pastel Assado (Frango)', 800);
INSERT INTO products (name, price_in_cents) VALUES ('Pastel Frito (Carne)', 700);
INSERT INTO products (name, price_in_cents) VALUES ('Pastel Frito (Queijo)', 600);
INSERT INTO products (name, price_in_cents) VALUES ('Pastel Frito (Pizza)', 800);
INSERT INTO products (name, price_in_cents) VALUES ('Pastel Frito (Frango)', 700);

-- VINCULAR SALGADOS
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Coxinha (Frango)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Coxinha (Frango com Catupiry)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Risoles (Presunto e Queijo)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Risoles (Carne)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Enroladinho de Salsicha'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Empada (Frango)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Empada (Palmito)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Empada (Camarão)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Quibe Frito'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Pastel Assado (Carne)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Pastel Assado (Queijo)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Pastel Assado (Pizza)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Pastel Assado (Frango)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Pastel Frito (Carne)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Pastel Frito (Queijo)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Pastel Frito (Pizza)'), (SELECT id FROM filters WHERE name = 'Salgados'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Pastel Frito (Frango)'), (SELECT id FROM filters WHERE name = 'Salgados'));


-- ===========================================
-- 🥤 BEBIDAS
-- ===========================================
INSERT INTO products (name, price_in_cents) VALUES ('Refrigerante (Lata)', 600);
INSERT INTO products (name, price_in_cents) VALUES ('Refrigerante (600ml)', 800);
INSERT INTO products (name, price_in_cents) VALUES ('Suco Natural', 1000);
INSERT INTO products (name, price_in_cents) VALUES ('Água', 400);
INSERT INTO products (name, price_in_cents) VALUES ('Chá Gelado', 700);

-- VINCULAR BEBIDAS
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Refrigerante (Lata)'), (SELECT id FROM filters WHERE name = 'Bebidas'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Refrigerante (600ml)'), (SELECT id FROM filters WHERE name = 'Bebidas'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Suco Natural'), (SELECT id FROM filters WHERE name = 'Bebidas'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Água'), (SELECT id FROM filters WHERE name = 'Bebidas'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Chá Gelado'), (SELECT id FROM filters WHERE name = 'Bebidas'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Suco Natural'), (SELECT id FROM filters WHERE name = 'Saudável'));
INSERT INTO product_filters (product_id, filter_id) VALUES ((SELECT id FROM products WHERE name = 'Água'), (SELECT id FROM filters WHERE name = 'Saudável'));
