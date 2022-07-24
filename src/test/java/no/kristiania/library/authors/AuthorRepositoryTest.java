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
    void shouldReturnSavedAuthor() {
        Author author = sampleAuthor();
        repository.save(author);
        assertThat(repository.retrieve(author.getId()))
                .usingRecursiveComparison()
                .isEqualTo(author);
    }

    @Test
    void shouldListSavedAuthors() {
        Author author = sampleAuthor();
        repository.save(author);
        assertThat(repository.streamAll())
                .extracting(Author::getFullName)
                .contains(author.getFullName());
    }

    private Author sampleAuthor() {
        Author author = new Author();
        author.setFullName("Test Persson");
        return author;
    }
}
