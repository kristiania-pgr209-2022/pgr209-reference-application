package no.kristiania.library;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookRepositoryTest {

    private final BookRepository bookRepository = new BookRepository();

    @Test
    void shouldListInsertedBooks() {
        String title = "some title";
        bookRepository.insertBook(title, "some author");
        assertThat(bookRepository.streamAllBooks())
                .contains(title);
    }

}
