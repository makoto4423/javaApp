package com.app.pack0617;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

public class JsonApplication {

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("field", "abc");
        ArrayNode nullArr = (ArrayNode) node.get("null");
        ArrayNode array = mapper.createArrayNode();
        for (int i = 0; i < 10; i++) {
            ObjectNode arrNode = mapper.createObjectNode();
            arrNode.put("No." + i, i);
            array.add(arrNode);
        }
        // array.add(mapper.createArrayNode());
        for (int i = 0; i < array.size(); i++) {
            ObjectNode arrNode = (ObjectNode) array.get(i);
            arrNode.put("No." + i, i + 10);
        }
        node.set("arr", array);
        String s = node.toString();
        node = (ObjectNode) mapper.readTree(s);
        System.out.println(node.toString());
        ObjectMapper objectMapper = new ObjectMapper();

        Car car = new Car();
        car.brand = "Cadillac";
        car.doors = 4;


        JsonNode carJsonNode = objectMapper.valueToTree(car);
        String ss = carJsonNode.toString();
        mapper.readValue(ss,JsonNode.class);
        mapper.valueToTree(ss);
    }

    @Data
    static class Car{
        String brand;
        int doors;
    }



}
