package no.kristiania.library;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BookRepository {

    private final List<Book> books = new ArrayList<>();

    public Stream<String> streamAllBooks() {
        return books.stream().map(Book::getTitle);
    }

    public void insertBook(String title, String author) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        books.add(book);
    }
}
