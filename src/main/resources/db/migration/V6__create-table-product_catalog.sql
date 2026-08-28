create table if not exist product_catalog(
    id integer generated always as identity primary key,
    public_id UUID unique not null,
    credits integer not null,
    amount bigint not null
);