package com.app.pack0617;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class Json2Application {

    public static void main(String[] args) throws JsonProcessingException {
        String s = "AUTH-CENTER: http://10.8.6.125:8070\n" +
                "server:\n" +
                "  port: 8094";
        YAMLMapper mapper = new YAMLMapper();
        JSONObject object = mapper.readValue(s, JSONObject.class);
    }

}
