/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package edf_filereader;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author balin
 */
public class EDF_FileReader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EDF_Reader myReader = new EDF_Reader("experiment.bdf");
        myReader.printHeader();
        System.out.println("Data");
        System.out.println("myData: " + myReader.getChannel(1).getSamplesFromTo(2048, 2148));
        
        
        
        
        
        try {
            EDFreader edfReader = new EDFreader("experiment.bdf");
            int[] buf = new int[2148];
            edfReader.readDigitalSamples(1, buf);
            System.out.print("EDFreader: ");
            for(int i=2048; i<2148; i++)
            {
                System.out.print(buf[i] + ", ");
            }

        } catch (IOException ex) {
            Logger.getLogger(EDF_FileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EDFreader.EDFException ex) {
            Logger.getLogger(EDF_FileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
