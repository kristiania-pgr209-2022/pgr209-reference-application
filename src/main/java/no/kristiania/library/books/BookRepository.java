package no.kristiania.library.books;

import no.kristiania.library.authors.Author;
import no.kristiania.library.authors.AuthorRepository;
import org.fluentjdbc.DatabaseRow;
import org.fluentjdbc.DbContext;
import org.fluentjdbc.DbContextTable;
import org.fluentjdbc.DbContextTableAlias;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BookRepository {

    private final DbContextTable table;
    private final DbContextTable authorshipTable;
    private final DbContextTable authorsTable;

    public BookRepository(DbContext dbContext) {
        table = dbContext.tableWithTimestamps("books");
        authorshipTable = dbContext.table("authorships");
        authorsTable = dbContext.table("authors");
    }

    public void insertBook(Book book) {
        var status = table.newSaveBuilder("id", book.getId())
                .setField("title", book.getTitle())
                .execute();
        book.setId(status.getId());
    }

    private Book mapToBook(DatabaseRow row) throws SQLException {
        Book book = new Book();
        book.setId(row.getLong("id"));
        book.setTitle(row.getString("title"));
        return book;
    }

    public Collection<BookAggregate> listBooks() {
        Map<String, BookAggregate> results = new HashMap<>();
        DbContextTableAlias b = table.alias("b");
        DbContextTableAlias ab = authorshipTable.alias("ab");
        DbContextTableAlias a = authorsTable.alias("a");
        b.join(b.column("id"), ab.column("book_id"))
                .join(ab.column("author_id"), a.column("id"))
                .query()
                .forEach(row -> {
                    String bookId = row.table(b).getString("id");
                    if (!results.containsKey(bookId)) {
                        results.put(bookId, new BookAggregate(mapToBook(row.table(b))));
                    }
                    results.get(bookId).addAuthor(AuthorRepository.mapToAuthor(row.table(a)));
                });
        return results.values();
    }

    public Book retrieve(long bookId) {
        return table.where("id", bookId)
                .singleObject(this::mapToBook)
                .orElseThrow();
    }

    public void addBookAuthor(Book book, Author author) {
        authorshipTable.insert()
                .setField("book_id", book.getId())
                .setField("author_id", author.getId())
                .execute();
    }

    public Optional<Book> findByTitle(String title) {
        return table.where("title", title)
                .singleObject(this::mapToBook);
    }
}
