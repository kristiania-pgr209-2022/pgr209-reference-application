package no.kristiania.library.books;

import no.kristiania.library.authors.Author;
import no.kristiania.library.authors.AuthorRepository;
import org.fluentjdbc.DatabaseRow;
import org.fluentjdbc.DbContext;
import org.fluentjdbc.DbContextTable;

import java.sql.SQLException;
import java.util.stream.Stream;

public class BookRepository {

    private final DbContextTable table;
    private final DbContextTable authorshipTable;
    private final AuthorRepository authorRepository;

    public BookRepository(DbContext dbContext) {
        table = dbContext.tableWithTimestamps("books");
        authorshipTable = dbContext.table("authorships");
        authorRepository = new AuthorRepository(dbContext);
    }

    public void insertBook(Book book) {
        var status = table.newSaveBuilder("id", book.getId())
                .setField("title", book.getTitle())
                .setField("author", book.getAuthor())
                .execute();
        book.setId(status.getId());

        Author author = new Author();
        author.setFullName(book.getAuthor());
        authorRepository.save(author);

        addBookAuthor(book.getId(), author.getId());
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

    public void addBookAuthor(long id, long authorId) {
        authorshipTable.insert()
                .setField("book_id", id)
                .setField("author_id", authorId)
                .execute();
    }
}
