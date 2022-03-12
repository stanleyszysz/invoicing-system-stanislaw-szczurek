CREATE TABLE public.company
(
    id                UUID                   NOT NULL,
    tax_identifier    character varying(10)  NOT NULL,
    name              character varying(100) NOT NULL,
    pension_insurance numeric(10, 2)         NOT NULL DEFAULT 0,
    health_insurance  numeric(10, 2)         NOT NULL DEFAULT 0,
    address_id        UUID                   NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (tax_identifier),
    CONSTRAINT fk_address_id FOREIGN KEY (address_id) REFERENCES address (id) ON DELETE CASCADE
);

--ALTER TABLE public.company OWNER to postgres;