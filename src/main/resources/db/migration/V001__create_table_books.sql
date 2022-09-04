create table books
(
    id         serial primary key,
    created_at date         not null default now(),
    updated_at date         not null default now(),
    title      varchar(200) not null
)
