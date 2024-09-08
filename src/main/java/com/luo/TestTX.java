package com.luo;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestTX {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.31.201",6379);
        jedis.auth("redis123");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "world250");
        jsonObject.put("name", "luowensa");
        Transaction multi = jedis.multi(); //开启事务
        //multi.watch("user1"); //watch 作为乐观锁
        try {
            multi.set("user1", jsonObject.toJSONString());
            int x = 1/0;
            multi.set("user2", jsonObject.toJSONString());
            multi.exec();
        }catch (Exception e){
            e.printStackTrace();
            multi.discard(); //抛弃事务
        }finally {
            System.out.println(jedis.get("user1"));
            System.out.println(jedis.get("user2"));
        }

        jedis.close(); //关闭连接
    }
}
