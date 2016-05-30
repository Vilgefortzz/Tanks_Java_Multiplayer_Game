/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Configuration {

    public static void serverConfg(int port, String started){

        try (
                FileWriter fileWriter = new FileWriter("configurations/serverConfg.txt", false);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter out = new PrintWriter(bufferedWriter)
        ) {
            String addToFile = "SERVER CONFIGURATION:\n" +
                    "PORT: " + port + "\n" +
                    "SERVER STATUS: " + started;
            out.print(addToFile);

        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
    }

    public static void clientConfg(String host, int port, String connected, String user){

        try (
                FileWriter fileWriter = new FileWriter("configurations/" + user + "Confg.txt", false);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter out = new PrintWriter(bufferedWriter)
        ) {
            String addToFile = "CLIENT CONFIGURATION:\n" +
                    "HOST: " + host + "\n" +
                    "PORT: " + port + "\n" +
                    "CONNECTED: " + connected + "\n" +
                    "USER: " + user;
            out.print(addToFile);

        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
    }
}