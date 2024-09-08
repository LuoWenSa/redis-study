package com.luo;

import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luo.pojo.User;
import com.luo.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class Redis02SpringbootApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Test
    void contextLoads() {
        //redisTemplate 操作不同的数据类型，api和我们的指令是一样的
        //opsForValue 操作字符串类似String
        //opsForList 操作List类似List
        //...
        //我们常用的方法也可以通过redisTemplate进行操作

//        获取redis的连接对象
//        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
//        connection.flushDb();
        redisTemplate.opsForValue().set("hero", "ironMan");
        System.out.println("redisTemplate.opsForValue().get(\"hero\") = " + redisTemplate.opsForValue().get("hero"));

    }

    @Test
    public void test() throws JsonProcessingException {
        // 真实的开发一般都使用json来传递对象
        User user = new User("罗文洒", 3);
        //String jsonUser = new ObjectMapper().writeValueAsString(user);
        redisTemplate.opsForValue().set("user", user);
        System.out.println(redisTemplate.opsForValue().get("user"));
    }

    @Test
    public void test1(){
        System.out.println(redisUtil.get("user"));
    }
}
