package no.kristiania.library.books;

import no.kristiania.library.authors.Author;
import no.kristiania.library.authors.AuthorRepository;
import no.kristiania.library.authors.AuthorsController;
import no.kristiania.library.infrastructure.TestDbContext;
import org.fluentjdbc.DbContext;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class BooksControllerTest {

    private final BooksController controller;
    private final DbContext dbContext;
    private final AuthorRepository authorRepository;
    private final BookRepository repository;

    BooksControllerTest(@TestDbContext DbContext dbContext) {
        controller = new BooksController(dbContext);
        this.dbContext = dbContext;
        authorRepository = new AuthorRepository(this.dbContext);
        repository = new BookRepository(this.dbContext);
    }

    @Test
    void shouldReturnSavedBooks() {
        String authorName = "Test Persson";
        String title = "The Title of the Book";
        controller.addBook(title, Optional.empty(), Optional.of(authorName));
        assertThat(controller.getBooks()).contains(title + "</a> by " + authorName);
    }

    @Test
    public void shouldUpdateBookTitle() {
        String oldTitle = "Old title " + Math.random()*10000;
        controller.addBook(oldTitle, Optional.empty(), Optional.of("Author Name"));
        long bookId = repository.findByTitle(oldTitle).orElseThrow().getId();

        controller.updateBook(bookId, "Updated title");
        assertThat(controller.getBook(bookId, Optional.empty()))
                .contains(">Updated title<");
    }

    @Test
    void shouldCreateAuthorForNewBook() {
        String authorName = "Test Persson";
        controller.addBook("Irrelevant", Optional.empty(), Optional.of(authorName));
        AuthorsController authorsController = new AuthorsController(dbContext);
        assertThat(authorsController.getAuthorsOptions()).contains(authorName);
    }

    @Test
    void shouldUseExistingAuthorInformation() {
        Author author = new Author();
        author.setFullName("Test Persson");
        authorRepository.save(author);

        String bookTitle = "Book title";
        controller.addBook(bookTitle, Optional.of(author.getId()), Optional.empty());
        assertThat(controller.getBooks())
                .contains(bookTitle + "</a> by " + author.getFullName());
    }

    @Test
    void shouldAddNewAuthorToBook() {
        String title = "Unique title " + (Math.random()*10000);
        String firstAuthor = "First Author";
        controller.addBook(title, Optional.empty(), Optional.of(firstAuthor));

        long bookId = repository.findByTitle(title).orElseThrow().getId();

        Author author = new Author();
        author.setFullName("Test Author");
        authorRepository.save(author);

        controller.addBookAuthor(bookId, author.getId());
        assertThat(authorRepository.listByBook(bookId))
                .extracting(Author::getFullName)
                .containsExactlyInAnyOrder(firstAuthor, author.getFullName());
    }
}
