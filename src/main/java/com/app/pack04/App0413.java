package com.app.pack04;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.protocol.RedisCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Duration;
import java.util.*;

@Slf4j
public class App0413 {

    public static void main(String[] args) {
        cluster();
    }


    public static void single() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName("10.8.4.190");
        configuration.setPort(6376);
        configuration.setDatabase(0);
        configuration.setPassword("1234");
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration,getClientConfig());
        factory.afterPropertiesSet();
//        RedisStandaloneConfiguration configuration2 = new RedisStandaloneConfiguration();
//        configuration2.setHostName("10.8.4.190");
//        configuration2.setPort(6372);
//        configuration2.setDatabase(0);
//        configuration2.setPassword("1234");
//        LettuceConnectionFactory factory2 = new LettuceConnectionFactory(configuration2);
//        factory2.afterPropertiesSet();
        long start = System.currentTimeMillis();
        try {
            factory.getConnection();
        } catch (Exception e) {

        }
        log.info(" EEE cost " + (System.currentTimeMillis()-start));
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setValueSerializer(new MyRedisSerializer());
        template.afterPropertiesSet();
        template.rename("makoto","L.");
//        A a = new A();
//        ByteArrayOutputStream bo = new ByteArrayOutputStream();
//        ObjectOutputStream oo = null;
//        try{
//            oo = new ObjectOutputStream(bo);
//            oo.writeObject(a);
//            template.opsForValue().set("a",bo.toByteArray());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        template.opsForValue().get("auth:a70e2742-cb51-43f4-ac84-77db6f4b8beb");
//        template.opsForValue().set("world", 1);
//        template.opsForSet().add("set",new A(),new A());
//        template.opsForList().leftPush("list", "dancer");
//        DefaultRedisScript<String> script = new DefaultRedisScript<>();
//        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/redis/getDatabaseNum.lua")));
//        template.execute(script,Collections.emptyList(),"");
//        Properties properties = template.getConnectionFactory().getConnection().getConfig("*");
//        assert properties != null;
//        properties.forEach((k, v) -> {
//            if (k.toString().contains("database")) {
//                System.out.println("key : " + k + "; value : " + v);
//            }
//        });
//        System.out.println(template.opsForList().range("list", 0, 10));
    }


    public static void sentinel() {
        RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
        configuration.addSentinel(new RedisNode("10.8.4.190", 26380));
        configuration.addSentinel(new RedisNode("10.8.4.190", 26381));
        configuration.addSentinel(new RedisNode("10.8.4.190", 26379));
        configuration.setMaster("mymaster");
//        configuration.setDatabase(0);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration, getClientConfig());
        factory.afterPropertiesSet();
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
//        template.getConnectionFactory().getSentinelConnection().masters();
//        template.opsForValue().set("abc","abc");
//        System.out.println(template.opsForValue().get("abc"));
    }

    public static void replica() {
        RedisStaticMasterReplicaConfiguration configuration = new RedisStaticMasterReplicaConfiguration("10.8.4.190", 6380);
        configuration.addNode("10.8.4.190", 6381);
        configuration.addNode("10.8.4.190", 6382);
        configuration.setDatabase(0);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
        factory.afterPropertiesSet();
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        template.opsForValue().set("abc", "abc");
    }

    public static void cluster() {
        RedisClusterConfiguration configuration = new RedisClusterConfiguration();
        configuration.addClusterNode(new RedisNode("10.8.4.190", 6372));
        configuration.addClusterNode(new RedisNode("10.8.4.190", 6373));
        configuration.addClusterNode(new RedisNode("10.8.4.190", 6375));

        configuration.addClusterNode(new RedisNode("10.8.4.190", 6371));
        configuration.addClusterNode(new RedisNode("10.8.4.190", 6374));
        configuration.addClusterNode(new RedisNode("10.8.4.190", 6376));
        configuration.setPassword("1234");
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
        factory.afterPropertiesSet();
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        template.opsForValue().set("abc","ddd");
        System.out.println(template.opsForValue().get("abc"));
    }

    public static LettucePoolingClientConfiguration getClientConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig =
                new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(10);
        genericObjectPoolConfig.setMinIdle(2);
        genericObjectPoolConfig.setMaxTotal(20);
        genericObjectPoolConfig.setMaxWaitMillis(Duration.ofSeconds(10L).toMillis());

        return LettucePoolingClientConfiguration.builder().
                clientOptions(ClientOptions.builder().socketOptions(
                        SocketOptions.builder().connectTimeout(Duration.ofSeconds(4L)).build()).build()).
                build();
    }

    public static class MyRedisSerializer implements RedisSerializer<Object> {

        private static final JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();

        @Override
        public byte[] serialize(Object o) throws SerializationException {
            return serializer.serialize(o);
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            try {
                return serializer.deserialize(bytes);
            } catch (SerializationException e) {
                return bytes;
            }
        }
    }

    public static class A implements Serializable {
        public static final String s = "";

        String value = "value";

        public final void func() {

        }
    }
}
