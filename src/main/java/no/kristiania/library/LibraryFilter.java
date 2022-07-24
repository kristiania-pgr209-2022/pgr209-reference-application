package no.kristiania.library;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.fluentjdbc.DbContext;
import org.fluentjdbc.DbContextConnection;

import javax.sql.DataSource;
import java.io.IOException;

public class LibraryFilter extends HttpFilter {
    private final DbContext dbContext;
    private DataSource dataSource;

    public LibraryFilter(DbContext dbContext) {
        this.dbContext = dbContext;
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try (DbContextConnection ignored = dbContext.startConnection(dataSource)) {
            chain.doFilter(req, res);
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
