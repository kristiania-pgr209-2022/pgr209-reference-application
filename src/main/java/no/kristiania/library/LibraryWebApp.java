package no.kristiania.library;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

class LibraryWebApp implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext()
                .addServlet("libraryApiServlet", new LibraryApiServlet())
                .addMapping("/api/*");
    }
}
