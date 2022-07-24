package no.kristiania.library.books;

import no.kristiania.library.authors.Author;
import no.kristiania.library.authors.AuthorRepository;
import no.kristiania.library.authors.AuthorsController;
import no.kristiania.library.infrastructure.TestDbContext;
import org.fluentjdbc.DbContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BooksControllerTest {

    private final BooksController controller;
    private final DbContext dbContext;

    BooksControllerTest(@TestDbContext DbContext dbContext) {
        controller = new BooksController(dbContext);
        this.dbContext = dbContext;
    }

    @Test
    void shouldReturnSavedBooks() {
        String authorName = "Test Persson";
        String title = "The Title of the Book";
        controller.addBook(title, authorName);
        assertThat(controller.getBooks()).contains(title + " by " + authorName);
    }

    @Test
    void shouldCreateAuthorForNewBook() {
        String authorName = "Test Persson";
        controller.addBook("Irrelevant", authorName);
        AuthorsController authorsController = new AuthorsController(dbContext);
        assertThat(authorsController.getAuthorsOptions()).contains(authorName);
    }

    @Test
    void shouldUseExistingAuthorInformation() {
        AuthorRepository authorRepository = new AuthorRepository(dbContext);
        Author author = new Author();
        author.setFullName("Test Persson");
        authorRepository.save(author);

        String bookTitle = "Book title";
        controller.addBookWithExistingAuthor(bookTitle, author.getId());
        assertThat(controller.getBooks())
                .contains(bookTitle + " by " + author.getFullName());
    }
}
