drop database if exists `Acme-Barter`;
create database `Acme-Barter`;
grant select, insert, update, delete
on `Acme-Barter`.* to 'acme-user'@'%';
grant select, insert, update, delete, create, drop, references, index, alter, create temporary tables, lock tables, create view, create routine, alter routine, execute, trigger, show view
on `Acme-Barter`.* to 'acme-manager'@'%';
