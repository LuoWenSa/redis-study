package com.luo;

import redis.clients.jedis.Jedis;

public class TestPing {
    public static void main(String[] args) {
        //1.new Jedis对象
        Jedis jedis = new Jedis("192.168.31.201",6379);
        //jedis所有的命令就是我们之前学习的所有指令！
        jedis.auth("redis123");

        System.out.println(jedis.ping());
        jedis.close();
    }
}
