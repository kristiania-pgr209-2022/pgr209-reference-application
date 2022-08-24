create table authorships
(
    book_id   integer not null references books (id),
    author_id integer not null references authors (id),
    primary key (book_id, author_id)
)
