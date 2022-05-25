--DROP DATABASE IF EXISTS market_db;

CREATE DATABASE market_db
WITH
OWNER = postgres
ENCODING = 'UTF8'
LC_COLLATE = 'Russian_Russia.1251'
LC_CTYPE = 'Russian_Russia.1251'
TABLESPACE = pg_default
CONNECTION LIMIT = -1;

CREATE TABLE IF NOT EXISTS public.roles
(
    roles_id integer NOT NULL DEFAULT nextval('roles_roles_id_seq'::regclass),
    role character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT roles_pkey PRIMARY KEY (roles_id)
    );

CREATE TABLE IF NOT EXISTS public.users
(
    users_id integer NOT NULL DEFAULT nextval('users_users_id_seq'::regclass),
    login character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    role_id integer,
    CONSTRAINT users_pkey PRIMARY KEY (users_id),
    CONSTRAINT users_role_id_fkey FOREIGN KEY (role_id)
    REFERENCES public.roles (roles_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );

CREATE TABLE IF NOT EXISTS public.register_actions
(
    register_actions_id integer NOT NULL DEFAULT nextval('register_actions_register_actions_id_seq'::regclass),
    user_id integer NOT NULL,
    date_time timestamp with time zone NOT NULL,
                            action character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT register_actions_pkey PRIMARY KEY (register_actions_id),
    CONSTRAINT register_actions_user_id_fkey FOREIGN KEY (user_id)
    REFERENCES public.users (users_id) MATCH SIMPLE
                        ON UPDATE NO ACTION
                        ON DELETE NO ACTION
    );
