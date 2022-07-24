package no.kristiania.library;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BookRepository {

    private final List<String> books = new ArrayList<>(List.of("Book1", "Book2"));

    public Stream<String> streamAllBooks() {
        return books.stream();
    }

    public void insertBook(String title, String author) {
        books.add(title);
    }
}
