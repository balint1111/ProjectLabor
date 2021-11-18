/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileReader_teszt;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.System.out;
import static java.nio.channels.FileChannel.MapMode.READ_ONLY;
import static java.nio.channels.FileChannel.MapMode.READ_WRITE;
import java.util.Random;

public final class FileReader_teszt {

    public static int PAGE_SIZE = 64 * 1024;
    public static final long FILE_SIZE = PAGE_SIZE * 1024 * 4;
    public static final String FILE_NAME = "test.txt";
    public static final byte[] BLANK_PAGE = new byte[PAGE_SIZE];

    public static void main(final String[] arg) throws Exception {
//        randomPage();

        

        
        
        while (PAGE_SIZE != 512) {
            
            String[] strArr = new String[1];
            strArr[0] = "standbylist";
            Runtime.getRuntime().exec("EmptyStandbyList.exe", strArr);

            Thread.sleep(25000);
            
            deleteFile(FILE_NAME);
            preallocateTestFile(FILE_NAME);

            strArr = new String[1];
            strArr[0] = "standbylist";
            Runtime.getRuntime().exec("EmptyStandbyList.exe", strArr);

            System.gc();
            
            Thread.sleep(25000);
            
            

            System.out.println("Buffer Size: " + (double) PAGE_SIZE / (double) 1024 + " Kb");
            for (final PerfTestCase testCase : testCases) {
                for (int i = 0; i < 5; i++) {
                    strArr = new String[1];
                    strArr[0] = "standbylist";
                    Runtime.getRuntime().exec("EmptyStandbyList.exe", strArr);
                    
                    System.gc();

                    Thread.sleep(1000);

                    long readDurationMs = testCase.test(PerfTestCase.Type.READ,
                            FILE_NAME);

                    long bytesReadPerSec = (FILE_SIZE * 1000L) / readDurationMs;
                    out.format("%s\tread=%,d bytes/sec\n",
                            testCase.getName(), bytesReadPerSec);
                }
//            for (int i = 0; i < 5; i++)
//            {
//                System.gc();
//                long writeDurationMs = testCase.test(PerfTestCase.Type.WRITE,
//                                                     FILE_NAME);
//
//                long bytesWrittenPerSec = (FILE_SIZE * 1000L) / writeDurationMs;
//
//                out.format("%sd\twrite=%,d bytes/sec\n",
//                           testCase.getName(), bytesWrittenPerSec);
//            }
            }

            PAGE_SIZE /= 2;
        }

        deleteFile(FILE_NAME);
    }

    private static void randomPage() {
        new Random().nextBytes(BLANK_PAGE);
    }

    private static void preallocateTestFile(final String fileName)
            throws Exception {
        RandomAccessFile file = new RandomAccessFile(fileName, "rw");

        for (long i = 0; i < FILE_SIZE; i += PAGE_SIZE) {
            file.write(BLANK_PAGE, 0, PAGE_SIZE);
        }

        file.close();
//        RandomAccessFile file = new RandomAccessFile(fileName, "rw");
//
//        String str = "ABCDEFGHIJKLMNOPQRSTXYZ";
//        Random rnd = new Random();
//        for (long i = 0; i < FILE_SIZE; i += PAGE_SIZE)
//        {
//            int rndNum = str.charAt(rnd.nextInt(str.length()));
//            System.out.println(rndNum);
//            file.writeChar(rndNum);
//        }
//
//        file.close();
    }

    private static void deleteFile(final String testFileName) throws Exception {
        File file = new File(testFileName);
        if (!file.delete()) {
            out.println("Failed to delete test file=" + testFileName);
            out.println("Windows does not allow mapped files to be deleted.");
        }
    }

    public abstract static class PerfTestCase {

        public enum Type {
            READ, WRITE
        }

        private final String name;
        private int checkSum;

