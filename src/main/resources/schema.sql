create table account (id bigint not null,
account_email varchar(255),
account_number varchar(255),
bank_name varchar(255),
current_balance double precision not null,
 owner_name varchar(255),
sort_code varchar(255),
primary key (id)) engine=InnoDB