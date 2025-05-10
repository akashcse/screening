CREATE TABLE IF NOT EXISTS public.batteries
(
    id SERIAL PRIMARY KEY NOT NULL ,
    created_dt timestamp(6) without time zone NOT NULL,
    is_deleted boolean NOT NULL,
    updated_dt timestamp(6) without time zone NOT NULL,
    capacity real NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    postcode character varying(255) COLLATE pg_catalog."default" NOT NULL
)