package com.example.processmodularizationtool;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Matthias Schoepe - Computer-Masters.de
 */
public class CsvWriter {

    private final String CRLF = "\r\n";
    private String delimiter = ";";

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void exportCsv(String[][] twoDimensionalData, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);

            for (int i = 0; i < twoDimensionalData.length; i++) {
                for (int j = 0; j < twoDimensionalData.length; j++) {
                    writer.append(
                            //Vorsicht hier wird die toString Methode verwendet!
                            twoDimensionalData[i][j]
                            //Falls diese nicht passend implementiert ist, muss eine Alternative verwendet werden.
                    );

                    //Das Trennzeichen einfuegen
                    if (j < twoDimensionalData.length - 1) {
                        writer.append(delimiter);
                    }
                }
                //Das Trennzeichen und das Zeilenende einfuegen
                if (i < twoDimensionalData.length - 1) {
                    writer.append(delimiter + CRLF);
                }
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

