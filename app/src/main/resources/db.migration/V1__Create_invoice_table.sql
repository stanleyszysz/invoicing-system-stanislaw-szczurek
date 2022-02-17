CREATE TABLE public.invoice
(
--    id         UUID                  NOT NULL   DEFAULT uuid_generate_v4(),
    id         UUID                  NOT NULL   DEFAULT gen_random_uuid(),
    issue_date date                  NOT NULL,
    "number"   character varying(50) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.invoice owner to postgres;