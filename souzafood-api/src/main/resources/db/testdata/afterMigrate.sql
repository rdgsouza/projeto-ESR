set foreign_key_checks = 0;

truncate souzafood.cidade;
truncate souzafood.cozinha;
truncate souzafood.estado;
truncate souzafood.forma_pagamento;
truncate souzafood.grupo;
truncate souzafood.grupo_permissao;
truncate souzafood.permissao;
truncate souzafood.produto;
truncate souzafood.restaurante;
truncate souzafood.restaurante_forma_pagamento;
truncate souzafood.restaurante_usuario_responsavel;
truncate souzafood.usuario;
truncate souzafood.usuario_grupo;

set foreign_key_checks = 1;

alter table souzafood.cidade auto_increment = 1;
alter table souzafood.cozinha auto_increment = 1;
alter table souzafood.estado auto_increment = 1;
alter table souzafood.forma_pagamento auto_increment = 1;
alter table souzafood.grupo auto_increment = 1;
alter table souzafood.permissao auto_increment = 1;
alter table souzafood.produto auto_increment = 1;
alter table souzafood.restaurante auto_increment = 1;
alter table souzafood.usuario auto_increment = 1;

insert into souzafood.cozinha (id, nome) values (1, 'Tailandesa');
insert into souzafood.cozinha (id, nome) values (2, 'Indiana');
insert into souzafood.cozinha (id, nome) values (3, 'Argentina');
insert into souzafood.cozinha (id, nome) values (4, 'Brasileira');

insert into souzafood.estado (id, nome) values (1, 'Minas Gerais');
insert into souzafood.estado (id, nome) values (2, 'São Paulo');
insert into souzafood.estado (id, nome) values (3, 'Ceará');

insert into souzafood.cidade (id, nome, estado_id) values (1, 'Uberlândia', 1);
insert into souzafood.cidade (id, nome, estado_id) values (2, 'Belo Horizonte', 1);
insert into souzafood.cidade (id, nome, estado_id) values (3, 'São Paulo', 2);
insert into souzafood.cidade (id, nome, estado_id) values (4, 'Campinas', 2);
insert into souzafood.cidade (id, nome, estado_id) values (5, 'Fortaleza', 3);

insert into restaurante (id, nome, taxa_frete, cozinha_id, data_cadastro, data_atualizacao, ativo, aberto, endereco_cidade_id, endereco_cep, endereco_logradouro, endereco_numero, endereco_bairro) values (1, 'Thai Gourmet', 10, 1, utc_timestamp, utc_timestamp, true, true, 1, '38400-999', 'Rua João Pinheiro', '1000', 'Centro');
insert into restaurante (id, nome, taxa_frete, cozinha_id, data_cadastro, data_atualizacao, ativo, aberto) values (2, 'Thai Delivery', 9.50, 1, utc_timestamp, utc_timestamp, true, true);
insert into restaurante (id, nome, taxa_frete, cozinha_id, data_cadastro, data_atualizacao, ativo, aberto) values (3, 'Tuk Tuk Comida Indiana', 15, 2, utc_timestamp, utc_timestamp, true, true);
insert into restaurante (id, nome, taxa_frete, cozinha_id, data_cadastro, data_atualizacao, ativo, aberto) values (4, 'Java Steakhouse', 12, 3, utc_timestamp, utc_timestamp, true, true);
insert into restaurante (id, nome, taxa_frete, cozinha_id, data_cadastro, data_atualizacao, ativo, aberto) values (5, 'Lanchonete do Tio Sam', 11, 4, utc_timestamp, utc_timestamp, true, true);
insert into restaurante (id, nome, taxa_frete, cozinha_id, data_cadastro, data_atualizacao, ativo, aberto) values (6, 'Bar da Maria', 6, 4, utc_timestamp, utc_timestamp, true, true);                       


insert into souzafood.produto (id, nome, descricao, preco, ativo, restaurante_id) values (1, 'Porco com molho agridoce', 'Deliciosa carne suína ao molho especial', 78.90, 1, 1);
insert into souzafood.produto (id, nome, descricao, preco, ativo, restaurante_id) values (2, 'Camarão tailandês', '16 camarões grandes ao molho picante', 110, 1, 1);
insert into souzafood.produto (id, nome, descricao, preco, ativo, restaurante_id) values (3, 'Salada picante com carne grelhada', 'Salada de folhas com cortes finos de carne bovina grelhada e nosso molho especial de pimenta vermelha', 87.20, 1, 2);
insert into souzafood.produto (id, nome, descricao, preco, ativo, restaurante_id) values (4, 'Garlic Naan', 'Pão tradicional indiano com cobertura de alho', 21, 1, 3);
insert into souzafood.produto (id, nome, descricao, preco, ativo, restaurante_id) values (5, 'Murg Curry', 'Cubos de frango preparados com molho curry e especiarias', 43, 1, 3);
insert into souzafood.produto (id, nome, descricao, preco, ativo, restaurante_id) values (6, 'Bife Ancho', 'Corte macio e suculento, com dois dedos de espessura, retirado da parte dianteira do contrafilé', 79, 1, 4);
insert into souzafood.produto (id, nome, descricao, preco, ativo, restaurante_id) values (7, 'T-Bone', 'Corte muito saboroso, com um osso em formato de T, sendo de um lado o contrafilé e do outro o filé mignon', 89, 1, 4);
insert into souzafood.produto (id, nome, descricao, preco, ativo, restaurante_id) values (8, 'Sanduíche X-Tudo', 'Sandubão com muito queijo, hamburger bovino, bacon, ovo, salada e maionese', 19, 1, 5);
insert into souzafood.produto (id, nome, descricao, preco, ativo, restaurante_id) values (9, 'Espetinho de Cupim', 'Acompanha farinha, mandioca e vinagrete', 8, 1, 6);

