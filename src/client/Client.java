/*
 * Copyright (c) 2016.
 * @gklimek
 */

package client;

import connection.ConnectionHandling;
import gui.MainFrame;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    /*
    Identyfikacja clienta
     */

    private int ID;
    private String LOGIN;

    /*
    Informacje o stanie klienta
     */

    private boolean connected; // informacja(czy połączony czy nie)
    private boolean running; // informacja (czy działa czy nie)

    /*
    Wątek klienta
     */

    private Thread clientThread = null;

    /*
    Socket do komunikacji
     */

    private Socket clientSocket = null; // (narazie 1 client)

    /*
    Protokoły komunikacji
     */

    private DataInputStream in = null; // wejściowy strumień danych od servera
    private DataOutputStream out = null; // wyjściowy strumień danych do servera

    /*
    Okno clienta
     */

    private MainFrame frame = null;

    /* -------------------------------------------------------------------------------------------------------------- */


    public Client() {

        connected = false;
        running = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isRunning() {
        return running;
    }

    public void setFrame(MainFrame frame) {
        this.frame = frame;
    }

    public void connect(String host, int port) throws IOException{

        if (connected){
            throw new IOException("Client is connected, you can't connect him again");
        }

        try {

            clientSocket = new Socket(host, port); // utworzenie socketa oraz połączenie go z serverem
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

        } catch (IOException ex){
            throw new IOException("Client didn't connect with server");
        }

        this.clientThread = new Thread(() ->
        {
            boolean looseConnectionWithServer = false;

                try {

                    while (running){

                    }
                    throw new IOException("Never throw!");

                } catch (IOException e) {
                   looseConnectionWithServer = true;
                } finally {

                    running = false; // zatrzymanie pętli

                    ConnectionHandling.close(out); // zamykanie strumienia wyjściowego
                    ConnectionHandling.close(in); // zamykanie strumienia wejściowego
                    ConnectionHandling.close(clientSocket); // zamknięcie socketa

                    connected = false; // odłączenie klienta

                    System.out.println("Jestem");
                    if (looseConnectionWithServer){

                        frame.connectionError();
                    }

                }
        });

        connected = true;
        running = true;
        clientThread.start();
    }

    public void disconnect()
    {
        if (connected)
        {
            running = false; // zatrzymanie pętli

            ConnectionHandling.close(out); // zamykanie strumienia wyjściowego
            ConnectionHandling.close(in); // zamykanie strumienia wejściowego
            ConnectionHandling.close(clientSocket); // zamknięcie socketa

            ConnectionHandling.join(clientThread); // czekanie aż wątek się wykonana do końca

            connected = false; // odłączenie klienta
        }
    }

    public void sendMessage(String message){

        try {
            ConnectionHandling.sendMessage(out, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage() throws IOException {

        try {
            ConnectionHandling.receiveMessage(in);
        } catch (IOException e) {
            throw e;
        }
    }
}