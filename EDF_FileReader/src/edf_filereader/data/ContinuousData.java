package edf_filereader.data;

import java.util.List;

/**
 *
 * @author balin
 */
public class ContinuousData {

    public Channel[] channels;

    public ContinuousData(){

    }
    
    public ContinuousData(
            int storedRecordNumber,
            int channelNumber,
            List<String> labelsOfTheChannels,
            List<String> transducerTypes,
            List<String> physicalDimensionOfChannels,
            List<Double> physicalMinimums,
            List<Double> physicalMaximums,
            List<Double> digitalMinimums,
            List<Double> digitalMaximums,
            List<String> prefilterings,
            List<Integer> numberOfSamples,
            int sampleLength
    ) {
        channels = new Channel[channelNumber];
        for (int i=0;i<channelNumber;i++) {
            channels[i] = new Channel(labelsOfTheChannels.get(i),
                    transducerTypes.get(i),
                    physicalDimensionOfChannels.get(i),
                    physicalMinimums.get(i),
                    physicalMaximums.get(i),
                    digitalMinimums.get(i),
                    digitalMaximums.get(i),
                    prefilterings.get(i),
                    numberOfSamples.get(i),
                    storedRecordNumber,
                    sampleLength
            );
        }
    }
    
    public int[][] getIntArray(){
        int[][] arr = new int[channels.length][];
        for (int i=0;i<channels.length;i++) {
            arr[i] = channels[i].getIntArray();
        }
        return arr;
    }
    
    public double[][] getDoubleArray(){
        double[][] arr = new double[channels.length][];
        for (int i=0;i<channels.length;i++) {
            arr[i] = channels[i].getDoubleArray();
        }
        return arr;
    }


}
