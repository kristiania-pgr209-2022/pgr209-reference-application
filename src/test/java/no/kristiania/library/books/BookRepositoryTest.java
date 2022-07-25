package no.kristiania.library.books;


import no.kristiania.library.infrastructure.TestDbContext;
import org.fluentjdbc.DbContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookRepositoryTest {

    private final BookRepository bookRepository;

    BookRepositoryTest(@TestDbContext DbContext dbContext) {
        bookRepository = new BookRepository(dbContext);
    }

    @Test
    void shouldRetrieveSavedBook() {
        Book book = sampleBook();
        bookRepository.insertBook(book);
        assertThat(bookRepository.retrieve(book.getId()))
                .usingRecursiveComparison()
                .isEqualTo(book);
    }

    @Test
    void shouldListInsertedBooks() {
        Book book = sampleBook();
        bookRepository.insertBook(book);
        assertThat(bookRepository.streamBooks())
                .extracting(Book::getTitle)
                .contains(book.getTitle());
    }

    private Book sampleBook() {
        Book book = new Book();
        book.setTitle("some title");
        book.setAuthor("some author");
        return book;
    }

}
