/*
 * Copyright (c) 2016.
 * @gklimek
 */

package client;

import utilities.Utilities;
import gui.GamePanel;
import gui.MainFrame;
import models.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static database.Database.registeredUsers;
import static gui.MainFrame.yourLogin;

public class Client {

    /*
    Informacje o stanie klienta
     */

    private boolean connected = false; // informacja(czy połączony czy nie)
    private boolean running = false; // informacja (czy działa czy nie)

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

    private static DataInputStream in = null; // wejściowy strumień danych od servera
    private static DataOutputStream out = null; // wyjściowy strumień danych do servera

    /*
    Okno clienta oraz panel z grą
     */

    private MainFrame frame = null;
    private GamePanel game = null;

    /*
    Client dostaje playera do gry
     */

    public static Player myPlayer = null;

    /* -------------------------------------------------------------------------------------------------------------- */


    public Client(MainFrame frame, GamePanel game) {

        this.frame = frame;
        this.game = game;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isRunning() {
        return running;
    }

    private MainFrame getFrame() {
        return frame;
    }

    public void connect(String host, int port) throws IOException{

        if (connected){
            throw new IOException("Client is connected, you can't connect him again");
        }

        try {

            clientSocket = new Socket(host, port); // utworzenie socketa oraz połączenie go z serverem
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());

        } catch (IOException ex){
            if (out != null)
                Utilities.closingSocketsAndStreams(out);
            if (in != null)
                Utilities.closingSocketsAndStreams(in);
            if (clientSocket != null)
                Utilities.closingSocketsAndStreams(clientSocket);
            throw new IOException("Client didn't connect with server", ex);
        }

        // Stworzenie playera o unikalnym id przynależnym do niego z bazy danych
        myPlayer = new Player(registeredUsers.get(yourLogin).getId());
        System.out.println("Player id: " + myPlayer.getId());
        myPlayer.setClient(this);
        myPlayer.setFrame(this.getFrame());

        sendYourId(myPlayer.getId()); // wysłanie do serwera, który go zapamięta (WAŻNE!!)

        this.clientThread = new Thread(() -> {

                try {
                    while (running){
                        eventListening();
                    }
                } catch (IOException e) {
                    running = false; // zatrzymanie pętli
                } finally {

                    System.out.println("Sprzatanie po kliencie");

                    Utilities.closingSocketsAndStreams(out); // zamykanie strumienia wyjściowego
                    Utilities.closingSocketsAndStreams(in); // zamykanie strumienia wejściowego
                    Utilities.closingSocketsAndStreams(clientSocket); // zamknięcie socketa

                    connected = false; // odłączenie klienta

                    game.setLooping(false); // wątek rysujący przestaje działać
                    Utilities.join(game.gameLoop);
                    game.cleanGamePanel(); // usunięcie wszystkich playerów z listy

                    game.setVisible(false);
                    frame.remove(game);

                    frame.getMenuPanel().setVisible(true);
                    frame.boxLoggedUser.setVisible(true);
                    frame.getMenuPanel().add(frame.boxLoggedUser);

                    frame.setTitle("Client logged as: " + yourLogin);
                    frame.add(frame.getMenuPanel());
                }
        });

        connected = true;
        running = true;
        clientThread.start();

        // Wysłanie informacji serwerowi o utworzonym graczu
        sendYourRegister(myPlayer.getId(), myPlayer.getOrientation(), myPlayer.getX(), myPlayer.getY());
        System.out.println("Wysylam");
    }

    public void disconnect() {

        if (connected) {
            Utilities.closingSocketsAndStreams(in); // zamknięcie strumienia wejściowego czyli wyskoczenie z pętli
                                                    // przez rzucenie wyjątku
        }
    }

    private void eventListening() throws IOException {

        // rodzaj komunikatu
        int communique = in.readInt();

        switch (communique) {

            case 1:
                registerHandling();
                break;

            case 2:
                unRegisterHandling();
                break;

            case 3:
                movementHandling();
                break;

            case 4:
                fireHandling();
                break;

            case 5:
                colissionTankWithWallHandling();
                break;

            case 6:
                respawnHandling();
                break;

            case 7:
                destroyedByHandling();
                break;

            default:
                break;
        }
    }

