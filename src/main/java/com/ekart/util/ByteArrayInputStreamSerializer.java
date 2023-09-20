package com.ekart.util;

import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import java.io.*;

public class ByteArrayInputStreamSerializer implements Serializer<ByteArrayInputStream>, Deserializer<ByteArrayInputStream> {
    @Override
    public void serialize(ByteArrayInputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] data = inputStream.readAllBytes();
        outputStream.write(data);
    }

    @Override
    public ByteArrayInputStream deserialize(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }




}

