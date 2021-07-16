package com.app.year2021.month07;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CacheApplication {

    public static void main(String[] args) throws InterruptedException {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                //缓存池大小，在缓存项接近该大小时， Guava开始回收旧的缓存项
                .maximumSize(20)
                //设置时间对象没有被读/写访问则对象从内存中删除(在另外的线程里面不定期维护)
                .expireAfterAccess(10, TimeUnit.SECONDS)
                //移除监听器,缓存项被移除时会触发
                .removalListener((RemovalListener<String, String>) rn -> {
                    //执行逻辑操作

                })
                //开启Guava Cache的统计功能
                .recordStats()
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) {
                        return null;
                    }
                });
        cache.put("abc", "dddd");
        cache.getIfPresent("abc");
        System.out.println(cache.getIfPresent("abc"));
    }

}
