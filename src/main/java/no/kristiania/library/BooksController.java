package no.kristiania.library;

import org.actioncontroller.actions.GET;
import org.actioncontroller.actions.POST;
import org.actioncontroller.values.ContentBody;
import org.actioncontroller.values.RequestParam;
import org.actioncontroller.values.SendRedirect;

import java.util.stream.Collectors;

public class BooksController {

    private final BookRepository bookRepository = new BookRepository();

    @GET("/books")
    @ContentBody
    public String getBooks() {
        String books = bookRepository.streamBookTitles().map(b -> "<li>" + b + "</li>")
                .collect(Collectors.joining("\n"));
        return "<ul class='bookList'>" + books + "</ul>";
    }

    @POST("/books")
    @SendRedirect("/books/")
    public void addBook(
            @RequestParam("title") String title,
            @RequestParam("author") String author
    ) {
        bookRepository.insertBook(title, author);
    }
 }
