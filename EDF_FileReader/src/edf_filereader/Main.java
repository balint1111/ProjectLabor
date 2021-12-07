package edf_filereader;

import edf_filereader.exceptions.UsupportedFileFormatException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author balin
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        try {
            Test.readFullFile("dsa.bdf");
            
//            for(int i=0;i<129;i++){
//                Test.testChannel(i,"ma0844az_1-1+.edf");
//            }
            
//            Test.testChannel(128,"dsa.bdf");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EDFreader.EDFException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UsupportedFileFormatException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
