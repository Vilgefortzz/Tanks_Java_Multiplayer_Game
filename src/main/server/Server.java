/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.server;

import main.utilities.Utilities;
import main.models.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.io.LoadImages.*;

public class Server {

    /*
    Informacje o stanie serwera
     */

    private boolean started = false; // informacja(czy włączony czy nie)
    private boolean running = false; // informacja (czy pracuje czy nie)

    /*
    Wątek servera
     */

    private Thread serverThread = null;
    private List<Thread> clientThreads = null; // wątki dla każdego klienta, aby można było je pozamykać

    /*
    Sockety do komunikacji
     */

    private ServerSocket serverSocket = null;
    private Map<Integer, Socket> clientSockets = null;

    /*
    Pomocne
     */

    private int clientID = 1; // identyfikacja na mapie poszczególnych klientów
    private Map<Integer, Player> registeredClients = null;

    /*
    Protokoły komunikacji
     */

    private Map<Integer, DataInputStream> dataInputStreams = null; // wejściowe strumienie danych od klientów
    private Map<Integer, DataOutputStream> dataOutputStreams = null; // wyjściowe strumienie danych do klientów

    /* -------------------------------------------------------------------------------------------------------------- */

    public boolean isStarted() {
        return started;
    }

    public boolean isRunning() {
        return running;
    }

    public synchronized void start(int port) throws IOException{

        if (started){
            // TODO LOGS tutaj
            throw new IOException("Server is started, you can't start it again");
        }

        try {
            serverSocket = new ServerSocket(port); // utworzenie serverSocketa na danym porcie
        } catch (IOException ex){
            // TODO LOGS tutaj
            throw new IOException("Error to create main.server", ex);
        }

        started = true;
        running = true;
        createAndRunServerThread();
    }

    private void createAndRunServerThread(){

        clientSockets = new HashMap<>();
        registeredClients = new HashMap<>();
        dataOutputStreams = new HashMap<>();
        dataInputStreams = new HashMap<>();
        clientThreads = new ArrayList<>(); // stworzenie wątków

        createTankOrientationMap(); // stworzenie mapy z klasy pomocniczej ( mam teraz tankOrientationMap )
        createMissileOrientationMap(); // stworzenie mapy z klasy pomocniczej ( mam teraz missileOrientationMap )

        serverThread = new Thread(() -> {

            while(running){

                try {

                    Socket clientSocket = serverSocket.accept();
                    // TODO LOGS tutaj
                    System.out.println("Polaczono z klientem");

                    DataOutputStream outstream = new DataOutputStream(clientSocket.getOutputStream());
                    DataInputStream instream = new DataInputStream(clientSocket.getInputStream());

                    dataOutputStreams.put(clientID, outstream);
                    dataInputStreams.put(clientID, instream);
                    clientSockets.put(clientID, clientSocket);

                    createAndRunClientThread(clientID, dataInputStreams.get(clientID), dataOutputStreams.get(clientID));
                    clientID++;

                } catch (IOException e){

                    // TODO LOGS tutaj
                    System.out.println("Client didn't connect to main.server");

                    // Nie trzeba zamykać strumieni bo były to zmienne lokalne

                    clientID++;
                }
            }
        });

        serverThread.start(); // startowanie wątku servera(akceptujący)
    }

