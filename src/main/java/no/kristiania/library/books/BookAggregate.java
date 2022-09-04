package no.kristiania.library.books;

import no.kristiania.library.authors.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookAggregate {
    private final Book book;
    private final List<Author> authors = new ArrayList<>();

    public BookAggregate(Book book) {
        this.book = book;
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public Book getBook() {
        return book;
    }

    public String getAuthors() {
        return authors.stream().map(Author::getFullName).collect(Collectors.joining(", "));
    }
}
