/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static main.client.Client.database;
import static main.client.Client.yourID;
import static main.client.Client.yourLogin;

public class SaveStats {

    public static boolean saveStatistics(){

        // Zczytanie id danego gracza z bazy danych
        yourID = database.takeID(yourLogin);

        // Sprawdzanie statystyk danego gracza
        int[] variables;
        variables = database.takeStats(yourID);

        try (
                FileWriter fileWriter = new FileWriter("statistics/" + yourLogin + "_stats.txt", false);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter out = new PrintWriter(bufferedWriter)
        ) {
            String addToFile = "STATS FROM USER '" + yourLogin.toUpperCase() + "'" + "\n" +
                    "Tanks destroyed: " + variables[0] +  "\n" +
                    "The number of deaths: " + variables[1] + "\n" +
                    "Difference between tanks destroyed and the number of deaths: " + variables[2];
            out.println(addToFile);

        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
            return false;
        }
        return true;
    }
}