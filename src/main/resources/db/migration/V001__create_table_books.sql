create table books
(
    id         serial primary key,
    title      varchar(200) not null,
    author     varchar(200),
    created_at date         not null default now(),
    updated_at date         not null default now()
)
