package no.kristiania.library;

import org.eclipse.jetty.http.HttpFields;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BookRepository {

    private final List<String> bookTitles = new ArrayList<>(List.of("Book1", "Book2"));
    private final List<Book> books = new ArrayList<>();

    public Stream<String> streamAllBooks() {
        return books.stream().map(Book::getTitle);
    }

    public void insertBook(String title, String author) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        books.add(book);
        bookTitles.add(title);
    }
}
