package com.app.pack0617;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadApplication2 {

    public static void main(String[] args){
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Map<Integer,Future<?>> futureMap = new HashMap<>();
        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://127.0.0.1:444/abc/kill");
        for(int i=0;i<5;i++){
            futureMap.put(i,executorService.submit(() -> {
                try {
                    client.execute(get);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("finished");
            }));
        }
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis()-start<= 2000){
            Iterator<Integer> iterator = futureMap.keySet().iterator();
            while (iterator.hasNext()){
                Integer k = iterator.next();
                Future<?> future = futureMap.get(k);
                if(future.isDone()){
                    iterator.remove();
                }
            }
        }
        if(!futureMap.isEmpty()){
            System.out.println(futureMap.size()+" error");
        }
        executorService.shutdownNow();
    }

}
