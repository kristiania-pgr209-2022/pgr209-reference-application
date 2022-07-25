package no.kristiania.library.books;

import org.fluentjdbc.DatabaseRow;
import org.fluentjdbc.DbContext;
import org.fluentjdbc.DbContextTable;

import java.sql.SQLException;
import java.util.stream.Stream;

public class BookRepository {

    private final DbContextTable table;

    public BookRepository(DbContext dbContext) {
        table = dbContext.tableWithTimestamps("books");
    }

    public void insertBook(Book book) {
        var status = table.newSaveBuilder("id", book.getId())
                .setField("title", book.getTitle())
                .setField("author", book.getAuthor())
                .execute();
        book.setId(status.getId());
    }

    private Book mapToBook(DatabaseRow row) throws SQLException {
        Book book = new Book();
        book.setId(row.getLong("id"));
        book.setTitle(row.getString("title"));
        book.setAuthor(row.getString("author"));
        return book;
    }

    public Stream<Book> streamBooks() {
        return table.query().stream(this::mapToBook);
    }

    public Book retrieve(long bookId) {
        return table.where("id", bookId)
                .singleObject(this::mapToBook)
                .orElseThrow();
    }
}
