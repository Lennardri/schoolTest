create table klasse (
	id identity,
	name varchar(255) not null
);
create table unterricht (
	id identity,
	name varchar(255) not null,
	klasse_id int not null
);
alter table unterricht add foreign key (klasse_id) references klasse(id);
create table raum (
	id identity,
	name varchar(255) not null
);

insert into klasse(id, name) select * from csvread('src/test/resources/klasse.csv');
insert into unterricht(klasse_id, name) select * from csvread('src/test/resources/unterricht.csv');
insert into raum(name) select * from csvread('src/test/resources/raum.csv');