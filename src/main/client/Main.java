/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.client;

import main.gui.MainFrame;
import main.io.Images;

import javax.swing.*;

import java.io.IOException;

import static main.gui.MainFrame.sizeX;
import static main.gui.MainFrame.sizeY;

public class Main {

    public static void main( String[] args ) {

        /*
        Załadowanie obrazków dla clienta
         */

        Images images = new Images();
        try {
            images.loadAllImages();
        } catch (IOException e) {
            System.err.println("Loaded the images is failed");
            System.exit(0);
        }

        final MainFrame frame = new MainFrame(); // Stworzenie okna dla klienta (nie jest jeszcze widoczne)
        Client client = new Client(frame, frame.getGamePanel()); // stworzenie klienta do komunikacji -
                                                                // dostaje okno + panel z grą
        frame.setClient(client); // wysłanie klienta do okna, które go połączy z serverem po naciśnięciu buttona

        SwingUtilities.invokeLater(() -> {

            frame.setTitle("Client not logged now");
            frame.setSize(sizeX, sizeY);
            frame.setResizable(false);
            frame.setUndecorated(true);

            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}