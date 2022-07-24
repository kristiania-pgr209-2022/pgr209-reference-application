package no.kristiania.library;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryServer {
    private static final Logger logger = LoggerFactory.getLogger(LibraryServer.class);

    private final Server server = new Server(0);
    private final LibraryWebApp listener = new LibraryWebApp();

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
        webapp.addEventListener(listener);
        return webapp;
    }

}
