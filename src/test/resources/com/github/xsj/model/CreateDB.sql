drop table users if exists;

create table users (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) primary key,
  name varchar(126) not null
);

insert into users(name) values('User1');