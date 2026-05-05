alter table public.wishlist
drop constraint fk_wishlist_on_product;

alter table public.wishlist
    add constraint fk_wishlist_on_product
        foreign key (product_id) references public.products
            on delete cascade;