package no.kristiania.library;

import org.eclipse.jetty.server.Server;

public class LibraryServer {
    private final Server server = new Server(0);

    public static void main(String[] args) throws Exception {
        new LibraryServer().start();
    }

    private void start() throws Exception {
        server.start();
        System.out.println("Started LibraryServer at " + server.getURI());
    }
}
