/*
 * Copyright (c) 2016.
 * @gklimek
 */

package client;

import java.io.IOException;

public class Main {

    public static void main( String[] args ) {

        final String HOST = "localhost";
        final int PORT = 8080;

        Client client = new Client();

        try {

            client.connect(HOST, PORT);
            System.out.println("Client connected on the port: " + PORT);

        } catch (IOException ex) {

            System.out.println(ex);
        }

        System.out.println(client.receiveMessage());
        client.sendMessage("I am client!!");
    }
}