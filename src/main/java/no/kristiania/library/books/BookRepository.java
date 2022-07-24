package no.kristiania.library.books;

import org.fluentjdbc.DbContext;
import org.fluentjdbc.DbContextTable;

import java.util.stream.Stream;

public class BookRepository {

    private final DbContextTable table;

    public BookRepository(DbContext dbContext) {
        table = dbContext.tableWithTimestamps("books");
    }

    public Stream<Book> streamBooks() {
        return table.query().stream(row -> {
            Book book = new Book();
            book.setTitle(row.getString("title"));
            return book;
        });
    }

    public void insertBook(Book book) {
        var status = table.newSaveBuilder("id", book.getId())
                .setField("title", book.getTitle())
                .setField("author", book.getAuthor())
                .execute();
        book.setId(status.getId());
    }
}
