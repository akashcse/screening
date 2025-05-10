CREATE TABLE IF NOT EXISTS public.batteries
(
    id SERIAL PRIMARY KEY NOT NULL ,
    created_dt timestamp(6) without time zone NOT NULL,
    is_deleted boolean NOT NULL,
    updated_dt timestamp(6) without time zone NOT NULL,
    capacity real NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    postcode character varying(255) COLLATE pg_catalog."default" NOT NULL
);

Insert into public.batteries (id, name, capacity, postcode, is_deleted, created_dt,updated_dt)
Values (1000,	'Cannington',	13500,	'6107',	false,	'2024-03-19 15:38:29.817876+06','2024-03-19 15:38:29.817876+06'),
(1001,	'Midland',	50500,	'6057',	false,	'2024-03-19 15:38:29.817876+06','2024-03-19 15:38:29.817876+06');