    private void createAndRunClientThread(int clientID, DataInputStream in, DataOutputStream out){

        Thread clientThread = new Thread(() -> {

            int playerID = 0;
            boolean createdProperly = true;

            try {
                playerID = in.readInt();
                System.out.println("ID: " + playerID);
            } catch (IOException e) {
                // TODO LOGS tutaj
                System.err.println("Client not created properly");
                createdProperly = false;
            }

            if (createdProperly){

                try {
                    eventListening(in, clientID); // pętla nasłuchująca eventy od konkretnego klienta
                } catch (IOException e) {
                    // TODO LOGS tutaj
                    System.out.println("Client left the game");
                }
                finally {

                    // Zamknięcie strumieni dla klienta + socketa
                    Utilities.closingSocketsAndStreams(out);
                    Utilities.closingSocketsAndStreams(in);
                    Utilities.closingSocketsAndStreams(clientSockets.get(clientID));

                    // Usunięcie z mapy strumieni oraz socketów
                    dataOutputStreams.remove(clientID);
                    dataInputStreams.remove(clientID);
                    clientSockets.remove(clientID);

                    registeredClients.remove(playerID);

                    // Powiadomienie pozostałych klientów o odejściu konkretnego klienta
                    try {
                        for (DataOutputStream outputStream : dataOutputStreams.values()){
                            sendUnRegisterInfo(outputStream, playerID);
                        }
                    } catch (IOException e1) {
                        System.err.println("Notification others is failed!!!");
                    }
                }
            }
            else {

                // Zamknięcie strumieni dla klienta + socketa
                Utilities.closingSocketsAndStreams(out);
                Utilities.closingSocketsAndStreams(in);
                Utilities.closingSocketsAndStreams(clientSockets.get(clientID));

                // Usunięcie z mapy strumieni oraz socketów
                dataOutputStreams.remove(clientID);
                dataInputStreams.remove(clientID);
                clientSockets.remove(clientID);
            }
        });

        clientThreads.add(clientThread); // dodanie wątku klienta na listę wątków
        clientThread.start(); // startowanie wątku klienta
    }

    public synchronized void stop() {

        if (started) {

            running = false; // zatrzymanie pętli

            // Zamykanie wszystkich strumieni wyjściowych

            dataOutputStreams.values().forEach(Utilities::closingSocketsAndStreams);

            // Zamykanie wszystkich strumieni wejściowych

            dataInputStreams.values().forEach(Utilities::closingSocketsAndStreams);

            // Zamykanie wszystkich socketów

            clientSockets.values().forEach(Utilities::closingSocketsAndStreams);

            Utilities.closingSocketsAndStreams(serverSocket); // zamknięcie serverSocketa

            // Czekanie, aż wątki klientów wykonają się do końca

            clientThreads.forEach(Utilities::join);

            Utilities.join(serverThread); // czekanie aż wątek servera wykonana się  do końca

            started = false; // wyłączenie servera
        }
    }

    private void eventListening(DataInputStream in, int clientID) throws IOException {

        while (true){

            // rodzaj komunikatu
            int communique = in.readInt();

            switch (communique) {

                case 1:
                    registerHandling(in);
                    break;

                case 2:
                    unRegisterHandling(in, clientID);
                    break;

                case 3:
                    movementHandling(in);
                    break;

                case 4:
                    fireHandling(in);
                    break;

                case 5:
                    collisionTankWithWallHandling(in);
                    break;

                case 6:
                    respawnHandling(in);
                    break;

                case 7:
                    destroyedByHandling(in);
                    break;

                default:
                    break;
            }
        }
    }

    private synchronized void sendRegisterInfo(DataOutputStream out, int id, int orientation, int x, int y) throws IOException {

        out.writeInt(1);
        out.writeInt(id);
        out.writeInt(orientation);
        out.writeInt(x);
        out.writeInt(y);
        out.flush();
    }

    private synchronized void sendUnRegisterInfo(DataOutputStream out, int id) throws IOException {

        out.writeInt(2);
        out.writeInt(id);
        out.flush();
        System.out.println("Wysylam do pozostalych o odejsciu");
    }

    private synchronized void sendMoveInfo(DataOutputStream out, int id, int orientation, int dx, int dy) throws IOException {

        out.writeInt(3);
        out.writeInt(id);
        out.writeInt(orientation);
        out.writeInt(dx);
        out.writeInt(dy);
        out.flush();
    }

    private synchronized void sendFireInfo(DataOutputStream out, int id, int orientation) throws IOException {

        out.writeInt(4);
        out.writeInt(id);
        out.writeInt(orientation);
        out.flush();
    }

    private synchronized void sendCollisionTankWithWallInfo(DataOutputStream out, int id) throws IOException {

        out.writeInt(5);
        out.writeInt(id);
        out.flush();
    }

    private synchronized void sendRespawnInfo(DataOutputStream out, int id, int x, int y) throws IOException {

        out.writeInt(6);
        out.writeInt(id);
        out.writeInt(x);
        out.writeInt(y);
        out.flush();
    }

