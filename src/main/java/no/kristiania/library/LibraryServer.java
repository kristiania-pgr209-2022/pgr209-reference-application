package no.kristiania.library;

import no.kristiania.library.infrastructure.DataSourceConfig;
import org.actioncontroller.config.ConfigObserver;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class LibraryServer {
    private static final Logger logger = LoggerFactory.getLogger(LibraryServer.class);

    private final Server server = new Server();
    private final ServerConnector connector = new ServerConnector(server);
    private final LibraryWebApp application = new LibraryWebApp();

    public static void main(String[] args) throws Exception {
        new LibraryServer().start();
    }

    private void start() throws Exception {
        new ConfigObserver("library")
                .onInetSocketAddress("http.port", 9080, this::setHttpPort)
                .onPrefixedValue("dataSource", new DataSourceConfig(), application::setDataSource)
        ;
        server.setHandler(createWebApp());
        server.start();
    }

    private void setHttpPort(InetSocketAddress address) throws Exception {
        connector.stop();
        connector.setPort(address.getPort());
        connector.start();
        logger.info("Started http://localhost:{}", connector.getPort());
    }

    private Handler createWebApp() {
        ServletContextHandler webapp = new ServletContextHandler();
        webapp.setContextPath("/");
        webapp.addEventListener(application);
        return webapp;
    }

}
