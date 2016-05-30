/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.io;

import java.io.*;
import java.time.LocalDate;

import static main.client.Client.yourLogin;

public class SavingStats {

    public static boolean saving(int destroyed, int deaths){

        try (
                FileWriter fileWriter = new FileWriter("src/main/resources/statistics/all/" +
                        yourLogin + "_all_stats.txt", true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter out = new PrintWriter(bufferedWriter)
        ) {

            // Struktura: Miesiac Destroyed Deaths
            LocalDate today = LocalDate.now();
            String addToFile = today.getMonth().getValue() + " " + destroyed + " " + deaths;
            out.println(addToFile);

        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
            return false;
        }
        return true;
    }
}