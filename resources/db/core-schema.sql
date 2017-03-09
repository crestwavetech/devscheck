create table admin_groups (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR,
    description VARCHAR,
    cdat DATE,
    udat DATE
);
insert into admin_groups (id,name,description) values (null,'system','default group',datetime(),datetime());

create table admins (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    admin_group_id INTEGER,
    first_name VARCHAR,
    last_name VARCHAR,
    email VARCHAR,
    phone VARCHAR,
    login VARCHAR,
    hash VARCHAR,
    fail NUMBER(3,0),
    adat DATE,
    ldat DATE,
    cdat DATE,
    udat DATE,
    ena NUMBER(1,0),
    constraint admin_groups_fk foreign key (admin_group_id) references admin_groups (id)
);

create table admin_roles (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	admin_id INTEGER,
    role VARCHAR,
    params VARCHAR,
    cdat DATE,
    udat DATE,
    constraint admin_roles_fk foreign key (admin_id) references admins (id)
);