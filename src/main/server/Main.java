/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.server;

import java.io.IOException;

public class Main {

    public static void main( String[] args ) {

        final int PORT = 8080;

        Server server = new Server();

        try {

            server.start(PORT); // start servera // Uruchamia wątek akceptujący servera oraz wątki dla każdego klienta
            System.out.println("Server started on port: " + PORT + "\n" + "Waiting for events...");

        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }

        //main.server.stop(); // zatrzymanie servera
    }
}