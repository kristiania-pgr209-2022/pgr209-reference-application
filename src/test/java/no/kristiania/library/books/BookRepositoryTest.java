package no.kristiania.library.books;


import no.kristiania.library.authors.Author;
import no.kristiania.library.authors.AuthorRepository;
import no.kristiania.library.authors.AuthorRepositoryTest;
import no.kristiania.library.infrastructure.TestDbContext;
import org.fluentjdbc.DbContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookRepositoryTest {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    BookRepositoryTest(@TestDbContext DbContext dbContext) {
        bookRepository = new BookRepository(dbContext);
        authorRepository = new AuthorRepository(dbContext);
    }

    @Test
    void shouldRetrieveSavedBook() {
        Author author = AuthorRepositoryTest.sampleAuthor();
        authorRepository.save(author);

        Book book = sampleBook();
        bookRepository.insertBook(book);
        bookRepository.addBookAuthor(book, author);
        assertThat(bookRepository.retrieve(book.getId()))
                .usingRecursiveComparison()
                .isEqualTo(book);
    }

    @Test
    void shouldListInsertedBooks() {
        Author author = AuthorRepositoryTest.sampleAuthor();
        authorRepository.save(author);

        Book book = sampleBook();
        bookRepository.insertBook(book);
        bookRepository.addBookAuthor(book, author);
        assertThat(bookRepository.listBooks())
                .extracting(b -> b.getBook().getTitle())
                .contains(book.getTitle());
    }

    public static Book sampleBook() {
        Book book = new Book();
        book.setTitle("some title");
        return book;
    }

}
