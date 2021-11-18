/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edf_filereader;

import edf_filereader.header.BDF_Header;
import edf_filereader.data.Channel;
import edf_filereader.data.Channel_Simplified;
import edf_filereader.data.ContinuousData;
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
public class BDF_File_Simplified extends EEG_Class{

    
    public BDF_File_Simplified(String filename) {
        header= new BDF_Header();
        header.setFilename(filename);

        try {
            header.setFileChannel(new FileInputStream(filename).getChannel());
        } catch (IOException ex) {
            Logger.getLogger(BDF_File_Simplified.class.getName()).log(Level.SEVERE, null, ex);
        }
        readHeader();
    }
    
    public BDF_Header getHeader() {
        return (BDF_Header) header;
    }

    private List<Integer> readASCII_Integers(int start, int dataLength, byte[] bytes) throws UnsupportedEncodingException {
        List<Integer> returnList = new ArrayList<>();
        for (int i = start; i < start + (dataLength * header.getNumberOfChannels()); i += dataLength) {
            returnList.add(Integer.parseInt(new String(bytes, i, dataLength, "US-ASCII").trim()));
        }
        return returnList;
    }

    private List<String> readStrings(int start, int dataLength, byte[] bytes) throws UnsupportedEncodingException {
        List<String> returnList = new ArrayList<>();
        for (int i = start; i < start + (dataLength * header.getNumberOfChannels()); i += dataLength) {
            returnList.add(new String(bytes, i, dataLength, "US-ASCII").trim());
        }
        return returnList;
    }

