package no.kristiania.library;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryApiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if ("/books".equals(req.getPathInfo())) {
            resp.setContentType("text/html");
            String books = List.of("Book1", "Book2").stream().map(b -> "<li>" + b + "</li>")
                    .collect(Collectors.joining("\n"));
            resp.getWriter().write("<ul class='bookList'>" + books + "</ul>");
        } else {
            resp.getWriter().write("Unknown action " + req.getPathInfo());
        }
    }
}
