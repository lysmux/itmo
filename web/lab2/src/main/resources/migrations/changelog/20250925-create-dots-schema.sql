CREATE TABLE dots
(
    x        float8,
    y        float8,
    r        float8,
    contains boolean,

    PRIMARY KEY (x, y, r)
);
-- rollback DROP TABLE dots;

