package org.example.carservice.kafka;

import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class EventSerializer implements Serializer<Object> {

    @SneakyThrows
    @Override
    public byte[] serialize(String topic, Object data) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(data);
        objectStream.flush();
        objectStream.close();
        return byteStream.toByteArray();
    }
}
