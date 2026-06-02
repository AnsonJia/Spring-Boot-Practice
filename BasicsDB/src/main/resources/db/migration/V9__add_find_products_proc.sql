/* --MYSQL example
DELIMITER $$

CREATE PROCEDURE findProductsByPrice(
    minPrice DECIMAL(10,2),
    maxPrice DECIMAL(10,2)
)
BEGIN
    SELECT id, name, description, price, category_id
    FROM products
    WHERE price BETWEEN minPrice AND maxPrice
    ORDER BY name;
END $$

DELIMITER ;
 */

--POSTGRESQL example
CREATE OR REPLACE FUNCTION findProductsByPrice(
    min_price NUMERIC(10,2),
    max_price NUMERIC(10,2)
)
RETURNS TABLE (
    id INTEGER,
    name TEXT,
    description TEXT,
    price NUMERIC(10,2),
    category_id INTEGER
)
LANGUAGE SQL
AS $$
SELECT id, name, description, price, category_id
FROM products
WHERE price BETWEEN min_price AND max_price
ORDER BY name;
$$;