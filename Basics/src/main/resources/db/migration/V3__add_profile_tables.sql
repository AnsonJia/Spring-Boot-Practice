/*create table profiles
(
    id             bigint not null
        constraint profiles_pk
            primary key
        constraint profiles_users_id_fk
            references users,
    bio            text,
    phone_number   varchar(15),
    date_of_birth  date,
    loyalty_points integer default 0
        constraint profiles_loyalty_points_check
            check (loyalty_points >= 0)
);*/

--alter table profiles
    --owner to postgres;

CREATE TABLE profiles
(
    id BIGINT PRIMARY KEY REFERENCES users(id),
    bio TEXT,
    phone_number VARCHAR(15),
    date_of_birth DATE,
    loyalty_points INTEGER DEFAULT 0 CHECK (loyalty_points >= 0)
);