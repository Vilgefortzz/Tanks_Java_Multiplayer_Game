/*
 * Copyright (c) 2016.
 * @gklimek
 */

package server;

import java.io.IOException;

public class Main {

    public static void main( String[] args ) {

        int PORT = 8080;

        Server server = new Server();

        try
        {
            server.start(PORT); // start servera
            System.out.println("Server started on port: " + PORT + "\n" + "Waiting for events...");
        }
        catch (IOException ex)
        {
            System.out.println(ex);
        }
        server.stop(); // zatrzymanie servera
    }
}