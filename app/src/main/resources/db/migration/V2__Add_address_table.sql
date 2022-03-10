CREATE TABLE public.address
(
    company_id      UUID                  NOT NULL,
    city            character varying(50) NOT NULL,
    postal_code     character varying(6)  NOT NULL,
    street_name     character varying(50) NOT NULL,
    street_number   character varying(5)  NOT NULL,
    PRIMARY KEY (company_id),
    CONSTRAINT fk_company_id FOREIGN KEY (company_id) REFERENCES company (id)  ON DELETE CASCADE
);

--ALTER TABLE public.address OWNER to postgres;