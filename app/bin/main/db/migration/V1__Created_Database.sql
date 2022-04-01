    create table address (
       address_id uuid not null,
        city varchar(255),
        postal_code varchar(255),
        street_name varchar(255),
        street_number varchar(255),
        primary key (address_id)
    );

    create table car (
       id uuid not null,
        personal_usage boolean not null,
        registration_number varchar(255),
        primary key (id)
    );

    create table company (
       company_id uuid not null,
        health_insurance numeric(19, 2),
        name varchar(255),
        pension_insurance numeric(19, 2),
        tax_identifier varchar(255),
        address_id uuid,
        primary key (company_id)
    );

    create table invoice (
       id uuid not null,
        date_at date,
        invoice_number varchar(255),
        buyer uuid,
        seller uuid,
        primary key (id)
    );

    create table invoice_entry (
       id uuid not null,
        description varchar(255),
        price numeric(19, 2),
        vat_rate int4,
        vat_value numeric(19, 2),
        car_related_expense uuid,
        invoice_id uuid not null,
        primary key (id)
    );

    alter table if exists company 
       add constraint FK_address
       foreign key (address_id) 
       references address;

    alter table if exists invoice 
       add constraint FK_company_buyer
       foreign key (buyer) 
       references company;

    alter table if exists invoice 
       add constraint FK_company_seller
       foreign key (seller) 
       references company;

    alter table if exists invoice_entry 
       add constraint FK_car
       foreign key (car_related_expense) 
       references car;

    alter table if exists invoice_entry 
       add constraint FK_invoice
       foreign key (invoice_id) 
       references invoice