CREATE TABLE public.vat
(
    id      bigserial               NOT NULL,
    name    character varying(6)    NOT NULL,
    rate    numeric(3, 2)           NOT NULL,
    PRIMARY KEY (id)
);

insert into public.vat (name, rate)
values ('VAT_0', 0.00),
       ('VAT_5', 0.08),
       ('VAT_8', 0.05),
       ('VAT_23', 0.23);