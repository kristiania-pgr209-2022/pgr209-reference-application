package no.kristiania.library;

import org.fluentjdbc.DbContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BookRepository {

    private final DbContext dbContext;

    private final List<Book> books = new ArrayList<>();

    public BookRepository(DbContext dbContext) {
        this.dbContext = dbContext;
    }

    public Stream<Book> streamBooks() {
        return books.stream();
    }

    public void insertBook(Book book) {
        books.add(book);
    }
}
