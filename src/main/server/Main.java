/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.server;

import main.io.Images;

import java.io.IOException;
import java.util.Scanner;

import static main.logs.Logs.log;

public class Main {

    public static void main( String[] args ) {

        /*
        Załadowanie obrazków dla servera (mapa graczy jest tworzona również w
        serwerze aby w momencie dołączenia się nowego klienta do gry poinformować go
        o istniejących już graczach)
         */

        Images images = new Images();
        try {
            images.loadAllImages();
        } catch (IOException e) {
            System.err.println("Loaded the images is failed");
            System.exit(0);
        }

        final int PORT = 8080;

        Server server = new Server();

        try {

            server.start(PORT); // start servera // Uruchamia wątek akceptujący servera oraz wątki dla każdego klienta
            log("server", "Server started on port: " + PORT);
            System.out.println("Server started on port: " + PORT + "\n" + "Waiting for events...");

        } catch (IOException ex) {
            log("server", ex.getMessage());
            System.out.println(ex.getMessage());
        }

        if (server.isStarted()){

            String choice;

            System.out.println("\nTURN OFF THE SERVER?? If yes write 'yes' otherwise do nothing");
            Scanner in = new Scanner(System.in);

            choice = in.next();
            in.close();

            if ("yes".equals(choice)) {
                server.stop();
                log("server", "Server was disconnected");
            }
        }
    }
}