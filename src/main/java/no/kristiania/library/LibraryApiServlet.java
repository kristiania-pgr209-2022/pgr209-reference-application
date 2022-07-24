package no.kristiania.library;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

public class LibraryApiServlet extends HttpServlet {

    private final BookRepository bookRepository = new BookRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if ("/books".equals(req.getPathInfo())) {
            resp.setContentType("text/html");
            String books = bookRepository.streamAllBooks().map(b -> "<li>" + b + "</li>")
                    .collect(Collectors.joining("\n"));
            resp.getWriter().write("<ul class='bookList'>" + books + "</ul>");
        } else {
            resp.getWriter().write("Unknown action " + req.getPathInfo());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if ("/books".equals(req.getPathInfo())) {
            String title = req.getParameter("title");
            String author = req.getParameter("author");
            bookRepository.insertBook(title, author);
            resp.sendRedirect("/books");
        } else {
            resp.getWriter().write("Unknown action " + req.getPathInfo());
        }
    }
}
