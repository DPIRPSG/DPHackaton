create user "acme-user" password 'ACME-Us3r-P@ssw0rd';
create user "acme-manager" password 'ACME-M@n@ger-6874';

drop database if exists "Acme-Orienteering";
create database "Acme-Orienteering" owner "acme-manager";

grant all on database "Acme-Orienteering" to "acme-user";
grant all on database "Acme-Orienteering" to postgres;

#conectarse a "Acme-Orienteering" con usuario postgres y ejecutar: 
alter schema public owner to "acme-manager";
