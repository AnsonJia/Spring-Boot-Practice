ALTER TABLE products
    ADD description TEXT;

ALTER TABLE products
    ALTER COLUMN description SET NOT NULL;