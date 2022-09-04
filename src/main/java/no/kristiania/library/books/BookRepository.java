package no.kristiania.library.books;

import no.kristiania.library.authors.Author;
import no.kristiania.library.authors.AuthorRepository;
import org.fluentjdbc.DatabaseRow;
import org.fluentjdbc.DbContext;
import org.fluentjdbc.DbContextJoinedSelectBuilder;
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
    private final DbContextTableAlias bookAlias;
    private final DbContextTableAlias authorshipAlias;
    private final DbContextTableAlias authorAlias;

    public BookRepository(DbContext dbContext) {
        table = dbContext.tableWithTimestamps("books");
        authorshipTable = dbContext.table("authorships");
        DbContextTable authorsTable = dbContext.table("authors");
        bookAlias = table.alias("b");
        authorshipAlias = authorshipTable.alias("ab");
        authorAlias = authorsTable.alias("a");
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
        return listAggregates(aggregateQuery()).values();
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

    public BookAggregate retrieveAggregate(long bookId) {
        var books = listAggregates(aggregateQuery().where(bookAlias.column("id"), bookId));
        return books.get(bookId);
    }

    private Map<Long, BookAggregate> listAggregates(DbContextJoinedSelectBuilder aggregateQuery) {
        Map<Long, BookAggregate> results = new HashMap<>();
        aggregateQuery.forEach(row -> {
            Long bookId = row.table(bookAlias).getLong("id");
            if (!results.containsKey(bookId)) {
                results.put(bookId, new BookAggregate(mapToBook(row.table(bookAlias))));
            }
            results.get(bookId).addAuthor(AuthorRepository.mapToAuthor(row.table(authorAlias)));
        });
        return results;
    }

    private DbContextJoinedSelectBuilder aggregateQuery() {
        return bookAlias.join(bookAlias.column("id"), authorshipAlias.column("book_id"))
                .join(authorshipAlias.column("author_id"), authorAlias.column("id"))
                .query();
    }
}
