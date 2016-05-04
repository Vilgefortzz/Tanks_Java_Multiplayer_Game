/*
 * Copyright (c) 2016.
 * @gklimek
 */

package server;

import connection.ConnectionHandling;
import models.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.LoadImages.*;

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
    private ArrayList<Thread> clientThreads = null; // wątki dla każdego klienta, aby można było je pozamykać

    /*
    Sockety do komunikacji
     */

    private ServerSocket serverSocket = null;
    private Map<Integer, Socket> clientSockets = null;
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

    public void start(int port) throws IOException{

        if (started){
            throw new IOException("Server is started, you can't start it again");
        }

        try {
            serverSocket = new ServerSocket(port); // utworzenie serverSocketa na danym porcie
        } catch (IOException ex){
            throw new IOException("Error to create server", ex);
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

        serverThread = new Thread(() -> {

            while(running){

                try {

                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Polaczono z klientem");

                    DataOutputStream outstream = new DataOutputStream(clientSocket.getOutputStream());
                    DataInputStream instream = new DataInputStream(clientSocket.getInputStream());

                    dataOutputStreams.put(clientID, outstream);
                    dataInputStreams.put(clientID, instream);
                    clientSockets.put(clientID, clientSocket);

                    createAndRunClientThread(clientID, dataInputStreams.get(clientID), dataOutputStreams.get(clientID));
                    clientID++;

                } catch (IOException e){

                    System.out.println("Client didn't connect to server");

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
            try {
                playerID = in.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("playerID: " + playerID);

            try {
                eventListening(in, clientID); // pętla nasłuchująca eventy od konkretnego klienta
            } catch (IOException e) {
                System.out.println("Client left the game");
            }
            finally {

                System.out.println("Witam ciebie");
                // Zamknięcie strumieni dla klienta + socketa
                ConnectionHandling.close(out);
                ConnectionHandling.close(in);
                ConnectionHandling.close(clientSockets.get(clientID));

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
                    e1.printStackTrace();
                }
            }
        });

        clientThreads.add(clientThread); // dodanie wątku klienta na listę wątków
        clientThread.start(); // startowanie wątku klienta
    }

    public void stop()
    {
        if (started)
        {
            running = false; // zatrzymanie pętli

            // Zamykanie wszystkich strumieni wyjściowych

            for (DataOutputStream out : dataOutputStreams.values()){
                ConnectionHandling.close(out);
            }

            // Zamykanie wszystkich strumieni wejściowych

            for (DataInputStream in : dataInputStreams.values()){
                ConnectionHandling.close(in);
            }

            // Zamykanie wszystkich socketów

            for (Socket socket : clientSockets.values()){
                ConnectionHandling.close(socket);
            }

            ConnectionHandling.close(serverSocket); // zamknięcie serverSocketa

            // Czekanie, aż wątki klientów wykonają się do końca

            for (Thread thread : clientThreads){

                ConnectionHandling.join(thread);
            }

            ConnectionHandling.join(serverThread); // czekanie aż wątek servera wykonana się  do końca

            started = false; // wyłączenie servera
        }
    }

    private void eventListening(DataInputStream in, int clientID) throws IOException {

        while (true){

            // rodzaj komunikatu
            int communique = in.readInt();

            switch (communique)
            {
                case 1:
                    registerHandling(in);
                    break;

                case 2:
                    unRegisterHandling(in, clientID);
                    break;

                case 3:
                    movementHandling(in);
                    break;

                default:
                    break;
            }
        }
    }

    private void sendRegisterInfo(DataOutputStream out, int id, int orientation, int x, int y) throws IOException {

        out.writeInt(1);
        out.writeInt(id);
        out.writeInt(orientation);
        out.writeInt(x);
        out.writeInt(y);
    }

    private void sendUnRegisterInfo(DataOutputStream out, int id) throws IOException {

        out.writeInt(2);
        out.writeInt(id);
        System.out.println("Wysylam do pozostalych o odejsciu");
    }

    private void sendMoveInfo(DataOutputStream out, int id, int orientation, int dx, int dy) throws IOException {

        out.writeInt(3);
        out.writeInt(id);
        out.writeInt(orientation);
        out.writeInt(dx);
        out.writeInt(dy);
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

        if (orientation == 1){
            registeredClients.get(id).setMainImage(tankLeft);
        }
        else if (orientation == 2){
            registeredClients.get(id).setMainImage(tankUp);
        }
        else if (orientation == 3){
            registeredClients.get(id).setMainImage(tankRight);
        }
        else{
            registeredClients.get(id).setMainImage(tankDown);
        }

        registeredClients.get(id).getImageDimensions();
        registeredClients.get(id).setDx(dx);
        registeredClients.get(id).setDy(dy);
        registeredClients.get(id).setOrientation(orientation);
        registeredClients.get(id).updateMovement();

        System.out.println("registered X: " + registeredClients.get(id).getX());
        System.out.println("registered Y: " + registeredClients.get(id).getY());
    }
}