package com.app.year2021.month08;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

// protobuf 根据字段的顺序反序列化，类型不匹配抛异常
public class ProtoStuffApp {

    public static void main(String[] args){
        From from = new From();
        Schema<From> schema = RuntimeSchema.getSchema(From.class);
        LinkedBuffer buffer = LinkedBuffer.allocate();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes;
        try {
            ProtobufIOUtil.writeTo(outputStream,from,schema,buffer);
            bytes = outputStream.toByteArray();
            To to = new To();
            Schema<To> toSchema = RuntimeSchema.getSchema(To.class);
            ProtobufIOUtil.mergeFrom(bytes,to,toSchema);
            ObjectMapper mapper = new JsonMapper();
            System.out.println(mapper.writeValueAsString(to));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    static class From{
        String name = "from";
        int age = 10;
        String year = "none";
        int month = 8;
    }

    @Data
    static class To{
        String name;
        int age;
        String month;
    }

}