        public PerfTestCase(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public long test(final Type type, final String fileName) {
            long start = System.currentTimeMillis();

            try {
                switch (type) {
                    case WRITE: {
                        checkSum = testWrite(fileName);
                        break;
                    }

                    case READ: {
                        final int checkSum = testRead(fileName);
//                        if (checkSum != this.checkSum)
//                        {
//                            final String msg = getName() +
//                                " expected=" + this.checkSum +
//                                " got=" + checkSum;
//                            throw new IllegalStateException(msg);
//                        }
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return System.currentTimeMillis() - start;
        }

        public abstract int testWrite(final String fileName) throws Exception;

        public abstract int testRead(final String fileName) throws Exception;
    }

    private static PerfTestCase[] testCases
            = {
                new PerfTestCase("RandomAccessFile") {
            public int testWrite(final String fileName) throws Exception {
                RandomAccessFile file = new RandomAccessFile(fileName, "rw");
                byte[] buffer = new byte[PAGE_SIZE];
                int pos = 0;
                int checkSum = 0;

                for (long i = 0; i < FILE_SIZE; i++) {
                    byte b = (byte) i;
                    checkSum += b;

                    buffer[pos++] = b;
                    if (PAGE_SIZE == pos) {
                        file.write(buffer, 0, PAGE_SIZE);
                        pos = 0;
                    }
                }

                file.close();

                return checkSum;
            }

            public int testRead(final String fileName) throws Exception {
                RandomAccessFile file = new RandomAccessFile(fileName, "r");
                byte[] buffer = new byte[PAGE_SIZE];
                int checkSum = 0;
                int bytesRead;

                while (-1 != (bytesRead = file.read(buffer))) {
                    for (int i = 0; i < bytesRead; i++) {
                        checkSum += buffer[i];
                    }
                }

                file.close();

                return checkSum;
            }
        },
                new PerfTestCase("BufferedStreamFile") {
            public int testWrite(final String fileName) throws Exception {
                int checkSum = 0;
                OutputStream out
                        = new BufferedOutputStream(new FileOutputStream(fileName));

                for (long i = 0; i < FILE_SIZE; i++) {
                    byte b = (byte) i;
                    checkSum += b;
                    out.write(b);
                }

                out.close();

                return checkSum;
            }

            public int testRead(final String fileName) throws Exception {
                int checkSum = 0;
                InputStream in
                        = new BufferedInputStream(new FileInputStream(fileName));

                int b;
                while (-1 != (b = in.read())) {
                    checkSum += (byte) b;
                }

                in.close();

                return checkSum;
            }
        },
                new PerfTestCase("BufferedChannelFile") {
            public int testWrite(final String fileName) throws Exception {
                FileChannel channel
                        = new RandomAccessFile(fileName, "rw").getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(PAGE_SIZE);
                int checkSum = 0;

                for (long i = 0; i < FILE_SIZE; i++) {
                    byte b = (byte) i;
                    checkSum += b;
                    buffer.put(b);

                    if (!buffer.hasRemaining()) {
                        channel.write(buffer);
                        buffer.clear();
                    }
                }

                channel.close();

                return checkSum;
            }

            public int testRead(final String fileName) throws Exception {
                FileChannel channel
                        = new RandomAccessFile(fileName, "rw").getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(PAGE_SIZE);
                int checkSum = 0;

                while (-1 != (channel.read(buffer))) {
                    buffer.flip();

                    while (buffer.hasRemaining()) {
                        checkSum += buffer.get();
                    }

                    buffer.clear();
                }

                return checkSum;
            }
        },
                new PerfTestCase("MemoryMappedFile") {
            public int testWrite(final String fileName) throws Exception {
                FileChannel channel
                        = new RandomAccessFile(fileName, "rw").getChannel();
                MappedByteBuffer buffer
                        = channel.map(READ_WRITE, 0,
                                Math.min(channel.size(), MAX_VALUE));
                int checkSum = 0;

                for (long i = 0; i < FILE_SIZE; i++) {
                    if (!buffer.hasRemaining()) {
                        buffer
                                = channel.map(READ_WRITE, i,
                                        Math.min(channel.size() - i, MAX_VALUE));
                    }

                    byte b = (byte) i;
                    checkSum += b;
                    buffer.put(b);
                }

                channel.close();

                return checkSum;
            }

            public int testRead(final String fileName) throws Exception {
                FileChannel channel
                        = new RandomAccessFile(fileName, "rw").getChannel();
                MappedByteBuffer buffer
                        = channel.map(READ_ONLY, 0,
                                Math.min(channel.size(), MAX_VALUE));
                int checkSum = 0;

                for (long i = 0; i < FILE_SIZE; i++) {
                    if (!buffer.hasRemaining()) {
                        buffer
                                = channel.map(READ_WRITE, i,
                                        Math.min(channel.size() - i, MAX_VALUE));
                    }

                    checkSum += buffer.get();
                }

                channel.close();

                return checkSum;
            }
        },};
}
