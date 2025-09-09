CREATE TABLE IF NOT EXISTS roles
(
    id_rol bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(50) COLLATE pg_catalog."default",
    description character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT roles_pkey PRIMARY KEY (id_rol)
);

CREATE TABLE IF NOT EXISTS users
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(50) COLLATE pg_catalog."default",
    lastname character varying(50) COLLATE pg_catalog."default",
    birthdate timestamp without time zone,
    address character varying(100) COLLATE pg_catalog."default",
    phone character varying(12) COLLATE pg_catalog."default",
    email character varying(50) COLLATE pg_catalog."default",
    basesalary numeric,
    dni character varying(12) COLLATE pg_catalog."default",
    password character varying(200) COLLATE pg_catalog."default",
    id_rol bigint,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT id_rol FOREIGN KEY (id_rol)
        REFERENCES public.roles (id_rol) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);