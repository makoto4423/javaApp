package com.app.pack04;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class App0413 {

    public static void main(String[] args){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName("10.8.4.190");
        configuration.setPort(6390);
        configuration.setDatabase(0);
        RedisConnectionFactory factory = new JedisConnectionFactory(configuration);
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        template.opsForValue().set("world","grey");
        template.opsForList().leftPush("list","dancer");
        System.out.println(template.opsForList().range("list",0,10));
    }

}
