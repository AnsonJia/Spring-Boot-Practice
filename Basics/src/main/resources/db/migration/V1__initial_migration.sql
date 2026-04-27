create table users
(
    --id       bigint generated always as identity
        --constraint users_pk
            --primary key,
    id       bigint generated always as identity primary key,
    name     varchar(255) not null,
    email    varchar(255) not null,
    password varchar(255) not null
);

--alter table users
    --owner to postgres;

create table addresses
(
    --id      bigint generated always as identity
        --constraint addresses_pk
            --primary key,
    id      bigint generated always as identity primary key,
    street  varchar(255) not null,
    city    varchar(255) not null,
    zip     varchar(255) not null,
    --user_id bigint       not null
        --constraint addresses_users_id_fk
            --references users
    user_id bigint not null references users(id)
);

--alter table addresses
    --owner to postgres;

