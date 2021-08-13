package main.java.com.company.hit.server;

import main.java.com.company.hit.util.CLI;

import java.io.IOException;
import java.lang.*;

public class CacheUnitServerDriver {
    public static void main(String[] args) throws IOException {
        CLI cli = new CLI(System.in, System.out);
        Server server = new Server();
        cli.addPropertyChangeListener(server);
        new Thread(cli).start();
    }
}
