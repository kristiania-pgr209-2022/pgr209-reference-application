package no.kristiania.library;

import java.util.List;
import java.util.stream.Stream;

public class BookRepository {

    private final List<String> books = List.of("Book1", "Book2");

    public Stream<String> streamAllBooks() {
        return books.stream();
    }
}
