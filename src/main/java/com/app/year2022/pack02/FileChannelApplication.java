package com.app.year2022.pack02;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileChannelApplication {

    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir") + "\\src\\main\\java\\com\\app\\year2022\\pack02\\abc.txt";
        System.out.println(read(path));
        write(path, "path");
    }


    public static String read(String path) throws IOException {
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
        byte[] remainByte;
        int left;
        try (FileChannel channel = FileChannel.open(Paths.get(path), StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuilder sb = new StringBuilder();
            while ((channel.read(buffer)) != -1) {
                buffer.flip();
                decoder.decode(buffer, charBuffer, true);
                charBuffer.flip();
                remainByte = null;
                left = buffer.limit() - buffer.position();
                if (left > 0) {
                    remainByte = new byte[left];
                    buffer.get(remainByte, 0, left);
                }
                if (charBuffer.hasRemaining()) {
                    sb.append(charBuffer.array(), 0, charBuffer.length());
                }
                buffer.clear();
                charBuffer.clear();
                if (remainByte != null) {
                    buffer.put(remainByte);
                }
            }
            return sb.toString();
        }

    }

    public static void write(String path, String content) throws IOException {
        File file = new File(path);
        if(!file.exists()) {
            boolean b = file.createNewFile();
            if(!b) throw new RuntimeException("create file error");
        }
        try (FileChannel channel = FileChannel.open(Paths.get(path), StandardOpenOption.WRITE)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            byte[] bytes = content.getBytes();
            for (int i = 0; i < bytes.length; ) {
                buffer.put(bytes, i, Math.min(bytes.length - i, buffer.limit() - buffer.position()));
                buffer.flip();
                i += channel.write(buffer);
                buffer.compact();
            }
            channel.force(false);
        }
    }

}
