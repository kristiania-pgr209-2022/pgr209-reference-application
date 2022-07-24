package no.kristiania.library.authors;

import org.fluentjdbc.DbContext;

import java.util.stream.Collectors;

public class AuthorsController {
    private final AuthorRepository repository;

    public AuthorsController(DbContext dbContext) {
        repository = new AuthorRepository(dbContext);
    }

    public String getAuthorsOptions() {
        return repository.streamAll()
                .map(a -> "<option value='" + a.getId() + "'>" + a.getFullName() + "</option>")
                .collect(Collectors.joining("\n"));
    }
}
