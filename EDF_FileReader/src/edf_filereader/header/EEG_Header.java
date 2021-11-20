/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edf_filereader.header;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author balin
 */
public abstract class EEG_Header {
    
    protected FileChannel fileChannel;

    protected String filename;
    protected Integer numberOfDataRecords;
    protected Integer numberOfChannels;
    protected List<String> labelsOfTheChannels;
    protected List<String> transducerTypes;
    protected List<String> physicalDimensionOfChannels;
    protected List<Integer> physicalMinimums;
    protected List<Integer> physicalMaximums;
    protected List<Integer> digitalMinimums;
    protected List<Integer> digitalMaximums;
    protected List<String> prefilterings;
    protected List<Integer> numberOfSamples;
    
    protected Map<String,Object> extraParameters;
    
    public List<String> getKeys(){
        return extraParameters.entrySet().stream().map((entry) -> {return entry.getKey();}).collect(Collectors.toList());
    }
    
    public FileChannel getFileChannel() {
        return fileChannel;
    }

    public void setFileChannel(FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getNumberOfDataRecords() {
        return numberOfDataRecords;
    }

    public void setNumberOfDataRecords(Integer numberOfDataRecords) {
        this.numberOfDataRecords = numberOfDataRecords;
    }

    public Integer getNumberOfChannels() {
        return numberOfChannels;
    }

    public void setNumberOfChannels(Integer numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
    }

    public String getLabelsOfTheChannels(int channelNumber) {
        return labelsOfTheChannels.get(channelNumber);
    }

    public void setLabelsOfTheChannels(List<String> labelsOfTheChannels) {
        this.labelsOfTheChannels = labelsOfTheChannels;
    }

    public String getTransducerType(int channelNumber) {
        return transducerTypes.get(channelNumber);
    }

    public void setTransducerTypes(List<String> transducerTypes) {
        this.transducerTypes = transducerTypes;
    }

    public String getPhysicalDimensionOfChannel(int channelNumber) {
        return physicalDimensionOfChannels.get(channelNumber);
    }

    public void setPhysicalDimensionOfChannels(List<String> physicalDimensionOfChannels) {
        this.physicalDimensionOfChannels = physicalDimensionOfChannels;
    }

    public Integer getPhysicalMinimum(int channelNumber) {
        return physicalMinimums.get(channelNumber);
    }

    public void setPhysicalMinimums(List<Integer> physicalMinimums) {
        this.physicalMinimums = physicalMinimums;
    }

    public Integer getPhysicalMaximum(int channelNumber) {
        return physicalMaximums.get(channelNumber);
    }

    public void setPhysicalMaximums(List<Integer> physicalMaximums) {
        this.physicalMaximums = physicalMaximums;
    }

    public Integer getDigitalMinimum(int channelNumber) {
        return digitalMinimums.get(channelNumber);
    }

    public void setDigitalMinimums(List<Integer> digitalMinimums) {
        this.digitalMinimums = digitalMinimums;
    }

    public Integer getDigitalMaximum(int channelNumber) {
        return digitalMaximums.get(channelNumber);
    }

    public void setDigitalMaximums(List<Integer> digitalMaximums) {
        this.digitalMaximums = digitalMaximums;
    }

    public String getPrefiltering(int channelNumber) {
        return prefilterings.get(channelNumber);
    }

    public void setPrefilterings(List<String> prefilterings) {
        this.prefilterings = prefilterings;
    }

    public Integer getNumberOfSample(int channelNumber) {
        return numberOfSamples.get(channelNumber);
    }

    public void setNumberOfSamples(List<Integer> numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }

    public List<String> getLabelsOfTheChannels() {
        return labelsOfTheChannels;
    }

    public List<String> getTransducerTypes() {
        return transducerTypes;
    }

    public List<String> getPhysicalDimensionOfChannels() {
        return physicalDimensionOfChannels;
    }

    public List<Integer> getPhysicalMinimums() {
        return physicalMinimums;
    }

    public List<Integer> getPhysicalMaximums() {
        return physicalMaximums;
    }

    public List<Integer> getDigitalMinimums() {
        return digitalMinimums;
    }

    public List<Integer> getDigitalMaximums() {
        return digitalMaximums;
    }

    public List<String> getPrefilterings() {
        return prefilterings;
    }

    public List<Integer> getNumberOfSamples() {
        return numberOfSamples;
    }

    public Map<String, Object> getScpecificValues() {
        return extraParameters;
    }
    
    
}
