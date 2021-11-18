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
public class Channel {

    String label;
    String transducerType;
    String physicalDimension;
    Integer physicalMinimum;
    Integer physicalMaximum;
    Integer digitalMinimum;
    Integer digitalMaximum;
    String prefiltering;
    double bitvalue;
    double offset;

    List<Sample> samples;

    public Channel(List<Sample> samples) {
        this.samples = samples;
    }

    public Channel() {
        this.samples = new ArrayList<>();
    }

    public void add(Sample sample) {
        samples.add(sample);
    }

    public void addAll(List<Sample> samples) {
        samples.addAll(samples);
    }

    public Sample getSample(int i) {
        return samples.get(i);
    }

    public List<Sample> getSamplesFromTo(int from, int to) {
        return samples.subList(from, to);
    }

    public int size() {
        return samples.size();
    }

    public int[] getIntArray() {
        return samples.stream().map(Sample::getIntegerValue).toList().stream().mapToInt(i -> i).toArray();
    }
    
    public double [] getDoubleArray() {
        return samples.stream().map((t) -> {
            return t.getDoubleValue(bitvalue, offset);
        }).toList().stream().mapToDouble(d -> d).toArray();
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
