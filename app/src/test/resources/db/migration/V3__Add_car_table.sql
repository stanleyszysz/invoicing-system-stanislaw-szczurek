CREATE TABLE public.car
(
    id                   UUID                  NOT NULL,
    registration_number  character varying(10) NOT NULL,
    personal_usage       boolean               NOT NULL     DEFAULT false,
    PRIMARY KEY (id)
);