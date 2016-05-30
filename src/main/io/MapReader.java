/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.io;

import java.io.*;
import java.util.ArrayList;

public class MapReader {

    private ArrayList<String> lines;

    public MapReader(String mapFile) {

        try {
            readFile(mapFile);
        } catch (IOException | NullPointerException e) {
            System.err.println("Loading the map is failed!!");
            System.exit(0);
        }
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    private void readFile(String fileName) throws IOException, NullPointerException {

        lines = new ArrayList<>();

        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        getClass().getResourceAsStream("/maps/" + fileName)))
        ) {
            while (bufferedReader.ready()){
                lines.add(bufferedReader.readLine());
            }
            bufferedReader.close();
        }
    }
}