insert into souzafood.forma_pagamento (id, descricao) values (1, 'Cartão de crédito');
insert into souzafood.forma_pagamento (id, descricao) values (2, 'Cartão de débito');
insert into souzafood.forma_pagamento (id, descricao) values (3, 'Dinheiro');

insert into souzafood.restaurante_forma_pagamento (restaurante_id, forma_pagamento_id) values (1, 1), (1, 2), (1, 3), (2, 3), (3, 2), (3, 3), (4, 1), (4, 2), (5, 1), (5, 2);

insert into souzafood.permissao (id, nome, descricao) values (1, 'CONSULTAR_COZINHAS', 'Permite consultar cozinhas');
insert into souzafood.permissao (id, nome, descricao) values (2, 'EDITAR_COZINHAS', 'Permite editar cozinhas');
insert into souzafood.permissao (id, nome, descricao) values (3, 'CONSULTAR_RESTAURANTES', 'Permite consultar restaurantes');
insert into souzafood.permissao (id, nome, descricao) values (4, 'EDITAR_RESTAURANTES', 'Permite editar restaurantes');
insert into souzafood.permissao (id, nome, descricao) values (5, 'CONSULTAR_PRODUTOS', 'Permite consultar produtos');
insert into souzafood.permissao (id, nome, descricao) values (6, 'EDITAR_PRODUTOS', 'Permite editar produtos');

insert into souzafood.grupo (id, nome) values (1, 'Administrador');
insert into souzafood.grupo (id, nome) values (2, 'Tático');
insert into souzafood.grupo (id, nome) values (3, 'Operacional');
insert into souzafood.grupo (id, nome) values (4, 'Auxiliar');

insert into souzafood.grupo_permissao (grupo_id, permissao_id) values (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6); 
insert into souzafood.grupo_permissao (grupo_id, permissao_id) values (2, 1), (2, 2), (2, 3),(2, 4);
insert into souzafood.grupo_permissao (grupo_id, permissao_id) values (3, 1), (3, 2), (3, 3),(3, 4);
insert into souzafood.grupo_permissao (grupo_id, permissao_id) values (4, 1), (4, 3);

insert into souzafood.usuario (id, data_cadastro, email, nome, senha) values (1, utc_timestamp, 'rdsouza.c@gmail.com', 'Rodrigo Carneiro de Souza', '123');
insert into souzafood.usuario (id, data_cadastro, email, nome, senha) values (2, utc_timestamp, 'jose_rodrigues@gmail.com', 'José Rodrigues', '123');
insert into souzafood.usuario (id, data_cadastro, email, nome, senha) values (3, utc_timestamp, 'maria_neves@gmail.com', 'Maria das Neves', '123');
insert into souzafood.usuario (id, data_cadastro, email, nome, senha) values (4, utc_timestamp, 'joao_silva.c@gmail.com', 'João Silva', '123');
insert into souzafood.usuario (id, data_cadastro, email, nome, senha) values (5, utc_timestamp, 'manoel.lm@gmail.com', 'Manoel Lima', '123');

insert into souzafood.usuario_grupo (usuario_id, grupo_id) values (1, 1);
insert into souzafood.usuario_grupo (usuario_id, grupo_id) values (1, 2);
insert into souzafood.usuario_grupo (usuario_id, grupo_id) values (1, 3);
insert into souzafood.usuario_grupo (usuario_id, grupo_id) values (2, 2);
insert into souzafood.usuario_grupo (usuario_id, grupo_id) values (2, 3);
insert into souzafood.usuario_grupo (usuario_id, grupo_id) values (2, 4);
insert into souzafood.usuario_grupo (usuario_id, grupo_id) values (3, 3);
insert into souzafood.usuario_grupo (usuario_id, grupo_id) values (3, 4);
insert into souzafood.usuario_grupo (usuario_id, grupo_id) values (4, 4);

insert into restaurante_usuario_responsavel (restaurante_id, usuario_id) values (1, 5), (3, 5);