    private synchronized void sendDestroyedByInfo(DataOutputStream out, int id) throws IOException {

        out.writeInt(7);
        out.writeInt(id);
        out.flush();
    }

    private void registerHandling(DataInputStream in) throws IOException {

        // Zczytanie informacji o kliencie
        int id = in.readInt();
        int orientation = in.readInt();
        int x = in.readInt();
        int y = in.readInt();

        registeredClients.put(id, new Player(id, orientation, x, y));

        // Wysłanie tej informacji wszystkim klientom
        for (Player player : registeredClients.values()){
            for (DataOutputStream out : dataOutputStreams.values()){
                sendRegisterInfo(out, player.getId(), player.getOrientation(), player.getX(), player.getY());
            }
        }
    }

    private void unRegisterHandling(DataInputStream in, int clientID) throws IOException {

        // Zczytanie id klienta, który opuścił grę
        int id = in.readInt();

        dataOutputStreams.remove(clientID);
        registeredClients.remove(id);

        // Wysłanie informacji o opuszczeniu gry przez konkretnego klienta wszystkim klientom
        for (DataOutputStream out : dataOutputStreams.values()) {
            sendUnRegisterInfo(out, id);
        }
    }

    private void movementHandling(DataInputStream in) throws IOException {

        // Zczytanie informacji o kliencie, który się poruszył
        int id = in.readInt();
        int orientation = in.readInt();
        int dx = in.readInt();
        int dy = in.readInt();

        // Wysłanie informacji o ruchu konkretnego klienta wszystkim klientom
        for (DataOutputStream out : dataOutputStreams.values()) {
            sendMoveInfo(out, id, orientation, dx, dy);
        }

        registeredClients.get(id).setOrientation(orientation);
        registeredClients.get(id).setDx(dx);
        registeredClients.get(id).setDy(dy);
        registeredClients.get(id).setMainImage(tankOrientationMap.get(orientation));
        registeredClients.get(id).getImageDimensions();
        registeredClients.get(id).updateMovement();
    }

    private void fireHandling(DataInputStream in) throws IOException {

        // Zczytanie informacji o wystrzeleniu pocisku przez konkretnego klienta
        int id = in.readInt();
        int orientation = in.readInt();

        // Wysłanie informacji o wystrzeleniu pocisku przez konkretnego klienta wszystkim klientom
        for (DataOutputStream out : dataOutputStreams.values()) {
            sendFireInfo(out, id, orientation);
        }
    }

    private void respawnHandling(DataInputStream in) throws IOException {

        // Zczytanie tylko id, następnie wygenerowanie nowych współrzędnych
        int id = in.readInt();
        int x = in.readInt();
        int y = in.readInt();

        // Wysłanie informacji o odnowieniu się konkretnego klienta wszystkim klientom
        for (DataOutputStream out : dataOutputStreams.values()){
            sendRespawnInfo(out, id, x, y);
        }

        registeredClients.get(id).setHp(100);
        registeredClients.get(id).setX(x);
        registeredClients.get(id).setY(y);
        registeredClients.get(id).setOrientation(3);
        registeredClients.get(id).setMainImage(tankOrientationMap.get(3));
        registeredClients.get(id).getImageDimensions();
    }

    private void destroyedByHandling(DataInputStream in) throws IOException {

        // Zczytanie id klienta, którego pocisk zniszczył wrogi czołg
        int id = in.readInt();

        // Wysłanie informacji o tym graczu, który zniszczył wrogi czołg wszystkim klientom
        for (DataOutputStream out : dataOutputStreams.values()){
            sendDestroyedByInfo(out, id);
        }
    }


    /*
    Collisions
     */

    private void collisionTankWithWallHandling(DataInputStream in) throws IOException {

        int id = in.readInt();

        // Wysłanie informacji o ruchu konkretnego klienta wszystkim klientom
        for (DataOutputStream out : dataOutputStreams.values()) {
            sendCollisionTankWithWallInfo(out, id);
        }

        registeredClients.get(id).restorePreviousPosition();
    }
}