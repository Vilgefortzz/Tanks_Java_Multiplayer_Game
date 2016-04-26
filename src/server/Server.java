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
                    System.out.println("Polaczono z socketem");
                    in = new DataInputStream(clientSocket.getInputStream());
                    out = new DataOutputStream(clientSocket.getOutputStream());

                    eventListening(); // pętla nasłuchująca eventy od klientów

                } catch (IOException e){
                    System.out.println("Nastapilo zerwanie polaczenia klienta z serverem");

                    // Powiadomienie wszystkich pozostałych klientów o odejściu konkretnego klienta
                    // TODO Tutaj jest duży problem jak powiadomić pozostałych klientów o odejściu klienta


                } finally {

                    System.out.println("Jestem");
                    ConnectionHandling.close(out); // zamykanie strumienia wyjściowego
                    ConnectionHandling.close(in); // zamykanie strumienia wejściowego
                    ConnectionHandling.close(clientSocket); // zamknięcie clientSocketa
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

            ConnectionHandling.close(out); // zamykanie strumienia wyjściowego
            ConnectionHandling.close(in); // zamykanie strumienia wejściowego
            ConnectionHandling.close(clientSocket); // zamknięcie clientSocketa

            ConnectionHandling.close(serverSocket); // zamknięcie serverSocketa

            ConnectionHandling.join(serverThread); // czekanie aż wątek się wykonana do końca

            started = false; // wyłączenie servera
        }
    }

    private void eventListening() throws IOException {

        while (true){

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
    }

    private void registerHandling() throws IOException {

        // Zczytanie informacji o kliencie
        int id = in.readInt();
        String login = in.readUTF();
        int x = in.readInt();
        int y = in.readInt();

        // Wysłanie tej informacji pozostałym klientom
        /*
        out.writeByte(1);
        out.writeInt(id);
        out.writeUTF(login);
        out.writeInt(x);
        out.writeInt(y);
         */
    }

    private void unRegisterHandling() throws IOException {

        // Zczytanie id klienta, który opuścił grę
        int id = in.readInt();

        // Wysłanie informacji o opuszczeniu gry przez konkretnego klienta pozostałym klientom
        /*
        out.writeByte(2);
        out.writeInt(id);
         */
    }

    private void movementHandling() throws IOException {

        // Zczytanie informacji o kliencie, który się poruszył
        int id = in.readInt();
        int dx = in.readInt();
        int dy = in.readInt();

        // Wysłanie informacji o ruchu konkretnego klienta pozostałym klientom
        /*
        out.writeByte(3);
        out.writeInt(id);
        out.writeInt(dx);
        out.writeInt(dy);
         */
    }
}