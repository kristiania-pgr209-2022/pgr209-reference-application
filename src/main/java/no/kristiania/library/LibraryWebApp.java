package no.kristiania.library;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import no.kristiania.library.authors.AuthorsController;
import no.kristiania.library.books.BooksController;
import org.actioncontroller.jakarta.ContentServlet;
import org.actioncontroller.jakarta.ApiJakartaServlet;
import org.fluentjdbc.DbContext;

import javax.sql.DataSource;
import java.util.EnumSet;
import java.util.List;

class LibraryWebApp implements ServletContextListener {

    private final DbContext dbContext = new DbContext();
    private final LibraryFilter libraryFilter = new LibraryFilter(dbContext);
    private final ContentServlet contentServlet = new ContentServlet("/webapp");
    private final BooksController booksController = new BooksController(dbContext);
    private final AuthorsController authorsController = new AuthorsController(dbContext);
    private final ApiJakartaServlet apiServlet = new ApiJakartaServlet(List.of(booksController, authorsController));

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        context.addServlet("libraryApiServlet", apiServlet).addMapping("/api/*");
        context.addServlet("content", contentServlet).addMapping("/*");
        context.addFilter("libraryFilter", libraryFilter)
                .addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), false, "libraryApiServlet");
    }

    public void setDataSource(DataSource dataSource) {
        libraryFilter.setDataSource(dataSource);
    }
}
