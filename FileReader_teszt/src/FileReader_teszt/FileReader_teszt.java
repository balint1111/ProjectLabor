/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileReader_teszt;
import java.io.FileReader;
/**
 *
 * @author Attila Katona
 */
public class FileReader_teszt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)throws Exception {
        // TODO code application logic here
                  FileReader fr=new FileReader("test.txt");    
          int i;    
          while((i=fr.read())!=-1)  
         System.out.print((char)i);    
         fr.close();    
    }
    
}
