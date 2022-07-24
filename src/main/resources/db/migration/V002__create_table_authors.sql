create table authors
(
    id         serial primary key,
    created_at date         not null default now(),
    updated_at date         not null default now(),
    full_name  varchar(200) not null
)
