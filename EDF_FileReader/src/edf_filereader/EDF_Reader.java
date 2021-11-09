/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edf_filereader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author balin
 */
public class EDF_Reader {

    String filename;
    
    private EDF_Reader(String filename){
        this.filename = filename;
    }
    
    public void readHeader() {

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filename));
            for (int i = 168; i < 1000; i++) {
                System.out.print((char) bytes[i]);
            }
        } catch (IOException ex) {
            Logger.getLogger(EDF_Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
