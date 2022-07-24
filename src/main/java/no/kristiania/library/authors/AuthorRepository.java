package no.kristiania.library.authors;

import org.fluentjdbc.DatabaseRow;
import org.fluentjdbc.DatabaseSaveResult;
import org.fluentjdbc.DbContext;
import org.fluentjdbc.DbContextTable;

import java.sql.SQLException;
import java.util.stream.Stream;

public class AuthorRepository {

    private final DbContextTable table;

    public AuthorRepository(DbContext dbContext) {
        table = dbContext.tableWithTimestamps("authors");
    }

    public void save(Author author) {
        var status = table.newSaveBuilder("id", author.getId())
                .setField("full_name", author.getFullName())
                .execute();
        author.setId(status.getId());
    }

    public Stream<Author> streamAll() {
        return table.query().stream(this::mapToAuthor);
    }

    private Author mapToAuthor(DatabaseRow row) throws SQLException {
        Author author = new Author();
        author.setId(row.getLong("id"));
        author.setFullName(row.getString("full_name"));
        return author;
    }

    public Author retrieve(Long id) {
        return table.query().where("id", id).singleObject(this::mapToAuthor).orElseThrow();
    }
}
