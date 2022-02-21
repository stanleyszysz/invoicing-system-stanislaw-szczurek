ALTER TABLE public.company
    ADD COLUMN address bigint NOT NULL;

ALTER TABLE public.company
    ADD CONSTRAINT address_fk FOREIGN KEY (address)
        REFERENCES public.address (id);