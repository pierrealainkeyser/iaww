
create table active_game(uuid varchar(36) not null, content text not null, primary key (uuid));

create table user(uid varchar(255) not null, name varchar(255) not null, primary key(uid));
