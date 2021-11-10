/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edf_filereader.data;

/**
 *
 * @author balin
 */
public class Sample {

    private byte[] data;

    public Sample() {
        this.data = new byte[3];
    }
    public Sample(byte[] data){
        this.data = data;
    }
    
    public Sample(byte data0,byte data1,byte data2){
        this.data = new byte[3];
        data[0] = data0;
        data[1] = data1;
        data[2] = data2;
    }

    public Integer getIntegerValue() {
        Integer value = 0;
        value |= data[0] & 0xFF;
        value |= (data[1] & 0xFF) << 8;
        value |= (data[2] & 0xFF) << 16;
        return value;
    }
    
    @Override
    public String toString(){
        return getIntegerValue().toString();
    }

}
