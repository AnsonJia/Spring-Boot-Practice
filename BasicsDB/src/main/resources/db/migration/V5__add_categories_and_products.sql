CREATE TABLE categories
(
    id smallint generated always as identity primary key,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE products
(
    id bigint PRIMARY KEY,
    name VARCHAR(255) NOT NULL ,
    price DECIMAL(10,2) NOT NULL ,
    category_id smallint,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);