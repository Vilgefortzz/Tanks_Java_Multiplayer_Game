/*
 * Copyright (c) 2016.
 * @gklimek
 */

package server;

import models.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    /*
    Informacje o stanie serwera
     */

    private boolean started; // informacja(czy włączony czy nie)
    private boolean running; // informacja (czy pracuje czy nie)

    /*
    Wątek servera
     */

    private Thread serverThread = null;

    /*
    Sockety do komunikacji
     */

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null; // (narazie 1 client)

    /*
    Protokoły komunikacji
     */

    private DataInputStream in = null; // wejściowy strumień danych od klienta
    private DataOutputStream out = null; // wyjściowy strumień danych od klienta

    /*
    Gracze
     */

    private Player Player1 = null; // gracz(klient) (narazie 1 gracz)

    /* -------------------------------------------------------------------------------------------------------------- */


    public Server() {

        started = false;
        running = false;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isRunning() {
        return running;
    }

    public void start(int port) throws IOException{

        if (started){
            throw new IOException("Server is started, you can't start it again");
        }

        try {
            serverSocket = new ServerSocket(port); // utworzenie serverSocketa na danym porcie
        } catch (IOException ex){
            throw new IOException("Server didn't start, check port availability");
        }

        serverThread = new Thread(() -> {

            while(running){

                try {

                    clientSocket = serverSocket.accept();
                    in = new DataInputStream(clientSocket.getInputStream());
                    out = new DataOutputStream(clientSocket.getOutputStream());

                } catch (IOException e){

                    running = false;

                    try {
                        serverSocket.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });

        started = true;
        running = true;
        serverThread.start();
    }

    public void stop()
    {
        if (started)
        {
            running = false; // zatrzymanie pętli
            started = false; // wyłączenie servera

            try {
                serverSocket.close(); // zamknięcie socketa
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                serverThread.join(); // czekanie aż wątek się wykonana do końca
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}