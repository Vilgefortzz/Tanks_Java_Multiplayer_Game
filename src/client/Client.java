/*
 * Copyright (c) 2016.
 * @gklimek
 */

package client;

import connection.ConnectionHandling;
import gui.GamePanel;
import gui.MainFrame;
import models.Player;

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
    Okno clienta oraz panel z grą
     */

    private MainFrame frame = null;
    private GamePanel game = null;

    /*
    Client dostaje playera do gry
     */

    private Player myPlayer = null;

    /* -------------------------------------------------------------------------------------------------------------- */


    public Client(MainFrame frame, GamePanel game) {

        this.frame = frame;
        this.game = game;

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

        myPlayer = new Player(1, "Grzes"); // TODO Trzeba zwiększać id playera żeby każdy klient był kolejnym id
        // Zarejestrowanie playera(main - sterowanie) na panelu
        game.registerMainPlayer(myPlayer.getId(), myPlayer.getLogin(), myPlayer.getX(), myPlayer.getY());

        // Wysłanie informacji o stworzonym kliencie pozostałym klientom
        try{

            out.writeByte(1);
            out.writeInt(myPlayer.getId());
            out.writeUTF(myPlayer.getLogin());
            out.writeInt(myPlayer.getX());
            out.writeInt(myPlayer.getY());
        } catch (IOException e){
            System.out.println("Loose connection, cannot send information to another clients");
        }

        this.clientThread = new Thread(() ->
        {
            boolean looseConnectionWithServer = false;

                try {

                    while (running){

                        eventListening();
                    }

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

    public void disconnect() {

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

    private void eventListening() throws IOException {

        // rodzaj komunikatu
        int communique = in.readByte();

        switch (communique)
        {
            case 1:
                registerHandling();

            case 2:
                unRegisterHandling();

            case 3:
                movementHandling();

            default:
        }
    }

    /*
    public void sendYourMove(int id, int dx, int dy){

        try {
            out.writeInt(id);
            out.writeInt(dx);
            out.writeInt(dy);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    private void registerHandling() throws IOException {

        // Zczytanie informacji o kliencie
        int id = in.readInt();
        String login = in.readUTF();
        int x = in.readInt();
        int y = in.readInt();

        game.registerAnotherPlayer(id, login, x, y);
    }

    private void unRegisterHandling() throws IOException {

        // Zczytanie id klienta, który opuścił grę
        int id = in.readInt();

        game.unRegisterPlayer(id);
    }

    private void movementHandling() throws IOException {

        // Zczytanie informacji o kliencie, który się poruszył
        int id = in.readInt();
        int dx = in.readInt();
        int dy = in.readInt();

        game.movePlayer(id, dx, dy);
    }
}