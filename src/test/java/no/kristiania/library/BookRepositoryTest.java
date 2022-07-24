package no.kristiania.library;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookRepositoryTest {

    private final BookRepository bookRepository = new BookRepository();

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
