CREATE TABLE public.vat
(
    id   bigserial     NOT NULL,
    name character varying(2),
    rate numeric(3, 2) NOT NULL,
    PRIMARY KEY (id)
);

insert into public.vat (name, rate)
values ('0', 0.00),
       ('5', 0.05),
       ('8', 0.08),
       ('23', 0.23);

ALTER TABLE public.vat OWNER to postgres;
