create database hotel;

create table guests(
id serial,
nome varchar(255),
documento varchar(255),
telefone varchar(100),
valor_gasto numeric(10,2),
primary key (id));

create table checkins(
id serial,
hospede integer,
data_entrada TIMESTAMP,
data_saida TIMESTAMP,
adicional_veiculo boolean,
primary key(id),
foreign key(hospede) references guests(id));