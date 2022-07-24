package no.kristiania.library.books;

import no.kristiania.library.authors.Author;
import no.kristiania.library.authors.AuthorRepository;
import org.actioncontroller.actions.GET;
import org.actioncontroller.actions.POST;
import org.actioncontroller.exceptions.HttpRequestException;
import org.actioncontroller.values.ContentBody;
import org.actioncontroller.values.RequestParam;
import org.actioncontroller.values.SendRedirect;
import org.fluentjdbc.DbContext;

import java.util.Optional;
import java.util.stream.Collectors;

public class BooksController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BooksController(DbContext dbContext) {
        bookRepository = new BookRepository(dbContext);
        authorRepository = new AuthorRepository(dbContext);
    }

    @GET("/books")
    @ContentBody
    public String getBooks() {
        String books = bookRepository.streamBooks()
                .map(b -> "<li>" + b.getTitle() + " by " + b.getAuthor() + "</li>")
                .collect(Collectors.joining("\n"));
        return "<ul class='bookList'>" + books + "</ul>";
    }

    @POST("/books")
    @SendRedirect("/books/")
    public void addBook(
            @RequestParam("title") String title,
            @RequestParam("author") Optional<String> authorName,
            @RequestParam("authorId") Optional<Long> authorId
    ) {
        if (authorName.isPresent() && authorId.isPresent()) {
            throw new HttpRequestException("Must only provide authorName OR authorId");
        }
        Author author;
        if (authorId.isPresent()) {
            author = authorRepository.retrieve(authorId.get());
        } else if (authorName.isPresent()) {
            author = new Author();
            author.setFullName(authorName.get());
            authorRepository.save(author);
        } else {
            throw new HttpRequestException("Missing authorId");
        }

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author.getFullName());
        bookRepository.insertBook(book);
    }
}
