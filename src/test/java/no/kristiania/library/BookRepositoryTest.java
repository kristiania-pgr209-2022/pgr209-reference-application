package no.kristiania.library;


import no.kristiania.library.books.Book;
import no.kristiania.library.books.BookRepository;
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
