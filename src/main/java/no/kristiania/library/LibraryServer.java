package no.kristiania.library;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryServer {
    private static final Logger logger = LoggerFactory.getLogger(LibraryServer.class);

    private final Server server = new Server(0);

    public static void main(String[] args) throws Exception {
        new LibraryServer().start();
    }

    private void start() throws Exception {
        server.setHandler(createWebApp());
        server.start();
        logger.info("Started {} at {}", this.getClass().getSimpleName(), server.getURI());
    }

    private WebAppContext createWebApp() {
        WebAppContext webapp = new WebAppContext(Resource.newClassPathResource("webapp"), "/");
        webapp.addEventListener(new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent event) {
                event.getServletContext()
                        .addServlet("libraryApiServlet", new LibraryApiServlet())
                        .addMapping("/api/*");
            }
        });
        return webapp;
    }
}
