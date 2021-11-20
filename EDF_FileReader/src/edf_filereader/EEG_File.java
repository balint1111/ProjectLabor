/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edf_filereader;

import edf_filereader.data.ContinuousData;
import edf_filereader.data.Channel;
import edf_filereader.header.EEG_Header;
import java.io.IOException;

abstract class EEG_File {
    
    public EEG_Header header;
    
    public abstract void readHeader();
    
    public abstract Channel getChannel(int channelNumber) throws IOException, InterruptedException;
    
    public abstract ContinuousData readRecordFromTo(int from, int to) throws IOException;

    public EEG_Header getHeader() {
        return header;
    }
    

    public static EEG_File build(String fileName){
        return new BDF_File(fileName);
    }
    
}
