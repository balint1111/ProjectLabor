package edf_filereader;

import edf_filereader.exceptions.UsupportedFileFormatException;
import edf_filereader.file.EDFplus_File;

import java.io.IOException;
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

            EDFplus_File edfplus = new EDFplus_File("test_generator_2.edf");
            System.out.println(edfplus.getAnnotations()[0]);
            System.out.println(edfplus.getAnnotations());

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