    @Override
    public void readHeader() {

        try {
            ByteBuffer buffer = ByteBuffer.allocate(256);

            header.getFileChannel().read(buffer, 0);
            byte[] bytes = buffer.array();

            getHeader().setHeaderType(Integer.valueOf(bytes[0]));

            getHeader().setPatient(new String(bytes, 8, 80, "US-ASCII"));
            getHeader().setRecording(new String(bytes, 88, 80, "US-ASCII"));
            getHeader().setStartDate(new String(bytes, 168, 8, "US-ASCII"));
            getHeader().setStartTime(new String(bytes, 176, 8, "US-ASCII"));
            getHeader().setNumberOfBytes(Integer.parseInt(new String(bytes, 184, 8, "US-ASCII").trim()));
            getHeader().setVersion(new String(bytes, 192, 44, "US-ASCII"));
            header.setNumberOfDataRecords(Integer.parseInt(new String(bytes, 236, 8, "US-ASCII").trim()));
            getHeader().setDurationOfDataRecord(Integer.parseInt(new String(bytes, 244, 8, "US-ASCII").trim()));
            header.setNumberOfChannels(Integer.parseInt(new String(bytes, 252, 4, "US-ASCII").trim()));

            buffer = ByteBuffer.allocate(256 * header.getNumberOfChannels());
            header.getFileChannel().read(buffer, 256);
            bytes = buffer.array();

            int start = 0;
            int dataLength = 16;
            header.setLabelsOfTheChannels(readStrings(start, dataLength, bytes));
            start += (dataLength * header.getNumberOfChannels());

            dataLength = 80;
            header.setTransducerTypes(readStrings(start, dataLength, bytes));
            start += (dataLength * header.getNumberOfChannels());

            dataLength = 8;
            header.setPhysicalDimensionOfChannels(readStrings(start, dataLength, bytes));
            start += (dataLength * header.getNumberOfChannels());

            dataLength = 8;
            header.setPhysicalMinimums(readASCII_Integers(start, dataLength, bytes));
            start += (dataLength * header.getNumberOfChannels());

            dataLength = 8;
            header.setPhysicalMaximums(readASCII_Integers(start, dataLength, bytes));
            start += (dataLength * header.getNumberOfChannels());

            dataLength = 8;
            header.setDigitalMinimums(readASCII_Integers(start, dataLength, bytes));
            start += (dataLength * header.getNumberOfChannels());

            dataLength = 8;
            header.setDigitalMaximums(readASCII_Integers(start, dataLength, bytes));
            start += (dataLength * header.getNumberOfChannels());

            dataLength = 80;
            header.setPrefilterings(readStrings(start, dataLength, bytes));
            start += (dataLength * header.getNumberOfChannels());

            dataLength = 8;
            header.setNumberOfSamples(readASCII_Integers(start, dataLength, bytes));
            start += (dataLength * header.getNumberOfChannels());

            dataLength = 32;
            start += (dataLength * header.getNumberOfChannels());

            getHeader().setStartData((header.getNumberOfChannels() + 1) * 256);

            int dataRecordSize = 0;
            for (int i = 0; i < header.getNumberOfChannels(); i++) {
                dataRecordSize += header.getNumberOfSample(i) * 3;
            }
            getHeader().setDataRecordSize(dataRecordSize);

        } catch (IOException ex) {
            Logger.getLogger(BDF_File_Simplified.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Channel_Simplified getChannel(int channelNumber) throws IOException, InterruptedException {
        Channel_Simplified channel = new Channel_Simplified();

        channel.setLabel(header.getLabelsOfTheChannels(channelNumber));
        channel.setTransducerType(header.getTransducerType(channelNumber));
        channel.setPhysicalDimension(header.getPhysicalDimensionOfChannel(channelNumber));
        channel.setPhysicalMinimum(header.getPhysicalMinimum(channelNumber));
        channel.setPhysicalMaximum(header.getPhysicalMaximum(channelNumber));
        channel.setDigitalMinimum(header.getDigitalMinimum(channelNumber));
        channel.setDigitalMaximum(header.getDigitalMaximum(channelNumber));
        channel.setPrefiltering(header.getPrefiltering(channelNumber));
        channel.calculateValues();

        readBytesToChannel(channelNumber, channel, 0, header.getNumberOfDataRecords());

        return channel;
    }

    private void readBytesToChannel(int channelNumber, Channel_Simplified channel, int fromRecord, int toRecord) throws IOException {
        int dataLength = 3;
        int n = header.getNumberOfSample(channelNumber);
        int startInRecord = getStartInRecord(channelNumber);
        channel.samples = new byte[(toRecord - fromRecord) * n * dataLength];
        for (int dataRecordNumber = fromRecord; dataRecordNumber < toRecord; dataRecordNumber++) {
            int start = getHeader().getStartData() + dataRecordNumber * getHeader().getDataRecordSize() + startInRecord;

            ByteBuffer buffer = ByteBuffer.allocate(dataLength * n);
            header.getFileChannel().read(buffer, start);
            int offset = header.getNumberOfSample(channelNumber) * dataRecordNumber * 3;
            buffer.get(0, channel.samples, offset, buffer.limit());

        }
    }

    @Override
    public ContinuousData readRecordFromTo(int from, int to) throws IOException {
        int length = to - from;
        int dataLength = 3;
        int start = getHeader().getStartData() + from * getHeader().getDataRecordSize();
        ByteBuffer buffer = ByteBuffer.allocate(getHeader().getDataRecordSize() * (length));
        header.getFileChannel().read(buffer, start);
        ContinuousData data = new ContinuousData(length, header.getNumberOfChannels(), header.getLabelsOfTheChannels(), header.getTransducerTypes(),
                header.getPhysicalDimensionOfChannels(), header.getPhysicalMinimums(), header.getPhysicalMaximums(),
                header.getDigitalMinimums(), header.getDigitalMaximums(), header.getPrefilterings(), header.getNumberOfSamples());
        for (int channelNumber = 0; channelNumber < header.getNumberOfChannels(); channelNumber++) {
            for (int recordNumber = 0; recordNumber < length; recordNumber++) {
                int channelStart = recordNumber * getHeader().getDataRecordSize() + getStartInRecord(channelNumber);
                int n = header.getNumberOfSample(channelNumber);
                int offset = n * recordNumber * dataLength;
                buffer.get(channelStart, data.channels[channelNumber].samples, offset, dataLength * n);
            }
        }
        return data;
    }

    private Integer getStartInRecord(int channelNumber) {
        int startInRecord = 0;
        for (int i = 0; i < channelNumber; i++) {
            startInRecord += header.getNumberOfSample(i) * 3;
        }
        return startInRecord;
    }

}
