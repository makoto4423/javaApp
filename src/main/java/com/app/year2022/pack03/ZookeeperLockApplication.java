package com.app.year2022.pack03;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ZookeeperLockApplication {

    public static final String path = "/lock";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zk = new ZooKeeper("192.168.2.132:2181", Integer.MAX_VALUE, null);
        Stat stats = zk.exists(path, false);
        if (stats == null) {
            zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        Thread t1 = createThread();
        Thread t2 = createForeverThread();
        t1.start();
        t2.start();
        zk.close();
    }

    public static Thread createThread(){
        return new Thread(() -> {
            try {
                ZooKeeper z = new ZooKeeper("192.168.2.132:2181", Integer.MAX_VALUE, null);
                lock(z,path);
                System.out.println("i lock"+Thread.currentThread().getId());
                Thread.sleep(10000);
                System.out.println("i unlock"+Thread.currentThread().getId());
                z.close();
            } catch (IOException | InterruptedException | KeeperException e) {
                e.printStackTrace();
            }
        });
    }

    public static Thread createForeverThread(){
        return new Thread(() -> {
            try {
                ZooKeeper z = new ZooKeeper("192.168.2.132:2181", Integer.MAX_VALUE, null);
                lock(z,path);
                System.out.println("i forever lock"+Thread.currentThread().getId());
                while (true){}
            } catch (IOException | InterruptedException | KeeperException e) {
                e.printStackTrace();
            }
        });
    }


    public static void lock(ZooKeeper zk, String path) throws InterruptedException, KeeperException {
        String threadName = Thread.currentThread().getName();
        String p = path+"/"+threadName;
        zk.create(p, null,ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        List<String> list = zk.getChildren(path,false);
        if(list.get(0).startsWith(threadName)){
            return;
        }
        int i = 1;
        for(;i<list.size();i++){
            if(list.get(i).startsWith(threadName)){
                i--;
                break;
            }
        }
        AtomicBoolean flag = new AtomicBoolean(false);
        zk.getData(path+"/"+list.get(i), watchedEvent -> {
            flag.set(true);
        },null );
        while (!flag.get()){
            Thread.sleep(1000);
        }
    }

}
