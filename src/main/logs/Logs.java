/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.logs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Logs {

    public static void log(String type, String msg){

        // Type to może być server lub klient

            switch (type) {

                case "server":
                    serverLogs(msg);
                    break;

                case "client":
                    clientLogs(msg);
                    break;

                default:
                    break;
            }
    }

    private static void serverLogs(String msg){

        try (
                FileWriter fileWriter = new FileWriter("server.log", true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter out = new PrintWriter(bufferedWriter)
        ) {
            Date date = new Date();
            String addToFile = "| " + date.toString() + " | " + msg;
            out.println(addToFile);

        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }

    }

    private static void clientLogs(String msg){

        try (

                FileWriter fileWriter = new FileWriter("client.log", true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter out = new PrintWriter(bufferedWriter)
        ) {
            Date date = new Date();
            String addToFile = "| " + date.toString() + " | " + msg;
            out.println(addToFile);

        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
    }
}