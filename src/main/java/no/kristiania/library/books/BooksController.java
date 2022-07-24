package no.kristiania.library.books;

import no.kristiania.library.authors.Author;
import no.kristiania.library.authors.AuthorRepository;
import org.actioncontroller.actions.GET;
import org.actioncontroller.actions.POST;
import org.actioncontroller.values.ContentBody;
import org.actioncontroller.values.RequestParam;
import org.actioncontroller.values.SendRedirect;
import org.fluentjdbc.DbContext;

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
            @RequestParam("author") String authorName
    ) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(authorName);
        bookRepository.insertBook(book);

        Author author = new Author();
        author.setFullName(authorName);
        authorRepository.save(author);
    }
 }
