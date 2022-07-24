package no.kristiania.library;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BookRepository {

    private final List<Book> books = new ArrayList<>();

    public Stream<Book> streamBooks() {
        return books.stream();
    }

    public void insertBook(Book book) {
        books.add(book);
    }
}
