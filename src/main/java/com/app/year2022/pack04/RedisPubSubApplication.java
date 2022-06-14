package com.app.year2022.pack04;

import lombok.SneakyThrows;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;

public class RedisPubSubApplication {

    public static void main(String[] args){
        sub();
        pub();
    }

    public static void pub(){
        Thread thread = new Thread(){

            @Override
            public void run() {
                RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
                configuration.setHostName("10.8.4.190");
                configuration.setPort(6379);
                LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
                factory.afterPropertiesSet();
                RedisTemplate<String, Object> template = new RedisTemplate<>();
                template.setConnectionFactory(factory);
                template.setKeySerializer(new StringRedisSerializer());
                template.setHashKeySerializer(new StringRedisSerializer());
                template.setHashValueSerializer(new StringRedisSerializer());
                template.setValueSerializer(new StringRedisSerializer());
                template.afterPropertiesSet();
                int i = 0;
                while (true){
                    template.getRequiredConnectionFactory().getConnection().publish("channel".getBytes(),(i++ + "now is "+System.currentTimeMillis()).getBytes());
                }

            }
        };
        thread.start();
    }

    public static void sub() {
        Thread thread = new Thread() {

            @Override
            public void run() {
                RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
                configuration.setHostName("10.8.4.190");
                configuration.setPort(6379);
                LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
                factory.afterPropertiesSet();
                RedisTemplate<String, Object> template = new RedisTemplate<>();
                template.setConnectionFactory(factory);
                template.setKeySerializer(new StringRedisSerializer());
                template.setHashKeySerializer(new StringRedisSerializer());
                template.setHashValueSerializer(new StringRedisSerializer());
                template.setValueSerializer(new StringRedisSerializer());
                template.afterPropertiesSet();
                template.getRequiredConnectionFactory().getConnection().subscribe(new MessageListener() {
                    @SneakyThrows
                    @Override
                    public void onMessage(Message message, byte[] pattern) {
                        System.out.println(new String(message.getBody(), StandardCharsets.UTF_8.toString()));
                        throw new RuntimeException("end");
                    }
                }, "channel".getBytes());

            }
        };
        thread.start();
    }

}