    private void sendYourId(int id) throws IOException {

        // Wysłanie serwerowi głównego id aby serwer zapamiętał go
        try {
            out.writeInt(id);
            out.flush();
        } catch (IOException e) {
            // TODO LOGS writing
            throw new IOException("Loose connection, cannot send information about id of new player to server", e);
        }
    }

    private void sendYourRegister(int id, int orientation, int x, int y) throws IOException {

        try {
            out.writeInt(1);
            out.writeInt(id);
            out.writeInt(orientation);
            out.writeInt(x);
            out.writeInt(y);
            out.flush();
        } catch (IOException e) {
            // TODO LOGS writing
            throw new IOException("Loose connection, cannot send information about new player to another clients", e);
        }
    }

    public void sendYourUnRegister(int id) {

        try {
            out.writeInt(2);
            out.writeInt(id);
            out.flush();
        } catch (IOException e) {
            // TODO LOGS writing
            e.printStackTrace();
        }
    }

    public static void sendYourMove(int id, int orientation, int dx, int dy){

        try {
            out.writeInt(3);
            out.writeInt(id);
            out.writeInt(orientation);
            out.writeInt(dx);
            out.writeInt(dy);
            out.flush();
        } catch (IOException e) {
            // TODO LOGS writing
            e.printStackTrace();
        }
    }

    public static void sendYourFire(int id, int orientation){

        try {
            out.writeInt(4);
            out.writeInt(id);
            out.writeInt(orientation);
        } catch (IOException e) {
            // TODO LOGS writing
        }
    }

    public static void sendYourCollisionTankWithWall(int id){

        try {
            out.writeInt(5);
            out.writeInt(id);
        } catch (IOException e) {
            // TODO LOGS writing
            e.printStackTrace();
        }
    }

    public static void sendYourRespawn(int id, int x, int y){

        try {
            out.writeInt(6);
            out.writeInt(id);
            out.writeInt(x);
            out.writeInt(y);
        } catch (IOException e) {
            // TODO LOGS writing
            e.printStackTrace();
        }
    }

    public static void sendDestroyedBy(int id){

        try {
            out.writeInt(7);
            out.writeInt(id);
        } catch (IOException e) {
            // TODO LOGS writing
            e.printStackTrace();
        }
    }

    private void registerHandling() throws IOException {

        // Zczytanie informacji o kliencie
        int id = in.readInt();
        int orientation = in.readInt();
        int x = in.readInt();
        int y = in.readInt();

        game.registerPlayer(id, orientation, x, y);
    }

    private void unRegisterHandling() throws IOException {

        // Zczytanie id klienta, który opuścił grę
        int id = in.readInt();

        game.unRegisterPlayer(id);
    }

    private void movementHandling() throws IOException {

        // Zczytanie informacji o kliencie, który się poruszył
        int id = in.readInt();
        int orientation = in.readInt();
        int dx = in.readInt();
        int dy = in.readInt();

        game.movePlayer(id, orientation, dx, dy);
    }

    private void fireHandling() throws IOException {

        // Zczytanie id klienta, który strzelił
        int id = in.readInt();
        int orientation = in.readInt();

        game.firePlayer(id, orientation);
    }

    private void respawnHandling() throws IOException {

        // Zczytanie id klienta, który został zniszczony
        int id = in.readInt();
        int x = in.readInt();
        int y = in.readInt();

        game.respawnPlayer(id, x, y);
    }

    private void destroyedByHandling() throws IOException {

        // Zczytanie id klienta, którego pocisk zniszczył wrogi czołg
        int id = in.readInt();

        game.destroyedByPlayer(id);
    }

    /*
    Collisions
     */

    private void colissionTankWithWallHandling() throws IOException {

        // Zczytanie id klienta, który zderzył się z ścianą
        int id = in.readInt();

        game.collisionTankWithWall(id);
    }
}