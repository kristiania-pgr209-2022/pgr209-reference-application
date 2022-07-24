package no.kristiania.library.authors;

import org.fluentjdbc.DbContext;

public class AuthorsController {
    private final AuthorRepository repository;

    public AuthorsController(DbContext dbContext) {
        repository = new AuthorRepository(dbContext);
    }

    public String getAuthorsOptions() {
        return null;
    }
}
