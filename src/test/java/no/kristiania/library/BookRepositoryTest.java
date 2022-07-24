package no.kristiania.library;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookRepositoryTest {

    private final BookRepository bookRepository = new BookRepository();

    @Test
    void shouldListInsertedBooks() {
        String title = "some title";
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor("some author");
        bookRepository.insertBook(book);
        assertThat(bookRepository.streamBooks().map(Book::getTitle))
                .contains(title);
    }

}
