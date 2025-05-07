CREATE TABLE locations
(
    id   serial PRIMARY KEY,
    x    int          NOT NULL,
    y    float8       NOT NULL,
    name varchar(783) NOT NULL
);

CREATE TABLE coordinates
(
    id serial PRIMARY KEY,
    x  int    NOT NULL CHECK ( x <= 592 ),
    y  float8 NOT NULL CHECK ( y <= 892 )
);

CREATE TABLE persons
(
    id          serial PRIMARY KEY,
    name        varchar NOT NULL,
    birthday    date    NOT NULL,
    weight      bigint  NOT NULL CHECK ( weight > 0 ),
    location_id int     NOT NULL REFERENCES locations (id)
);

CREATE TABLE lab_works
(
    id             serial PRIMARY KEY,
    owner_id       int         NOT NULL REFERENCES users (id),
    name           varchar     NOT NULL,
    coordinates_id int         NOT NULL REFERENCES coordinates (id),
    minimal_point  bigint      NOT NULL CHECK ( minimal_point > 0 ),
    difficulty     varchar(40) NOT NULL,
    author_id      int         NOT NULL REFERENCES persons (id),
    created_at     timestamptz NOT NULL DEFAULT now()
);

CREATE VIEW lab_works_full AS
    (
        SELECT lw.id AS lw_id,
               lw.owner_id AS lw_owner_id,
               lw.name AS lw_name,
               lw.minimal_point AS lw_minimal_point,
               lw.difficulty AS lw_difficulty,
               lw.created_at AS lw_created_at,
               c.x AS coord_x, c.y AS coord_y,
               p.name AS author_name, p.birthday AS author_birthday, p.weight AS author_weight,
               l.name AS loc_name, l.x AS loc_x, l.y AS loc_y
        FROM lab_works lw
        JOIN coordinates c on lw.coordinates_id = c.id
        JOIN persons p on lw.author_id = p.id
        JOIN locations l on p.location_id = l.id
    );

CREATE FUNCTION insert_lab_work_fnc()
RETURNS TRIGGER AS
    '
    DECLARE
        loc_id int;
        author_id int;
        coord_id int;
        lw_id int;
    BEGIN
        INSERT INTO locations (x, y, name)
        VALUES (NEW.loc_x, NEW.loc_y, NEW.loc_name)
        RETURNING id INTO loc_id;

        INSERT INTO persons (name, birthday, weight, location_id)
        VALUES (NEW.author_name, NEW.author_birthday, NEW.author_weight, loc_id)
        RETURNING id INTO author_id;

        INSERT INTO coordinates (x, y)
        VALUES (NEW.coord_x, NEW.coord_y)
        RETURNING id INTO coord_id;

        INSERT INTO lab_works (owner_id, name, minimal_point, difficulty, coordinates_id, author_id)
        VALUES (NEW.lw_owner_id, NEW.lw_name, NEW.lw_minimal_point, NEW.lw_difficulty, coord_id, author_id)
        RETURNING id INTO lw_id;

        NEW.lw_id := lw_id;

        RETURN NEW;
    end;
    ' language plpgsql;

CREATE TRIGGER insert_lab_work
INSTEAD OF INSERT ON lab_works_full
FOR EACH ROW
EXECUTE FUNCTION insert_lab_work_fnc();