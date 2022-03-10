CREATE TABLE public.invoice
(
    id                  UUID                    NOT NULL,
    date_at             date                    NOT NULL,
    invoice_number      character varying(50)   NOT NULL,
    seller              UUID                    NOT NULL   references company (id) ON DELETE CASCADE,
    buyer               UUID                    NOT NULL   references company (id) ON DELETE CASCADE,
    PRIMARY KEY (id),
    UNIQUE (invoice_number)
);

--ALTER TABLE public.invoice OWNER to postgres;