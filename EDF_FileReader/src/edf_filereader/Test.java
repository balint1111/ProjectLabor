package edf_filereader;

import edf_filereader.exceptions.UsupportedFileFormatException;
import edf_filereader.data.ContinuousData;
import edf_filereader.data.Channel;
import edf_filereader.file.EEG_File;
import java.io.IOException;

/**
 *
 * @author balin
 */
public class Test {

    public static void testChannel(int cahannelNumber, String fileName) throws IOException, EDFreader.EDFException, InterruptedException, UsupportedFileFormatException {
        EEG_File myReader = EEG_File.build(fileName);
        System.out.println(myReader.getClass());
        System.out.println(myReader.getHeader().getKeys());
        EDFreader edfReader = new EDFreader(fileName);

        double[] doubleArray = myReader.getChannel(cahannelNumber).getDoubleArray();
        double[] doubleArray2 = new double[myReader.getHeader().getNumberOfSample(cahannelNumber) * myReader.getHeader().getNumberOfDataRecords()];
        edfReader.readPhysicalSamples(cahannelNumber, doubleArray2);
        String ok = "OK";
        for (int i = 0; i < myReader.getHeader().getNumberOfSample(cahannelNumber) * myReader.getHeader().getNumberOfDataRecords(); i++) {
            if (doubleArray[i] != doubleArray2[i]) {
                ok = "WRONG";
            }
        }
        System.out.println("Channel " + cahannelNumber + ": " + ok);
    }


    public void testRecord(String fileName) throws IOException, EDFreader.EDFException, InterruptedException, UsupportedFileFormatException {
        EEG_File myReader = EEG_File.build(fileName);
        ContinuousData data = myReader.readRecordFromTo(0, myReader.getHeader().getNumberOfDataRecords());

        for (int cahannelNumber = 0; cahannelNumber < myReader.getHeader().getNumberOfChannels(); cahannelNumber++) {
            double[] doubleArray = data.channels[cahannelNumber].getDoubleArray();
            double[] doubleArray2 = myReader.getChannel(cahannelNumber).getDoubleArray();

            String ok = "OK";
            for (int i = 0; i < myReader.getHeader().getNumberOfSample(cahannelNumber) * myReader.getHeader().getNumberOfDataRecords(); i++) {
                if (doubleArray[i] != doubleArray2[i]) {
                    ok = "WRONG";
                }
            }
            System.out.println("Channel " + cahannelNumber + ": " + ok);
        }
    }

    public static void readFullFile(String fileName) throws IOException, EDFreader.EDFException, InterruptedException, UsupportedFileFormatException {
        //clearMemoryMappoings();
       
        System.out.println("myReader");
        long currentTimeMillis = currentTimeMillis = System.currentTimeMillis();
        EEG_File myReader = EEG_File.build(fileName);
        System.out.println(myReader.getClass());
        
        Channel[] channels = myReader.readRecordFromTo(0, myReader.getHeader().getNumberOfDataRecords()).channels;
        
        for (Channel channel : channels) {
            channel.getDoubleArray();
        }

        currentTimeMillis = System.currentTimeMillis() - currentTimeMillis;
        System.out.println(currentTimeMillis);
        
        //clearMemoryMappoings();
        

        System.out.println("edfReader");
        currentTimeMillis = currentTimeMillis = System.currentTimeMillis();
        
        EDFreader eDFreader = new EDFreader(fileName);
        for(int i=0;i<eDFreader.getNumSignals();i++){
            double[] buf;
            buf = new double[(int)eDFreader.getSampelsPerDataRecord(i)*(int)eDFreader.getNumDataRecords()];
            eDFreader.readPhysicalSamples(i, buf);
        }
        currentTimeMillis = System.currentTimeMillis() - currentTimeMillis;
        System.out.println(currentTimeMillis);
    }
    
    public static void clearMemoryMappoings() throws IOException, InterruptedException{
        String[] strArr = new String[1];
        strArr[0] = "standbylist";
        Runtime.getRuntime().exec("EmptyStandbyList.exe", strArr);
        System.gc();
        Thread.sleep(10000);
        System.out.println("Memory cleared!");
        
    }

}
