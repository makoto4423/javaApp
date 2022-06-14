package com.app.year2022.pack05;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ScriptApplication {

    public static void main(String[] args) throws ScriptException, NoSuchMethodException, FileNotFoundException {
        ScriptEngineManager manager = new ScriptEngineManager();
        manager.getEngineFactories().forEach(f ->{
            System.out.println(f.getEngineName()+"-"+f.getEngineVersion()+"-"+f.getLanguageName()+"-"+f.getLanguageVersion());
        });
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        if (!(engine instanceof Invocable)) {
            System.out.println("Invoking methods is not supported.");
            return;
        }
        for(int i = 0;i<2;i++){
            engine.put("JavaOkHttp", OkHttp.class);
            String path = "D:\\intellij\\javaApp\\src\\main\\resources\\a.js";
            File file = new File(path);
            if (!file.exists()) return ;
            StringBuilder sb = new StringBuilder("var OkHttp = JavaOkHttp.static;");
            try (FileChannel channel = FileChannel.open(Paths.get(path), StandardOpenOption.READ)) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int index;
                while ((index = channel.read(buffer)) != -1) {
                    sb.append(new String(buffer.array(),0, index));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String s = sb.toString();
            engine.eval(s);

            Object result = ((Invocable) engine).invokeFunction("main");
            List<Object> list = parseResult(JSONObject.toJSONString(result));
        }


    }


    public static List<Object> parseResult(String result) {
        if(StringUtils.isEmpty(result)) return Collections.emptyList();
        JSONObject jsonObject = JSONObject.parseObject(result);
        List<Object> ans = new ArrayList<>();
        if(jsonObject.containsKey("0")){
            for(int i = 0;i<jsonObject.keySet().size();i++){
                ans.add(jsonObject.get(i+""));
            }
        }else{
            ans.add(jsonObject);
        }
        parse(ans);
        return ans;
    }

    public static void parse(List<Object> ans){
        for (Object obj : ans) {
            if (obj instanceof JSONObject) {
                recur((JSONObject) obj);
            }
        }
    }

    public static List<Object> recur(JSONObject object){
        if(object.containsKey("0")){
            List<Object> list = new ArrayList<>();
            for(int i = 0;i<object.keySet().size();i++){
                Object obj = object.get(i+"");
                if(obj instanceof JSONObject){
                    List<Object> r = recur((JSONObject) obj);
                    if(r != null){
                        obj = r;
                    }
                }
                list.add(obj);
            }
            return list;
        }else{
            for(String key : object.keySet()){
                Object val = object.get(key);
                if(val instanceof JSONObject){
                    List<Object> r = recur((JSONObject) val);
                    if(r != null){
                        object.put(key, r);
                    }
                }
            }
            return null;
        }
    }
}
