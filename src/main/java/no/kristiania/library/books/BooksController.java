package no.kristiania.library.books;

import no.kristiania.library.authors.Author;
import no.kristiania.library.authors.AuthorRepository;
import org.actioncontroller.actions.GET;
import org.actioncontroller.actions.POST;
import org.actioncontroller.exceptions.HttpRequestException;
import org.actioncontroller.values.ContentBody;
import org.actioncontroller.values.PathParam;
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
                .map(b -> """
                        <li>
                            <a href="/books/book.html?id=%s">%s</a> by %s
                        </li>
                        """.formatted(b.getId(), b.getTitle(), b.getAuthor()))
                .collect(Collectors.joining("\n"));
        return "<ul class='bookList'>" + books + "</ul>";
    }

    @GET("/books/book")
    @ContentBody
    public String getBook(@RequestParam("id") long bookId, @RequestParam("action") Optional<String> action) {
        Book book = bookRepository.retrieve(bookId);
        if (action.filter(s -> s.equals("edit")).isPresent()) {
            return ("""
                <h2>Edit %s</h2>
                <form method='POST' action='/api/books/%d'>
                    <div><input name="title" value="%s" />
                    <div><button>Submit</button></div>
                </form>
                """).formatted(book.getTitle(), book.getId(), book.getTitle());
        }
        return ("""
                <h2>%s</h2>
                <div>by %s</div>
                <div><a href='/books/book.html?id=%d&action=edit'>Edit</a></div>
                """).formatted(book.getTitle(), book.getAuthor(), book.getId());
    }

    @POST("/books")
    @SendRedirect("/books/")
    public void addBook(
            @RequestParam("title") String title,
            @RequestParam("authorId") Optional<Long> authorId,
            @RequestParam("author") Optional<String> authorName
    ) {
        Author author = getAuthor(authorId, authorName);

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author.getFullName());
        bookRepository.insertBook(book);
    }

    private Author getAuthor(Optional<Long> authorId, Optional<String> authorName) {
        if (authorName.isPresent() && authorId.isPresent()) {
            throw new HttpRequestException("Must only provide authorName OR authorId");
        }
        if (authorId.isPresent()) {
            return authorRepository.retrieve(authorId.get());
        } else if (authorName.isPresent()) {
            Author author = new Author();
            author.setFullName(authorName.get());
            authorRepository.save(author);
            return author;
        } else {
            throw new HttpRequestException("Missing authorId");
        }
    }

    @POST("/books/{bookId}")
    @SendRedirect("/books/")
    public void updateBook(@PathParam("bookId") long id, @RequestParam("title") String title) {
        Book book = bookRepository.retrieve(id);
        book.setTitle(title);
        bookRepository.insertBook(book);
    }
}
