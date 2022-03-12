CREATE TABLE public.address
(
    id              UUID                  NOT NULL,
    city            character varying(50) NOT NULL,
    postal_code     character varying(6)  NOT NULL,
    street_name     character varying(50) NOT NULL,
    street_number   character varying(5)  NOT NULL,
    PRIMARY KEY (id)
);