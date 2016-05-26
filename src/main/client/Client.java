/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.client;

import main.database.Database;
import main.gui.GamePanel;
import main.gui.MainFrame;
import main.models.Player;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

import static main.gui.MainFrame.yourLogin;
import static main.logs.Logs.log;
import static main.utilities.Utilities.closingSocketsAndStreams;
import static main.utilities.Utilities.join;

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

    private Socket clientSocket = null; // (narazie 1 main.client)

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
    Baza danych
     */

    public static Database database = null;

    /*
    Client dostaje playera do gry
     */

    public static Player myPlayer = null;

    /* -------------------------------------------------------------------------------------------------------------- */


    public Client(MainFrame frame, GamePanel game) {

        this.frame = frame;
        this.game = game;
        database = new Database();

        // Połączenie z bazą danych

        try {
            database.connectToDatabase();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            log("client", e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
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
                closingSocketsAndStreams(out);
            if (in != null)
                closingSocketsAndStreams(in);
            if (clientSocket != null)
                closingSocketsAndStreams(clientSocket);
            throw new IOException("Client didn't connect with server", ex);
        }

        // Stworzenie playera o unikalnym id przynależnym do niego z bazy danych
        int loggedUserID = database.takeID(yourLogin);

        if (loggedUserID != 0){

            myPlayer = new Player(loggedUserID);
            System.out.println("Player id: " + myPlayer.getId());
            myPlayer.setClient(this);
        }
        else
            System.exit(0);

        sendYourId(myPlayer.getId()); // wysłanie do serwera id, który go zapamięta (WAŻNE!!)
        sendYourLogin(yourLogin); // wysłanie do serwera loginu, który go zapamięta (WAŻNE!!)

        this.clientThread = new Thread(() -> {

                try {
                    while (running){
                        eventListening();
                    }
                } catch (IOException e) {
                    running = false; // zatrzymanie pętli
                } finally {

                    System.out.println("Sprzatanie po kliencie");

                    closingSocketsAndStreams(out); // zamykanie strumienia wyjściowego
                    closingSocketsAndStreams(in); // zamykanie strumienia wejściowego
                    closingSocketsAndStreams(clientSocket); // zamknięcie socketa

                    connected = false; // odłączenie klienta
                    game.setLooping(false); // wątek rysujący przestaje działać
                    join(game.gameLoop); // czekanie aż się wykona do końca
                    game.cleanGamePanel(); // usunięcie wszystkich playerów z listy

                    game.setVisible(false);
                    frame.getMenuPanel().add(frame.boxLoggedUser);
                    frame.remove(game);
                    frame.boxLoggedUser.setVisible(true);
                    frame.add(frame.getMenuPanel());
                    frame.getMenuPanel().setVisible(true);

                    frame.setTitle("Client logged as: " + yourLogin);
                }
        });

        // Wysłanie informacji serwerowi o utworzonym graczu
        sendYourRegister(myPlayer.getId(), myPlayer.getOrientation(), myPlayer.getX(), myPlayer.getY());
        System.out.println("Wysylam");

        connected = true;
        running = true;
        clientThread.start();
    }

    public void disconnect() {

        if (connected) {
            closingSocketsAndStreams(in); // zamknięcie strumienia wejściowego czyli wyskoczenie z pętli
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
            throw new IOException("Loose connection, cannot send information about id of new player to server", e);
        }
    }

    private void sendYourLogin(String yourLogin) throws IOException {

        // Wysłanie serwerowi loginu gracza aby serwer zapamiętał go
        try {
            out.writeUTF(yourLogin);
            out.flush();
        } catch (IOException e) {
            throw new IOException("Loose connection, cannot send information about login of new player to server", e);
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
            throw new IOException("Loose connection, cannot send information about new player to another clients", e);
        }
    }

    public static void sendYourUnRegister(int id) {

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