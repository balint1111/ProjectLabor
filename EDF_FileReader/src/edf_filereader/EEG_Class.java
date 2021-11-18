/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edf_filereader;

import edf_filereader.header.EEG_Header;
import edf_filereader.data.Channel_Simplified;
import edf_filereader.data.ContinuousData;
import java.io.IOException;

abstract class EEG_Class {
    
    public EEG_Header header;
    
    public abstract void readHeader();
    
    public abstract Channel_Simplified getChannel(int channelNumber) throws IOException, InterruptedException;
    
    public abstract ContinuousData readRecordFromTo(int from, int to) throws IOException;

    public EEG_Header getHeader() {
        return header;
    }
    

    public static EEG_Class build(String fileName){
        return new BDF_File_Simplified(fileName);
    }
    
}
