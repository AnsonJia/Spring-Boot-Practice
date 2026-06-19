create table carts
(   --gen_random_uuid() is a function from the pgcrypto extension that generates a random UUID requires (CREATE EXTENSION IF NOT EXISTS pgcrypto;)
    id uuid default (gen_random_uuid()) primary key not null,
    date_created date default (current_date) not null
);

create table cart_items
(
    id         bigint generated always as identity
        constraint cart_items_pk
            primary key,
    cart_id    uuid              not null
        constraint cart_items_carts_id_fk --foreign key that references carts id,
            references carts (id)
            on delete cascade, --when a cart is deleted, all associated cart items will also be deleted
    product_id bigint            not null
        constraint cart_items_products_id_fk --foreign key that references products id,
            references products (id)
            on delete cascade, --when a product is deleted, all associated cart items will also be deleted
    quantity   integer default 1 not null,
    constraint cart_items_cart_product_unique -- unique constraint to ensure a product can only exist once in a cart (no duplicate product in the same cart)
        unique (cart_id, product_id)
);
