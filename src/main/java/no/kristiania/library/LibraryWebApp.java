package no.kristiania.library;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.actioncontroller.jakarta.ContentServlet;
import org.actioncontroller.jakarta.ApiJakartaServlet;
import org.fluentjdbc.DbContext;

import javax.sql.DataSource;
import java.util.EnumSet;
import java.util.List;

class LibraryWebApp implements ServletContextListener {

    private final DbContext dbContext = new DbContext();
    private final BooksController booksController = new BooksController(dbContext);

    private final LibraryFilter libraryFilter = new LibraryFilter(dbContext);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        context.addServlet("libraryApiServlet", new ApiJakartaServlet(List.of(booksController)))
                .addMapping("/api/*");
        context.addServlet("content", new ContentServlet("/webapp"))
                .addMapping("/*");
        context.addFilter("libraryFilter", libraryFilter)
                .addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), false, "libraryApiServlet");
    }

    public void setDataSource(DataSource dataSource) {
        libraryFilter.setDataSource(dataSource);
    }
}
