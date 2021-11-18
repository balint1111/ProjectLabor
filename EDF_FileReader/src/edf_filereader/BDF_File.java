/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edf_filereader;

import edf_filereader.data.Channel;
import edf_filereader.data.Sample;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author balin
 */
public class BDF_File {

    
    FileChannel fileChannel;

    String filename;
    Integer headerType;
    String patient;
    String recording;
    String startDate;
    String startTime;
    Integer numberOfBytes;
    String version;
    Integer headerSize;
    Integer numberOfDataRecords;
    Integer durationOfDataRecord;
    Integer numberOfChannels;
    List<String> labelsOfTheChannels;
    List<String> transducerTypes;
    List<String> physicalDimensionOfChannels;
    List<Integer> physicalMinimums;
    List<Integer> physicalMaximums;
    List<Integer> digitalMinimums;
    List<Integer> digitalMaximums;
    List<String> prefilterings;
    List<Integer> numberOfSamples;

    List<Channel> channels;
    Integer startData;
    Integer dataRecordSize;

    public BDF_File(String filename) {
        this.filename = filename;

        try {
            fileChannel = new FileInputStream(filename).getChannel();
        } catch (IOException ex) {
            Logger.getLogger(BDF_File.class.getName()).log(Level.SEVERE, null, ex);
        }
        readHeader();
    }

    private List<Integer> readASCII_Integers(int start, int dataLength, byte[] bytes) throws UnsupportedEncodingException {
        List<Integer> returnList = new ArrayList<>();
        for (int i = start; i < start + (dataLength * numberOfChannels); i += dataLength) {
            returnList.add(Integer.parseInt(new String(bytes, i, dataLength, "US-ASCII").trim()));
        }
        return returnList;
    }

    private List<String> readStrings(int start, int dataLength, byte[] bytes) throws UnsupportedEncodingException {
        List<String> returnList = new ArrayList<>();
        for (int i = start; i < start + (dataLength * numberOfChannels); i += dataLength) {
            returnList.add(new String(bytes, i, dataLength, "US-ASCII").trim());
        }
        return returnList;
    }

