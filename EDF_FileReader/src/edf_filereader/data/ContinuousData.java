/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edf_filereader.data;

import java.util.List;

/**
 *
 * @author balin
 */
public class ContinuousData {

    public Channel_Simplified[] channels;

     public ContinuousData(){
         
     }
    
    public ContinuousData(
            int storedRecordNumber,
            int channelNumber,
            List<String> labelsOfTheChannels,
            List<String> transducerTypes,
            List<String> physicalDimensionOfChannels,
            List<Integer> physicalMinimums,
            List<Integer> physicalMaximums,
            List<Integer> digitalMinimums,
            List<Integer> digitalMaximums,
            List<String> prefilterings,
            List<Integer> numberOfSamples
    ) {
        channels = new Channel_Simplified[channelNumber];
        for (int i=0;i<channelNumber;i++) {
            channels[i] = new Channel_Simplified(labelsOfTheChannels.get(i),
                    transducerTypes.get(i),
                    physicalDimensionOfChannels.get(i),
                    physicalMinimums.get(i),
                    physicalMaximums.get(i),
                    digitalMinimums.get(i),
                    digitalMaximums.get(i),
                    prefilterings.get(i),
                    numberOfSamples.get(i),
                    storedRecordNumber
            );
        }
    }

}
