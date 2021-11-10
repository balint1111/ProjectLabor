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
    List<Sample> samples;

    public Channel(List<Sample> samples) {
        this.samples = samples;
    }
    public Channel() {
        this.samples = new ArrayList<>();
    }
    public void add(Sample sample){
        samples.add(sample);
    }
    public void addAll(List<Sample> samples){
        samples.addAll(samples);
    }
    public Sample getSample(int i){
        return samples.get(i);
    }
    public List<Sample> getSamplesFromTo(int from,int to){
        return samples.subList(from, to);
    }
    public int size(){
        return samples.size();
    }
    
}
