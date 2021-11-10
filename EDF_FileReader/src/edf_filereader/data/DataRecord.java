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
public class DataRecord {
    List<Channel> channels;

    public DataRecord(List<Channel> channels) {
        this.channels = channels;
    }
    
}
