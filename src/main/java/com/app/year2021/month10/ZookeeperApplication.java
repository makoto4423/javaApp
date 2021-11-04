package com.app.year2021.month10;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

@Slf4j
public class ZookeeperApplication {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        CuratorFramework client = CuratorFrameworkFactory.newClient("10.8.4.190:9000", 15 * 1000, 9 * 1000, new RetryNTimes(-1, 0));
        log.info("cost0  :" + (System.currentTimeMillis()-start)/1000);
        start = System.currentTimeMillis();
        client.start();
        log.info("cost1  :" + (System.currentTimeMillis()-start)/1000);
        start = System.currentTimeMillis();
        try{
            client.getData().forPath("/");
        }catch (Exception e){

        }
        log.info("cost2  :" + (System.currentTimeMillis()-start)/1000);
        start = System.currentTimeMillis();
    }


}
