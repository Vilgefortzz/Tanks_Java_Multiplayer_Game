/*
 * Copyright (c) 2016.
 * @gklimek
 */

package client;

import connection.ConnectionHandling;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

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
            while (running){

                // Narazie wykonuje się to w nieskończoność
            }

            running = false;
            connected = false;

            // Tutaj trzeba jeszcze zamykać wszystkie strumienie + socketa (osobna klasa będzie do tego)

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

        ConnectionHandling.sendMessage(out, message);
    }

    public String receiveMessage(){

        return ConnectionHandling.receiveMessage(in);
    }
}