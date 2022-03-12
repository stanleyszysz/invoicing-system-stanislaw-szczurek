CREATE TABLE public.invoice_entry
(
    id                     UUID                     NOT NULL,
    description            character varying(50)    NOT NULL,
    price                  numeric(10, 2)           NOT NULL,
    vat_value              numeric(10, 2)           NOT NULL,
    vat_rate               numeric(3, 2)            NOT NULL,
    car_related_expense    UUID                     NOT NULL    REFERENCES car(id) ON DELETE CASCADE,
    invoice_id             UUID                     NOT NULL    REFERENCES invoice(id) ON DELETE CASCADE,
    PRIMARY KEY (id)
);
