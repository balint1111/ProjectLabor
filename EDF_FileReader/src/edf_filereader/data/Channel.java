/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edf_filereader.data;


/**
 *
 * @author balin
 */
public class Channel{ 

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
    int sampleLength;

    public byte[] data;
    
    public Channel(){
        
    }

    public Channel(String label, 
            String transducerType,
            String physicalDimension,
            Integer physicalMinimum,
            Integer physicalMaximum,
            Integer digitalMinimum,
            Integer digitalMaximum,
            String prefiltering, 
            Integer numberOfSamples, 
            Integer storedRecordsOfTheChannel,
            int sampleLength) {
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
        data = new byte[numberOfSamples*storedRecordsOfTheChannel*sampleLength];
        this.sampleLength = sampleLength;
        calculateValues();
    }
    
    public int[] getIntArray(){
        int [] arr = new int[data.length/sampleLength];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = getInt(i);
        }
        return arr;
    }
    
    
    
    public double [] getDoubleArray(){
        double [] arr = new double[data.length/sampleLength];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = bitvalue * (offset + (double) getInt(i));
        }
        return arr;
    }
    
    private Integer getInt(int sampleNumber){
        Integer value = 0;
        for(int i = 0;i < 2 ;i++){
            value |= (data[sampleNumber*sampleLength + i] & 0xFF) << (8*i);
        }
        value |= (data[sampleNumber*sampleLength + sampleLength-1]) << (8*(sampleLength-1));
        return value;
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
