create table orders
(
    id          bigint generated always as identity primary key,
    customer_id bigint                              not null
        constraint orders_users_id_fk
            references users(id),
    status      varchar(20)                         not null,
    created_at  timestamp default current_timestamp not null,
    total_price decimal(10, 2)                      not null
);

create table order_items
(
    id          bigint generated always as identity primary key,
    order_id    bigint         not null
        constraint order_items_orders_id_fk
            references orders (id),
    product_id  bigint         not null
        constraint order_items_products_id_fk
            references products(id),
    unit_price  decimal(10, 2) not null,
    quantity    int            not null,
    total_price decimal(10, 2) not null
);
