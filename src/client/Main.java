/*
 * Copyright (c) 2016.
 * @gklimek
 */

package client;

import gui.MainFrame;

import javax.swing.*;

import static gui.MainFrame.sizeX;
import static gui.MainFrame.sizeY;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Main {

    public static void main( String[] args ) {

        //final String HOST; // to będzie na końcu wynikowo
        //final int PORT;

        final MainFrame frame = new MainFrame(); // Stworzenie okna dla klienta (nie jest jeszcze widoczne)
        Client client = new Client(frame, frame.getGamePanel()); // stworzenie klienta do komunikacji - dostaje okno + panel z grą
        frame.setClient(client); // wysłanie klienta(służącego komunikacji) do okna, które go połączy z serverem po naciśnięciu buttona

        SwingUtilities.invokeLater(() -> {

            frame.setTitle("Client not logged now");
            frame.setSize(sizeX, sizeY);
            frame.setResizable(false);
            frame.setUndecorated(true);

            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setVisible(true);
        });

        /*try {

            //final int port = Integer.parseInt(JOptionPane.showInputDialog("Enter port number where the server is connected"));
            //final String host = JOptionPane.showInputDialog("Enter IP address of a server\n");
            client.connect(HOST, PORT);

        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
        */
    }
}