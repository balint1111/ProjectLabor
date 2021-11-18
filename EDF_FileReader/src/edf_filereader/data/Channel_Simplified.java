/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edf_filereader.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author balin
 */
public class Channel_Simplified {

    String label;
    String transducerType;
    String physicalDimension;
    Integer physicalMinimum;
    Integer physicalMaximum;
    Integer digitalMinimum;
    Integer digitalMaximum;
    String prefiltering;
    Integer numberOfSamples;
    Integer storedRecordsOfTheChannel;
    double bitvalue;
    double offset;

    public byte[] samples;
    
    public Channel_Simplified(){
        
    }

    public Channel_Simplified(String label, 
            String transducerType,
            String physicalDimension,
            Integer physicalMinimum,
            Integer physicalMaximum,
            Integer digitalMinimum,
            Integer digitalMaximum,
            String prefiltering, 
            Integer numberOfSamples, 
            Integer storedRecordsOfTheChannel) {
        this.label = label;
        this.transducerType = transducerType;
        this.physicalDimension = physicalDimension;
        this.physicalMinimum = physicalMinimum;
        this.physicalMaximum = physicalMaximum;
        this.digitalMinimum = digitalMinimum;
        this.digitalMaximum = digitalMaximum;
        this.prefiltering = prefiltering;
        this.numberOfSamples = numberOfSamples;
        this.storedRecordsOfTheChannel = storedRecordsOfTheChannel;
        samples = new byte[numberOfSamples*storedRecordsOfTheChannel*3];
        calculateValues();
    }
    
    public int[] getIntArray(){
        int [] arr = new int[samples.length/3];
        for (int i = 0; i < samples.length; i+= 3) {
            Integer value = 0;
            value |= samples[i] & 0xFF;
            value |= (samples[i + 1] & 0xFF) << 8;
            value |= (samples[i + 2] ) << 16;
            arr[i/3] = value;
        }
        return arr;
    }
    
    public double [] getDoubleArray(){
        double [] arr = new double[samples.length/3];
        for (int i = 0; i < samples.length; i+= 3) {
            Integer value = 0;
            value |= samples[i] & 0xFF;
            value |= (samples[i + 1] & 0xFF) << 8;
            value |= (samples[i + 2] ) << 16;
            arr[i/3] = bitvalue * (offset + (double) value);
        }
        return arr;
    }

    public void calculateValues() {
        bitvalue = ((double) (physicalMaximum - physicalMinimum)) / ((double)(digitalMaximum - digitalMinimum));
        offset = ((double)physicalMaximum) / bitvalue - ((double)digitalMaximum);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTransducerType() {
        return transducerType;
    }

    public void setTransducerType(String transducerType) {
        this.transducerType = transducerType;
    }

    public String getPhysicalDimension() {
        return physicalDimension;
    }

    public void setPhysicalDimension(String physicalDimension) {
        this.physicalDimension = physicalDimension;
    }

    public Integer getPhysicalMinimum() {
        return physicalMinimum;
    }

    public void setPhysicalMinimum(Integer physicalMinimum) {
        this.physicalMinimum = physicalMinimum;
    }

    public Integer getPhysicalMaximum() {
        return physicalMaximum;
    }

    public void setPhysicalMaximum(Integer physicalMaximum) {
        this.physicalMaximum = physicalMaximum;
    }

    public Integer getDigitalMinimum() {
        return digitalMinimum;
    }

    public void setDigitalMinimum(Integer digitalMinimum) {
        this.digitalMinimum = digitalMinimum;
    }

    public Integer getDigitalMaximum() {
        return digitalMaximum;
    }

    public void setDigitalMaximum(Integer digitalMaximum) {
        this.digitalMaximum = digitalMaximum;
    }

    public String getPrefiltering() {
        return prefiltering;
    }

    public void setPrefiltering(String prefiltering) {
        this.prefiltering = prefiltering;
    }

}
