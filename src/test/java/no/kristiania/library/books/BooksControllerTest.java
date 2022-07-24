package no.kristiania.library.books;

import no.kristiania.library.infrastructure.TestDbContext;
import org.fluentjdbc.DbContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BooksControllerTest {

    private final BooksController controller;

    BooksControllerTest(@TestDbContext DbContext dbContext) {
        controller = new BooksController(dbContext);
    }

    @Test
    void shouldReturnSavedBooks() {
        String authorName = "Test Persson";
        String title = "The Title of the Book";
        controller.addBook(title, authorName);
        assertThat(controller.getBooks()).contains(title);
    }

}
