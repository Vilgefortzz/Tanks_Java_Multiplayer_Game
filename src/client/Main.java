/*
 * Copyright (c) 2016.
 * @gklimek
 */

package client;

import gui.MainFrame;

import javax.swing.*;
import java.io.IOException;
import java.util.Random;

import static gui.MainFrame.sizeX;
import static gui.MainFrame.sizeY;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Main {

    public static void main( String[] args ) {

        final String HOST = "localhost";
        final int PORT = 8080;

        Client client = new Client();

        final MainFrame frame = new MainFrame(); // Stworzenie okna dla połączonego klienta
        client.setFrame(frame); // Przekazanie okna klientowi aby mógł informować o eventach

        try {

            client.connect(HOST, PORT);
            System.out.println("Client connected on the port: " + PORT);

        } catch (IOException ex) {

            System.out.println(ex);
        }

        if (client.isConnected()){

            SwingUtilities.invokeLater(() -> {

                frame.setTitle("Client: " + (new Random().nextInt(301) + 1));
                frame.setSize(sizeX, sizeY);
                frame.setResizable(false);

                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                frame.setVisible(true);
            });
        }
    }
}