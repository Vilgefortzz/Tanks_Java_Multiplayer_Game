/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MapReader {

    private String [][] fieldsArray;

    public MapReader(String mapFile) {

        try {
            readFile(mapFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[][] getFieldsArray() {
        return fieldsArray;
    }

    private void readFile(String file) throws IOException{

        fieldsArray = new String[15][27];
        String[] withoutSpaces;

        String filePath = new File("").getAbsolutePath();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath + "/src/main/resources/maps/" + file))){

            /* odczytywanie lini tekstu */

            String textLine;
            int i=0;

           do {
                textLine = bufferedReader.readLine();
                withoutSpaces = textLine.split(" ");

                    for (int j = 0; j < 27; j++) {
                        fieldsArray[i][j] = withoutSpaces[j];
                    }
                i++;
                } while(bufferedReader.ready());
        }
    }
}
