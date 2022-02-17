CREATE TABLE public.address
(
    id             bigserial             NOT NULL,
    city           character varying(50) NOT NULL,
    postalCode     character varying(6)  NOT NULL,
    streetName     character varying(50) NOT NULL,
    streetNumber   numeric(5)            NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.address OWNER to postgres;