package no.kristiania.library;

import org.actioncontroller.actions.GET;
import org.actioncontroller.actions.POST;
import org.actioncontroller.values.ContentBody;
import org.actioncontroller.values.RequestParam;
import org.actioncontroller.values.SendRedirect;
import org.fluentjdbc.DbContext;

import java.util.stream.Collectors;

public class BooksController {

    private final BookRepository bookRepository;

    public BooksController(DbContext dbContext) {
        bookRepository = new BookRepository(dbContext);
    }

    @GET("/books")
    @ContentBody
    public String getBooks() {
        String books = bookRepository.streamBooks()
                .map(b -> "<li>" + b.getTitle() + "</li>")
                .collect(Collectors.joining("\n"));
        return "<ul class='bookList'>" + books + "</ul>";
    }

    @POST("/books")
    @SendRedirect("/books/")
    public void addBook(
            @RequestParam("title") String title,
            @RequestParam("author") String author
    ) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        bookRepository.insertBook(book);
    }
 }
