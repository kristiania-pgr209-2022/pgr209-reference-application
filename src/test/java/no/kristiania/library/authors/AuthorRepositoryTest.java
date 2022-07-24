package no.kristiania.library.authors;


import no.kristiania.library.infrastructure.TestDbContext;
import org.fluentjdbc.DbContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorRepositoryTest {
    private final AuthorRepository repository;

    AuthorRepositoryTest(@TestDbContext DbContext dbContext) {
        repository = new AuthorRepository(dbContext);
    }

    @Test
    void shouldListSavedAuthors() {
        Author author = new Author();
        author.setFullName("Test Persson");
        repository.save(author);
        assertThat(repository.streamAll())
                .extracting(Author::getFullName)
                .contains(author.getFullName());
    }
}
