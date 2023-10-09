create table if not exists users
(
    id            bigserial primary key,
    name          text NOT NULL,
    creation_date timestamp WITH TIME ZONE,
    chat_id       bigint
);
create table if not exists currency
(
    id     bigserial primary key,
    symbol text NOT NULL,
    price numeric(30, 20),
    chat_id       bigint
);