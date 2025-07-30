drop database if EXISTS taller_bicicletas;
create database if not EXISTS taller_bicicletas;
use taller_bicicletas;

create table if not exists clientes(
	id int auto_increment primary key,
    nombre varchar(50) not null,
    apellido varchar(50) not null,
    dni char(8) not null,
    telefono varchar(10) not null,
    email varchar(50)
);

create table if not exists bicicletas(
	id int auto_increment primary key,
	id_cliente int not null,
    marca varchar(100) not null,
    modelo varchar(100) not null,
    color varchar(50) not null,
    rodado enum('14', '16', '20', '22', '24', '26', '27.5', '28', '29') not null,
    fecha_ingreso date not null,
    fecha_egreso date
);

create table if not exists presupuestos(
	numero int auto_increment primary key,
	fecha date not null,
    cliente_id int not null,
    bicicleta_id int not null,
    valor_total double not null,
    detalle varchar(300)
);

create table if not exists repuestos(
	codigo int primary key not null,
    producto varchar(100) not null,
    marca varchar(100) not null,
    color varchar(50) not null,
    precio_venta double not null,
    precio_costo double not null,
    stock int not null
);

create table if not exists detalles(
	presupuesto_numero int not null,
    repuesto_codigo int not null,
    cantidad_agregada int,
    primary key(presupuesto_numero, repuesto_codigo)
);


alter table 	bicicletas
add constraint	FK_bicicletas_clientes
foreign key		(id_cliente)
references		clientes(id);

alter table		presupuestos
add constraint	FK_presupuestos_clientes
foreign key		(cliente_id)
references		clientes(id);

alter table		presupuestos
add constraint	FK_presupuestos_bicicletas
foreign key		(bicicleta_id)
references		bicicletas(id);

alter table		detalles
add constraint	FK_detalles_presupuestos
foreign key		(presupuesto_numero)
references		presupuestos(numero);

alter table		detalles
add constraint	FK_detalles_repuestos
foreign key		(repuesto_codigo)
references		repuestos(codigo);