    public void readHeader() {

        try {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            
            fileChannel.read(buffer, 0);
            byte[] bytes = buffer.array();
            
            headerType = Integer.valueOf(bytes[0]);

            patient = new String(bytes, 8, 80, "US-ASCII");
            recording = new String(bytes, 88, 80, "US-ASCII");
            startDate = new String(bytes, 168, 8, "US-ASCII");
            startTime = new String(bytes, 176, 8, "US-ASCII");
            numberOfBytes = Integer.parseInt(new String(bytes, 184, 8, "US-ASCII").trim());
            version = new String(bytes, 192, 44, "US-ASCII");
            numberOfDataRecords = Integer.parseInt(new String(bytes, 236, 8, "US-ASCII").trim());
            durationOfDataRecord = Integer.parseInt(new String(bytes, 244, 8, "US-ASCII").trim());
            numberOfChannels = Integer.parseInt(new String(bytes, 252, 4, "US-ASCII").trim());

            buffer = ByteBuffer.allocate(256*numberOfChannels);
            fileChannel.read(buffer, 256);
            bytes = buffer.array();
            
            int start = 0;
            int dataLength = 16;
            labelsOfTheChannels = readStrings(start, dataLength, bytes);
            start += (dataLength * numberOfChannels);

            dataLength = 80;
            transducerTypes = readStrings(start, dataLength, bytes);
            start += (dataLength * numberOfChannels);

            dataLength = 8;
            physicalDimensionOfChannels = readStrings(start, dataLength, bytes);
            start += (dataLength * numberOfChannels);

            dataLength = 8;
            physicalMinimums = readASCII_Integers(start, dataLength, bytes);
            start += (dataLength * numberOfChannels);

            dataLength = 8;
            physicalMaximums = readASCII_Integers(start, dataLength, bytes);
            start += (dataLength * numberOfChannels);

            dataLength = 8;
            digitalMinimums = readASCII_Integers(start, dataLength, bytes);
            start += (dataLength * numberOfChannels);

            dataLength = 8;
            digitalMaximums = readASCII_Integers(start, dataLength, bytes);
            start += (dataLength * numberOfChannels);

            dataLength = 80;
            prefilterings = readStrings(start, dataLength, bytes);
            start += (dataLength * numberOfChannels);

            dataLength = 8;
            numberOfSamples = readASCII_Integers(start, dataLength, bytes);
            start += (dataLength * numberOfChannels);

            dataLength = 32;
            start += (dataLength * numberOfChannels);

            startData = (numberOfChannels+1)*256;

            dataRecordSize = 0;
            for (int i = 0; i < numberOfChannels; i++) {
                dataRecordSize += numberOfSamples.get(i) * 3;
            }

        } catch (IOException ex) {
            Logger.getLogger(BDF_File.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public Channel getChannel(int channelNumber) throws IOException {
        Channel channel = new Channel();
        
        channel.setLabel(labelsOfTheChannels.get(channelNumber));
        channel.setTransducerType(transducerTypes.get(channelNumber));
        channel.setPhysicalDimension(physicalDimensionOfChannels.get(channelNumber));
        channel.setPhysicalMinimum(physicalMinimums.get(channelNumber));
        channel.setPhysicalMaximum(physicalMaximums.get(channelNumber));
        channel.setDigitalMinimum(digitalMinimums.get(channelNumber));
        channel.setDigitalMaximum(digitalMaximums.get(channelNumber));
        channel.setPrefiltering(prefilterings.get(channelNumber));
        channel.calculateValues();
        
        int dataLength = 3;
        int n = numberOfSamples.get(channelNumber);
        int startInRecord = getStartInRecord(channelNumber);
        int i;
        for (int dataRecordNumber = 0; dataRecordNumber < numberOfDataRecords; dataRecordNumber++) {
            int start = startData + dataRecordNumber * dataRecordSize + startInRecord;
            int end =start + (dataLength * n);
            
            ByteBuffer buffer = ByteBuffer.allocate(dataLength*n);
            fileChannel.read(buffer, start);
            for (i = 0; i < buffer.limit(); i += dataLength) {
                channel.add(new Sample(buffer.get(i), buffer.get(i + 1), buffer.get(i + 2)));
            }
        }
        return channel;
    }
    

    private Integer getStartInRecord(int channelNumber) {
        int startInRecord = 0;
        for (int i = 0; i < channelNumber; i++) {
            startInRecord += numberOfSamples.get(i) * 3;
        }
        return startInRecord;
    }
    
    public void printHeader() {
        System.out.println("headerType:" + headerType);
        System.out.println("patient:" + patient);
        System.out.println("recording:" + recording);
        System.out.println("startDate:" + startDate);
        System.out.println("startTime:" + startTime);
        System.out.println("numberOfBytes:" + numberOfBytes);
        System.out.println("version:" + version);
        System.out.println("numberOfDataRecords:" + numberOfDataRecords);
        System.out.println("durationOfDataRecord:" + durationOfDataRecord);
        System.out.println("numberOfChannels:" + numberOfChannels);
        System.out.println("labelsOfTheChannels:" + labelsOfTheChannels);
        System.out.println("transducerTypes:" + transducerTypes);
        System.out.println("physicalDimensionOfChannels:" + physicalDimensionOfChannels);
        System.out.println("physicalMinimums:" + physicalMinimums);
        System.out.println("physicalMaximums:" + physicalMaximums);
        System.out.println("digitalMinimums:" + digitalMinimums);
        System.out.println("digitalMaximums:" + digitalMaximums);
        System.out.println("prefilterings:" + prefilterings);
        System.out.println("numberOfSamples:" + numberOfSamples);
    }

    public String getFilename() {
        return filename;
    }

    public Integer getHeaderType() {
        return headerType;
    }

    public String getPatient() {
        return patient;
    }

    public String getRecording() {
        return recording;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public Integer getNumberOfBytes() {
        return numberOfBytes;
    }

    public String getVersion() {
        return version;
    }

    public Integer getHeaderSize() {
        return headerSize;
    }

    public Integer getNumberOfDataRecords() {
        return numberOfDataRecords;
    }

    public Integer getDurationOfDataRecord() {
        return durationOfDataRecord;
    }

    public Integer getNumberOfChannels() {
        return numberOfChannels;
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

    public List<Channel> getChannels() {
        return channels;
    }

    public Integer getStartData() {
        return startData;
    }

    public Integer getDataRecordSize() {
        return dataRecordSize;
    }

}
