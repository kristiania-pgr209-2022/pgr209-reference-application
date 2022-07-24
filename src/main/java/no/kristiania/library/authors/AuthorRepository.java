package no.kristiania.library.authors;

import org.fluentjdbc.DbContext;

import java.util.stream.Stream;

public class AuthorRepository {
    public AuthorRepository(DbContext dbContext) {

    }

    public void save(Author author) {

    }

    public Stream<Author> streamAll() {
        return null;
    }
}
