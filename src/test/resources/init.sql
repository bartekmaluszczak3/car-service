create table car(
    id integer PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    mileage VARCHAR(255) NOT NULL
);

CREATE sequence car_seq increment 1 start 1;
