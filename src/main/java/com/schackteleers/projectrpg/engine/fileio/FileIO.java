package com.schackteleers.projectrpg.engine.fileio;

import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * @author Stijn Schack
 * @since 27/04/2017
 */
public class FileIO {
    public static String loadShaderResource(String fileName) throws IOException {
        String shaderCode;
        try (InputStream in = FileIO.class.getClass().getResourceAsStream(fileName); Scanner scanner = new Scanner(in, "UTF-8")) {
            shaderCode = scanner.useDelimiter("\\A").next();
        }
        return shaderCode;
    }

    public static List<String> readAllLines(String fileName) throws IOException {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(FileIO.class.getClass().getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    public static ByteBuffer loadResourceToByteBuffer(String fileName, int bufferSize) throws IOException {
        long startTime = System.nanoTime();
        ByteBuffer buffer;
        try (InputStream in = FileIO.class.getResourceAsStream(fileName); ReadableByteChannel byteChannel = Channels.newChannel(in)) {
            buffer = BufferUtils.createByteBuffer(bufferSize);
            while (true) {
                int bytes = byteChannel.read(buffer);
                if (bytes == -1) break;
                if (buffer.remaining() == 0) buffer = resizeBuffer(buffer, buffer.capacity() * 2);
            }
        }
        buffer.flip();
        long totalTime = System.nanoTime() - startTime;
        System.out.println(fileName + " loaded to ByteBuffer in " + totalTime + "ns");
        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
