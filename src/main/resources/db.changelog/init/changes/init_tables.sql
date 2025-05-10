CREATE SEQUENCE IF NOT EXISTS public.battery_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

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