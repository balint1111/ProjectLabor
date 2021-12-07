package edf_filereader;

import edf_filereader.EDFreader.EDFAnnotationStruct;
import edf_filereader.data.EEG_Data;
import edf_filereader.exceptions.UsupportedFileFormatException;
import edf_filereader.file.EDFplus_File;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author balin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        try {
//            for(int i=0;i<129;i++){
//                Test.testChannel(i);
//            }

            EDFreader eDFreader = new EDFreader("test_generator_2.edf");
            

            EDFplus_File edfplus = new EDFplus_File("test_generator_2.edf");
            EEG_Data data = edfplus.readRecordFromTo(0, edfplus.getHeader().getNumberOfDataRecords());
            System.out.println((int)data.getAnnotations()[0].charAt(25));
            System.out.println((char)20);
            for (EDFAnnotationStruct annotation : eDFreader.annotationslist) {
                System.out.println(annotation.onset + " " + annotation.description + " " + annotation.duration);
            }
            System.out.println(Arrays.asList(data.getAnnotations()));
            System.out.println(edfplus.getHeader().getPatient());
            
            

            Test.testChannel(0,"ma0844az_1-1+.edf");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EDFreader.EDFException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UsupportedFileFormatException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
