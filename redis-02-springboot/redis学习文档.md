# Redis入门

## 概述

Redis（<font color=red>Re</font>mote <font color=red>Di</font>ctionary <font color=red>S</font>erver）,即远程字典服务！

是一个开源的**使用ANSI C语言编写**、支持网络、可基于内存亦可持久化的日志型、key-Value数据库，并提供多种语言的API。免费和开源！是当下最热门的NoSQL技术之一！也被人们称之为结构化数据库！

官网：https://redis.io/
中文网：https://www.redis.net.cn/

## 用途

1.内存存储、持久化，内存中是断点即失、所以说持久化很重要（rdb、aof）

2.效率高，可以用于高速缓存

3.发布订阅系统（可做简单的消息队列）

4.地图信息分析

5.计数器、计时器（浏览量！）

...

## 特性

1.多样的数据类型

2.持久化

3.集群

4.事务

...

## 安装

1.将安装包放在/usr/local/下

2.在该目录下解压压缩包

```
tar -zxvf redis-5.0.14.tar.gz
```

3.yum安装gcc依赖(redis是用C写的)

```
yum install gcc
```

4.跳转到redis解压目录下，编译安装，安装结束

```
cd /usr/local/redis/src

-- 直接编译安装
make install
```

5.**配置文件**

```
-- 修改redis.conf文件
vim /usr/local/redis/redis.conf

-- 启用守护进程方式运行
daemonize 修改为 yes

-- 绑定的主机地址，这样别的主机都可以访问此redis
bind 0.0.0.0

--修改密码，‘zhurs@123’这个是密码实体，根据自己需求修改
requirepass zhurs@123
```

6.**设置redis开机自启动**

```
-- 1.在/etc目录下新建redis目录
cd /etc
mkdir redis

-- 2.将/usr/local/redis/redis.conf文件复制一份到/etc/redis目录下，并命名为6379.conf
cp /usr/local/redis/redis.conf /etc/redis/6379.conf
-- 瞄一眼是否复制成功了
cd redis
ll

-- 3.将redis的启动脚本复制一份放到/etc/init.d目录下
cp /usr/local/redis/utils/redis_init_script /etc/init.d/redisd

-- 4.设置redis开机自启动, 先切换到/etc/init.d目录下, 然后执行自启命令
cd /etc/init.d
chkconfig redisd on

在/usr/local/redis/src/启动redis
./redis-server /etc/redis/6379.conf
```

## 测试性能

**redis-benchmark**是一个压力测试工具!

官方自带的性能测试工具

redis 性能测试工具可选参数如下所示：

| 序号 | 选项                   | 描述                                       | 默认值    |
| :--- | :--------------------- | :----------------------------------------- | :-------- |
| 1    | **-h**                 | 指定服务器主机名                           | 127.0.0.1 |
| 2    | **-p**                 | 指定服务器端口                             | 6379      |
| 3    | **-s**                 | 指定服务器 socket                          |           |
| 4    | **-c**                 | 指定并发连接数                             | 50        |
| 5    | **-n**                 | 指定请求数                                 | 10000     |
| 6    | **-d**                 | 以字节的形式指定 SET/GET 值的数据大小      | 2         |
| 7    | **-k**                 | 1=keep alive 0=reconnect                   | 1         |
| 8    | **-r**                 | SET/GET/INCR 使用随机 key, SADD 使用随机值 |           |
| 9    | **-P**                 | 通过管道传输 <numreq> 请求                 | 1         |
| 10   | **-q**                 | 强制退出 redis。仅显示 query/sec 值        |           |
| 11   | **--csv**              | 以 CSV 格式输出                            |           |
| 12   | **-l（L 的小写字母）** | 生成循环，永久执行测试                     |           |
| 13   | **-t**                 | 仅运行以逗号分隔的测试命令列表。           |           |
| 14   | **-I（i 的大写字母）** | Idle 模式。仅打开 N 个 idle 连接并等待。   |           |

```bash
#测试：100个并发连接 100000请求
redis-benchmark -h localhost -p 6379 -c 100 -n 100000


====== SET ======
  100000 requests completed in 0.87 seconds  #对我们的10万个请求进行写入测试
  100 parallel clients #100个并发客户端
  3 bytes payload #每次写入3个字节
  keep alive: 1 #只有1台服务器来处理这些请求，单机性能

94.14% <= 1 milliseconds #任务在1毫秒执行的进度为94.14%
98.16% <= 2 milliseconds
98.56% <= 3 milliseconds
99.16% <= 4 milliseconds
99.50% <= 5 milliseconds
99.66% <= 6 milliseconds
99.77% <= 7 milliseconds
99.90% <= 10 milliseconds
99.98% <= 11 milliseconds
100.00% <= 11 milliseconds #所有请求在11毫秒完成
114416.48 requests per second #每秒能处理114416.48次请求
```

## 基础的知识、命令

redis默认有16个数据库

**redis不区分大小写命令**

默认使用的是第0个

可以使用select进行切换

```bash
#登录验证密码
>auth password

>ping pang

#查询密码
>config get requirepass
#设置密码
>config set requirepass "redis123"

#切换数据库、查看当前数据库1db大小
>select 1
>1
#查看当前数据库所有的keys
>keys *
>"name"
#清空当前数据库数据
>flushdb
#清空所有数据库数据
>flushall
#查看某个key是否存在
>exists name
#移动key到另一个库
>move name 1 #移动name到数据库1
#删除某个key
>del name
#设置某个key的过期时间（秒）
>expire name 60
#查看某个key的剩余秒数
>ttl name
#查看某个key的类型
>type name
```

**<font color = red>redis是单线程的</font>**

> 明白Redis是很快的，官方表示，Redis是基于内存操作，CPU不是Redis的性能瓶颈，Redis的瓶颈是根据机器的**内存**和**网络带宽**，既然可以使用单线程来实现，就使用单线程了

**Redis为什么单线程还这么快？**

1、误区1：高性能的服务器一定是多线程的？

2、误区2：多线程（CPU上下文会切换！会耗时）一定比单线程效率高！

CPU>内存>硬盘 大概是100倍以上

<font color = red>**核心**</font>：redis是将所有数据全部放在内存中的，所以说使用单线程去操作效率就是最高的，多线程（CPU上下文会切换：耗时的操作！！！**1500-2000纳秒，1.5-2微秒**），对于内存系统来说，如果没有上下文切换效率就是最高的！

# 五大数据类型

Redis是一个开源（BSD许可），内存存储的数据结构服务器，可用作**数据库**，**高速缓存**和**消息队列**代理。它支持字符串、哈希表、列表、集合、有序集合，位图，hyperloglogs等数据类型。内置复制、Lua脚本、LRU收回、事务以及不同级别磁盘持久化功能，同时通过Redis Sentinel提供高可用，通过Redis Cluster提供自动分区。

## String（字符串、数字字符串）

```bash
#往字符串后追加字符串
#如果当前key不存在，则相当于set key（新增）
>append key1 "hell "
>"v1hell "
#查看字符串长度
>strlen key1
>(integer) 7
----------------------------------------
#自增，数值+1
>incr views
#自减，数值-1
>decr views
#自增，按步长数值增加(以下例子是每次+10)
>incrby views 10
#自减，按步长数值减少(以下例子是每次-10)
>decrby views 10
-----------------------------------------
#查看字符串某个范围的字串，闭区间(例：key1="hello,luowensa"),这个和java还不一样，java应该是0,5
>getrange key1 0 4
>"hello"
>getrange key1 6 -1 #-1代表截取到字符串最后
>"luowensa"
#将字符串key2从下标1开始的字符依次替换为xxx（key2="abcdefg"）
>setrange key2 1 xxx
>(integer) 7
>get key2
>"axxxefg"
------------------------------------------
常用于分布式锁
#设置key及key值，以及到期时间
>setex key3 30 "hello"
#如果不存在这个key，则赋值，否则就不生效
>setnx mykey "redis"
------------------------------------------
#批量设置key
>mset k1 v1 k2 v2
#批量获取key们的值
>mget k1 k2

常用于分布式锁
#如果不存在key，则批量设置key，是原子性操作（k1存在数据库，k2不存在数据库，则也设置失败）
>msetnx k1 v1 k4 v4
>(integer) 0
-------------------------------------------
#对象
set user:1 {name:zhangsan,age:3} # 设置一个user:1 对象 值为json字符来保存一个对象！
#这里的key是一个巧妙的设计：user:{id}:{filed},如此设计在redis中是完全可以的
> mset user:1:name zhangsan user:1:age 18
OK
> mget user:1:name user:1:age
1) "zhangsan"
2) "18"
-------------------------------------------
#先get，再set
127.0.0.1:6379> getset db redis #1.get db 2.set db redis
(nil)
127.0.0.1:6379> get db
"redis"
127.0.0.1:6379> getset db mongodb
"redis"
127.0.0.1:6379> get db
"mongodb"
```

> CAS: Compare and Swap 比较并交换 乐观锁

## List（列表，可栈可队）

基本数据类型，列表

又可以当栈，又可以当队列，**可视为读操作一定是从左至右**

![](https://img-blog.csdnimg.cn/ff3564c0d2f24925bbe610e60da4aaef.png)

**所有的list命令都是以l开头的**

```bash
#lpush 头插法（往左边扔）
127.0.0.1:6379> lpush list one
(integer) 1
127.0.0.1:6379> lpush list two
(integer) 2
127.0.0.1:6379> lpush list three
(integer) 3
127.0.0.1:6379> lrange list 0 -1
1) "three"
2) "two"
3) "one"
#rpush 尾插法（往右边扔）
127.0.0.1:6379> rpush list right
(integer) 4
127.0.0.1:6379> lrange list 0 -1
1) "three"
2) "two"
3) "one"
4) "right"
#
---------------------------------------
#lpop 左移除一个值
127.0.0.1:6379> lpop list
"three"
#rpop 右移除一个值
127.0.0.1:6379> rpop list
"right"
#lrange 查看列表指定范围的值
127.0.0.1:6379> lrange list 0 -1
1) "two"
2) "one"
#lindex列表根据下标获取元素，没有rindex
127.0.0.1:6379> lindex list 0
"two"
127.0.0.1:6379> lindex list -1 #-1 获取列表最后(右)的元素
"one"
-----------------------------------------
#llen 获取列表长度
127.0.0.1:6379> llen list
(integer) 2
#lrem移除列表指定数量的指定值元素
127.0.0.1:6379> lrange list 0 -1
1) "three"
2) "three"
3) "two"
4) "one"
127.0.0.1:6379> lrem list 3 one
(integer) 1
127.0.0.1:6379> lrange list 0 -1
1) "three"
2) "three"
3) "two"
------------------------------------------
trim 修剪 ： list 截断
#ltrim 通过下标范围截取指定的元素，这个list已经被改变了【闭区间】
127.0.0.1:6379> rpush mylist hello
(integer) 1
127.0.0.1:6379> rpush mylist hello1
(integer) 2
127.0.0.1:6379> rpush mylist hello2
(integer) 3
127.0.0.1:6379> rpush mylist hello3
(integer) 4
127.0.0.1:6379> ltrim mylist 1 2
OK
127.0.0.1:6379> lrange mylist 0 -1
1) "hello1"
2) "hello2"
--------------------------------------------
rpoplpush #右移除列表的最后一个元素，将他左移动放置到新的（别的（包括自己））列表中
127.0.0.1:6379> rpush mylist "hello" "hello1" "hello2"
(integer) 3
127.0.0.1:6379> lrange mylist 0 -1
1) "hello"
2) "hello1"
3) "hello2"
127.0.0.1:6379> rpoplpush mylist myotherlist
"hello2"
127.0.0.1:6379> lrange mylist 0 -1
1) "hello"
2) "hello1"
127.0.0.1:6379> lrange myotherlist 0 -1
1) "hello2"
----------------------------------------------
lset #列表中指定下标的值替换为另一个值，更新操作/如果该下标的值不存在，则更新失败
127.0.0.1:6379> lset list 0 item
(error) ERR no such key
127.0.0.1:6379> lpush list value1
(integer) 1
127.0.0.1:6379> lset list 0 item
OK
127.0.0.1:6379> lrange list 0 0
1) "item"
----------------------------------------------
linsert #往指定列表指定元素之前或之后插值
127.0.0.1:6379> lrange mylist 0 -1
1) "hello"
2) "world"
127.0.0.1:6379> linsert mylist before world other
(integer) 3
127.0.0.1:6379> lrange mylist 0 -1
1) "hello"
2) "other"
3) "world"
127.0.0.1:6379> linsert mylist after world !
(integer) 4
127.0.0.1:6379> lrange mylist 0 -1
1) "hello"
2) "other"
3) "world"
4) "!"
```

> 小结

- 他实际上是一个链表，before Node after；left，right都可以插入值
- 如果key不存在，创建新的链表
- 如果key存在，新增内容
- 如果移除了所有值，空链表，也代表不存在！
- **在两边插入或者改动值，效率最高！中间元素，相对来说效率会低一点**

**<font color=red>消息队列（lpush rpop）、栈（lpush，lpop）</font>**

## Set(无序集合，值不可重复)

**set 无序不重复集合。抽随机！**

```bash
#sadd 往set中添加值
127.0.0.1:6379> sadd myset "hello"
(integer) 1
127.0.0.1:6379> sadd myset "lws"
(integer) 1
#smembers 查看改set中的所有成员
127.0.0.1:6379> smembers myset
1) "hello"
2) "lws"
#sismember 判断该成员是否存在该set中
127.0.0.1:6379> sismember myset hello
(integer) 1
127.0.0.1:6379> sismember myset world
(integer) 0
#scard 获取当前set中的成员数量
127.0.0.1:6379> scard myset
(integer) 2
--------------------------------------------
#srem 移除set中的指定成员
127.0.0.1:6379> srem myset "hello"
(integer) 1
127.0.0.1:6379> smembers myset
1) "lws"
---------------------------------------------
#srandmember 随机抽选出set中指定个数的成员
127.0.0.1:6379> smembers myset
1) "lws2"
2) "lws1"
3) "lws"
127.0.0.1:6379> srandmember myset
"lws"
127.0.0.1:6379> srandmember myset
"lws1"
127.0.0.1:6379> srandmember myset
"lws1"
127.0.0.1:6379> srandmember myset 2
1) "lws1"
2) "lws"
---------------------------------------------
#spop 随机移除set中指定个数的成员
127.0.0.1:6379> smembers myset
1) "lws2"
2) "lws1"
3) "lws"
127.0.0.1:6379> spop myset
"lws1"
127.0.0.1:6379> smembers myset
1) "lws2"
2) "lws"
127.0.0.1:6379> spop myset 2
......
-----------------------------------------------
#smove 将一个set中的一个成员移动到另一个set中去
127.0.0.1:6379> smembers myset
1) "lws2"
2) "lws"
127.0.0.1:6379> smembers myset2
1) "set2"
127.0.0.1:6379> smove myset myset2 lws
(integer) 1
127.0.0.1:6379> smembers myset2
1) "set2"
2) "lws"
-----------------------------------------------
微博，B站，共同关注！（并集）
127.0.0.1:6379> sadd set1 a b c
(integer) 3
127.0.0.1:6379> sadd set2 c d e
(integer) 3
#sdiff 以第一个set为基底，取多个set的差集
127.0.0.1:6379> sdiff set1 set2
1) "b"
2) "a"
#sinter 取多个set的交集
127.0.0.1:6379> sinter set1 set2
1) "c"
#sunion 取多个set的并集
127.0.0.1:6379> sunion set1 set2
1) "b"
2) "c"
3) "a"
4) "d"
5) "e"
```

> 例子

微博，A用户将所有关注的人放在一个set中！将他的粉丝放在另一个set中！

<font color=red>**共同关注，共同爱好，推荐好友，互关可以用set的api实现**</font>（六度分割理论）

## Hash(哈希，key-map)

本质和String类型没有太大区别

```bash
#hset set一个具体的key的map-key和map-value
127.0.0.1:6379> hset myhash field1 lws
(integer) 1
#hget get一个具体的key的map-key和map-value
127.0.0.1:6379> hget myhash field1
"lws"
#hmset 批量set一个具体key的map-key和map-value
127.0.0.1:6379> hmset myhash field1 hello field2 world
(integer) 1
#hmget 批量get一个具体key的map-key和map-value
127.0.0.1:6379> hmget myhash field1 field2
1) "hello"
2) "world"
#hgetall 获取这个key所有的值
127.0.0.1:6379> hgetall myhash
1) "field1" #map-key
2) "hello"  #map-value
3) "field2" #map-key
4) "world"  #map-value
---------------------------------------------------------
#hdel 删除hash指定key的map-key和对应的map-value
127.0.0.1:6379> hgetall myhash
1) "field1"
2) "hello"
3) "field2"
4) "world"
127.0.0.1:6379> hdel myhash field1
(integer) 1
127.0.0.1:6379> hgetall myhash
1) "field2"
2) "world"
----------------------------------------------------------
#hlen 查看hash指定key的map键值对数量
127.0.0.1:6379> hgetall myhash
1) "field2"
2) "world"
3) "field1"
4) "hello"
127.0.0.1:6379> hlen myhash
(integer) 2
---------------------------------------------------------
#hexists 判断hash指定key的map-key存不存在
127.0.0.1:6379> hexists myhash field1
(integer) 1
127.0.0.1:6379> hexists myhash field5
(integer) 0
---------------------------------------------------------
#hkeys 获取hash指定key的所有map-key
127.0.0.1:6379> hkeys myhash
1) "field2"
2) "field1"
#hvals 获取hash指定key的所有map-value
127.0.0.1:6379> hvals myhash
1) "world"
2) "hello"
-----------------------------------------------------------
#hincrby 将hash指定key的map-key的map-value按步长自增或自减
127.0.0.1:6379> hset myhash field3 5
(integer) 1
127.0.0.1:6379> hincrby myhash field3 1
(integer) 6
127.0.0.1:6379> hincrby myhash field3 -1
(integer) 5
------------------------------------------------------------
#hsetnx set一个具体的key的map-key和map-value；不存在这个map-key，则赋值，否则就不生效
127.0.0.1:6379> hsetnx myhash field4 hello
(integer) 1
127.0.0.1:6379> hsetnx myhash field4 world
(integer) 0
```

<font color=red>**hash更适合于对象的存储**</font>，hash可用于存放易变更的数据（用户信息）：user:1 name lws

## Zset(有序集合)

在set的基础上，增加了一个值，set k1 v1 --> zset k1 score1 v1

```bash
#zadd 往zset中添加有序值
127.0.0.1:6379> zadd myzet 1 one
(integer) 1
127.0.0.1:6379> zadd myzet 2 two 3 three
(integer) 2
#zrange 查看zset指定范围的值
127.0.0.1:6379> zrange myzet 0 -1
1) "one"
2) "two"
3) "three"
-------------------------------------------
排序如何实现
127.0.0.1:6379> zadd salary 2500 xiaohong
(integer) 1
127.0.0.1:6379> zadd salary 5000 zhangsan
(integer) 1
127.0.0.1:6379> zadd salary 500 lws
(integer) 1
#正序
#zrange
#zrangebyscore
127.0.0.1:6379> zrange salary 0 -1
1) "lws"
2) "xiaohong"
3) "zhangsan"
127.0.0.1:6379> zrangebyscore salary -inf +inf #(-∞,+∞)
1) "lws"
2) "xiaohong"
3) "zhangsan"
127.0.0.1:6379> zrangebyscore salary -inf +inf withscores #withscores：显示带成绩
1) "lws"
2) "500"
3) "xiaohong"
4) "2500"
5) "zhangsan"
6) "5000"
127.0.0.1:6379> zrangebyscore salary -inf 2500 withscores #(-∞,2500]，withscores：显示带成绩
1) "lws"
2) "500"
3) "xiaohong"
4) "2500"
#倒序
#zrevrange
#zrevrangebyscore
127.0.0.1:6379> zrevrange salary 0 -1
1) "zhangsan"
2) "xiaohong"
3) "lws"
127.0.0.1:6379> zrevrangebyscore salary +inf -inf #(-∞,+∞)
1) "zhangsan"
2) "xiaohong"
3) "lws"
127.0.0.1:6379> zrevrangebyscore salary +inf 2500 withscores #[2500,+∞),withscores：显示带成绩
1) "zhangsan"
2) "5000"
3) "xiaohong"
4) "2500"
----------------------------------------------------
#zrem 移除zset中的指定成员
127.0.0.1:6379> zrange salary 0 -1
1) "lws"
2) "xiaohong"
3) "zhangsan"
127.0.0.1:6379> zrem salary xiaohong
(integer) 1
127.0.0.1:6379> zrange salary 0 -1
1) "lws"
2) "zhangsan"
---------------------------------------------------
#zcard 获取当前zset中的成员数量
127.0.0.1:6379> zcard salary
(integer) 2
---------------------------------------------------
#zcount 获取zset指定score闭区间内的有效成员数量
127.0.0.1:6379> zrangebyscore myzet -inf +inf withscores
1) "one"
2) "1"
3) "two"
4) "2"
5) "three"
6) "3"
127.0.0.1:6379> zcount myzet 1 3
(integer) 3
127.0.0.1:6379> zcount myzet 1 2
(integer) 2
```

<font color=red>**案例思路：**</font>**存储班级成绩表，工资表排序，排行榜，带权重进行判断(普通消息1/重要消息2)**

# 三种特殊数据类型

## geospatial地理位置

朋友的定位，附近的人，打车距离的计算

Redis3.2版本就推出了

这个功能可以推算地理位置的信息，两地之间的距离，方圆几里的人！

**只有6个命令**

- getadd
- geopos
- geodist
- georadius
- georadiusbymember
- geohash

```bash
# getadd 添加地理位置(key 经度 纬度 名称)
127.0.0.1:6379> geoadd china:city 106.50 29.53 chongqin
(integer) 1
127.0.0.1:6379> geoadd china:city 114.05 22.52 shengzhen 120.16 30.24 hangzhou 108.96 34.26 xian
(integer) 3
#geopos 获取当前定位 由于Redis在存储位置信息时使用字符串，而获取时会将其转换为浮点数，所以在进行比较时可能会存在精度不一致的问题
127.0.0.1:6379> geopos china:city beijing
1) 1) "116.39999896287918091"
   2) "39.90000009167092543"
#geodist 获取两个定位之间的距离
#单位：m，km,mi(英里~1.6km),ft(英尺)，默认m
127.0.0.1:6379> geodist china:city beijing shanghai
"1067378.7564"
127.0.0.1:6379> geodist china:city beijing shanghai km
"1067.3788"
#georadius 以给定的经纬度为中心，找出某一半径内的元素
127.0.0.1:6379> georadius china:city 110 30 1000 km
1) "chongqi"
2) "xian"
3) "shengzhen"
4) "hangzhou"
127.0.0.1:6379> georadius china:city 110 30 500 km
1) "chongqi"
2) "xian"
127.0.0.1:6379> georadius china:city 110 30 500 km withdist #并展示距离
1) 1) "chongqi"
   2) "341.9374"
2) 1) "xian"
   2) "483.8340"
127.0.0.1:6379> georadius china:city 110 30 500 km withcoord #并展示查询出地址的经纬度
1) 1) "chongqi"
   2) 1) "106.49999767541885376"
      2) "29.52999957900659211"
2) 1) "xian"
   2) 1) "108.96000176668167114"
      2) "34.25999964418929977"
127.0.0.1:6379> georadius china:city 110 30 500 km withcoord count 1 #限制返回的数量
1) 1) "chongqi"
   2) 1) "106.49999767541885376"
      2) "29.52999957900659211"
#georadiusbymember 以给定的某元素为中心，找出某一半径内的元素
127.0.0.1:6379> georadiusbymember china:city shanghai 400 km withcoord withdist
1) 1) "hangzhou"
   2) "166.7613"
   3) 1) "120.1600000262260437"
      2) "30.2400003229490224"
2) 1) "shanghai"
   2) "0.0000"
   3) 1) "121.47000163793563843"
      2) "31.22999903975783553"
#geohash 返回一个或多个位置元素的Geohash表示 该命令将返回11个字符的geohash字符串
#将二维的经纬度转换为一维的字符串，如果两个字符串越接近相似，那么距离越近
127.0.0.1:6379> geohash china:city beijing shanghai
1) "wx4fbxxfke0"
2) "wtw3sj5zbj0"
```

**Geo底层实现原理其实就是Zset！我们可以用Zset命令来操作Geo**，就可以查看Geo所有元素，以及删除元素

## Hyperloglog基数统计

A{1,3,5,7,8,7}  B{1,3,5,7,8}

基数（子集元素的个数） = 5，<font color=red>会存在误差，需要可以接受误差</font>

Redis 2.8.9就更新了Hyperloglog数据结构

优点：占用的内存是固定，存2^64个不同的元素，只需要12KB内存，从内存角度考虑Hyperloglog应是首选

**网页的UV(一个人访问一个网站多次，但还是算作一个人！)**

传统的方式，set保存用户的id，然后就可以统计set中元素数量作为标准判断！

这个方式如果保存大量的用户id，就会比较麻烦！我们的目标是为了计数，而不是保存用户id

*0.81%错误率！统计UV任务，可以忽略不计的！*

```bash
#pfadd 添加元素
127.0.0.1:6379> pfadd mykey a b c d e f g h i j
(integer) 1
#pfcount 统计不重复元素的数量
127.0.0.1:6379> pfcount mykey
(integer) 10
127.0.0.1:6379> pfadd mykey2 i j z x c v b n m
(integer) 1
#pfmerge 创建交集
127.0.0.1:6379> pfmerge mykey3 mykey mykey2
OK
127.0.0.1:6379> pfcount mykey3
(integer) 15
```

底层string

## Bitmaps位存储

统计用户信息，活跃，不活跃！登录、未登录！打卡，未打卡！两个状态的，都可以使用Bitmaps

Bitmaps位图，数据结构！都是操作二进制位来进行记录，就只有0和1两个状态！

```bash
#使用bitmap来记录周一到周日的打卡！
#setbit 设置值，只能设置值为1或0
127.0.0.1:6379> setbit sign 0 1
(integer) 0
127.0.0.1:6379> setbit sign 1 0
(integer) 0
127.0.0.1:6379> setbit sign 2 0
(integer) 0
127.0.0.1:6379> setbit sign 3 1
(integer) 0
127.0.0.1:6379> setbit sign 4 1
(integer) 0
127.0.0.1:6379> setbit sign 5 0
(integer) 0
127.0.0.1:6379> setbit sign 6 0
(integer) 0
#getbit 查看值
127.0.0.1:6379> getbit sign 3
(integer) 1
127.0.0.1:6379> getbit sign 6
(integer) 0
#bitcount 统计所有或指定范围内的value为1的数量 指定范围【start end】单位是字节
127.0.0.1:6379> bitcount sign
(integer) 3
```

底层string

# 事务

Redis事务的本质：一组命令的集合！一个事务中的所有命令都会被序列化，在事务执行的过程中，会按照顺序执行！

一次性、顺序性、排他性！

执行一些列的命令

```bash
---------- 队列 set set set 执行 -----------
```

==Redis事务没有隔离级别的概念！==(没有幻读，脏读)

所有的命令在事务中，并没有直接被执行！只有发起执行命令的时候才会执行Exec

==Redis单条命令是保证原子性的，但是事务不保证原子性！==

redis的事务

1. 开启事务（multi）
2. 命令入队（）
3. 执行事务（exec）

```bash
127.0.0.1:6379> multi #开启事务
OK
127.0.0.1:6379> set k1 v1 #命令1入队
QUEUED
127.0.0.1:6379> set k2 v2 #命令2入队
QUEUED
127.0.0.1:6379> get k2 #命令3入队
QUEUED
127.0.0.1:6379> set k3 v3 #命令4入队
QUEUED
127.0.0.1:6379> exec #执行事务
1) OK
2) OK
3) "v2"
4) OK
```

==放弃事务==，事务队列中命令都不会被执行！

```bash
127.0.0.1:6379> multi
OK
127.0.0.1:6379> set k1 v1
QUEUED
127.0.0.1:6379> set k2 v2
QUEUED
127.0.0.1:6379> set k4 v4
QUEUED
127.0.0.1:6379> discard #放弃事务
OK
127.0.0.1:6379> get k4
(nil)
```

**编译型异常**（代码有问题，命令有错，编译没通过），事务中所有的命令都不会被执行！

```bash
127.0.0.1:6379> multi
OK
127.0.0.1:6379> set k1 v1
QUEUED
127.0.0.1:6379> set k2 v2
QUEUED
127.0.0.1:6379> set k3 v3
QUEUED
127.0.0.1:6379> getset k3 #错误的命令
(error) ERR wrong number of arguments for 'getset' command
127.0.0.1:6379> set k4 v4
QUEUED
127.0.0.1:6379> set k5 v5
QUEUED
127.0.0.1:6379> exec #执行事务报错
(error) EXECABORT Transaction discarded because of previous errors.
127.0.0.1:6379> get k5 #所有的命令都不会被执行！
(nil)
```

**运行时异常**（1/0），如果事务队列中存在语法性，那么<font color=red>执行命令的时候，其他命令是可以正常执行的，错误命令抛出异常</font>

```bash
127.0.0.1:6379> set k1 "v1"
OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379> incr k1 #执行的时候会失败
QUEUED
127.0.0.1:6379> set k2 v2
QUEUED
127.0.0.1:6379> set k3 v3
QUEUED
127.0.0.1:6379> get k3 #执行的时候会成功
QUEUED
127.0.0.1:6379> exec
1) (error) ERR value is not an integer or out of range
2) OK
3) OK
4) "v3"
```

> 监控！watch

**悲观锁：**

- 很悲观，认为什么时候都会出问题，无论做什么都会加锁！

**乐观锁：**

- 很乐观，认为什么时候都不会出现问题，所以不会上锁！更新数据的时候去判断一下，在此期间是否有人修改过这个数据，version！
- 1.获取version 2.更新的时候比较version

> Redis监视测试

1.正常执行成功！

```bash
127.0.0.1:6379> set money 100
OK
127.0.0.1:6379> set out 0
OK
127.0.0.1:6379> watch money # 监视money对象
OK
127.0.0.1:6379> multi #事务正常结束，数据期间没有发生变动，这个时候就正常执行成功
OK
127.0.0.1:6379> decrby money 20
QUEUED
127.0.0.1:6379> incrby out 20
QUEUED
127.0.0.1:6379> exec
1) (integer) 80
2) (integer) 20
```

2.测试多线程修改值，**使用watch可以当作redis的乐观锁操作**！

```bash
---线程1---
127.0.0.1:6379> watch money #监视money
OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379> decrby money 10
QUEUED
127.0.0.1:6379> incrby out 10
QUEUED
---线程2---
127.0.0.1:6379> get money
"80"
127.0.0.1:6379> set money 1000 #在线程1没exec之前，线程2改变了线程1watch的money值
OK
---线程1---
127.0.0.1:6379> exec #线程1执行失败！
(nil)
```

UNWATCH

取消 WATCH 命令对所有 key 的监视。如果在执行 WATCH 命令之后，EXEC 命令或DISCARD 命令先被执行了的话，那么就不需要再执行UNWATCH 了。

# Jedis

我们要使用java来操作Redis

> 什么是jedis是Redis官方推荐的java连接开发工具！使用java操作Redis中间件！如果你要使用java操作redis，那么一定要对jedis十分熟悉！

1、导入对应的依赖

```xml
<dependencies>
    <!--导入jedis的包-->
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
        <version>3.3.0</version>
    </dependency>
</dependencies>
```

2、编码测试

- 连接数据库

  ```java
  //1.new Jedis对象
  Jedis jedis = new Jedis("192.168.31.201",6379);
  //jedis所有的命令就是我们之前学习的所有指令！
  jedis.auth("redis123");
  
  System.out.println(jedis.ping());
  ------------------------------------
  PONG
  ```

- 操作命令

- 断开连接！

**Jedis事务体会**

```java
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
```

# SpringBoot集成Reids

SpringBoot操作数据：spring-data jpa jdbc mongodb redis！

SpringData也是和SpringBoot齐名的项目！

说明：在SpringBoot2.x之后，原来使用的jedis被替换为了lettuce？

jedis：采用的直连，多个线程操作的话，是不安全的，如果想要避免不安全的，使用jedis pool连接池！BIO（是阻塞的）

lettuce：采用netty（高性能异步请求框架，快），实例可以在多个线程中共享，不存在线程不安全的情况！可以减少线程数量了，更像NIO模式（多路复用）

**源码分析**：

```java
@AutoConfiguration
@ConditionalOnClass({RedisOperations.class})
@EnableConfigurationProperties({RedisProperties.class})
@Import({LettuceConnectionConfiguration.class, JedisConnectionConfiguration.class})
public class RedisAutoConfiguration {
    public RedisAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean({RedisConnectionDetails.class})
    PropertiesRedisConnectionDetails redisConnectionDetails(RedisProperties properties) {
        return new PropertiesRedisConnectionDetails(properties);
    }

    @Bean
    @ConditionalOnMissingBean( 
        name = {"redisTemplate"}
    )//这个注解的意思是：如果你自己写了redisTemplate名字的类，可以将这个Bean替换掉
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //默认的RedisTemplate没有过多的设置，redis的对象是需要序列化的！
        //两个泛型都是object，object的类型，我们后使用需要强制转换<String, Object>
        RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean //由于String是Redis中最常使用的类型，所以说单独提出来了一个bean
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
```

> 整合测试一下

1.导入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

2.配置连接

```properties
spring.redis.host=192.168.31.201
spring.redis.port=6379
spring.redis.password=redis123
```

3.测试！

```java
@SpringBootTest
class Redis02SpringbootApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

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

}
-------------------------------
redisTemplate.opsForValue().get("hero") = ironMan
```

**redis序列化**

```java
//在RedisTemplate类中
@Nullable
private RedisSerializer keySerializer = null;
@Nullable
private RedisSerializer valueSerializer = null;
@Nullable
private RedisSerializer hashKeySerializer = null;
@Nullable
private RedisSerializer hashValueSerializer = null;
private RedisSerializer<String> stringSerializer = RedisSerializer.string();



if (this.defaultSerializer == null) {
    //JDK序列化，会使字符串转义，我们可能会使用json来序列化
    this.defaultSerializer = new JdkSerializationRedisSerializer(this.classLoader != null ? this.classLoader : this.getClass().getClassLoader());
}

if (this.enableDefaultSerializer) {
    if (this.keySerializer == null) {
        this.keySerializer = this.defaultSerializer;
        defaultUsed = true;
    }

    if (this.valueSerializer == null) {
        this.valueSerializer = this.defaultSerializer;
        defaultUsed = true;
    }

    if (this.hashKeySerializer == null) {
        this.hashKeySerializer = this.defaultSerializer;
        defaultUsed = true;
    }

    if (this.hashValueSerializer == null) {
        this.hashValueSerializer = this.defaultSerializer;
        defaultUsed = true;
    }
}
```

直接往redis里传对象，会报错，redis的对象是需要序列化的！

```java
@Test
public void test() throws JsonProcessingException {
    // 真实的开发一般都使用json来传递对象
    User user = new User("罗文洒", 3);
    //String jsonUser = new ObjectMapper().writeValueAsString(user);
    redisTemplate.opsForValue().set("user", user);
    System.out.println(redisTemplate.opsForValue().get("user"));
}
---------------------------------------
org.springframework.data.redis.serializer.SerializationException: Cannot serialize; nested exception is org.springframework.core.serializer.support.SerializationFailedException: Failed to serialize object using DefaultSerializer; nested exception is java.lang.IllegalArgumentException: DefaultSerializer requires a Serializable payload but received an object of type [com.luo.pojo.User]
//让对象实现序列化接口，就可以传输
public class User implements Serializable {

    private String name;

    private int age;
}
```

```bash
#用自带的jdk序列化的redisTemplate
>keys *user
>\xac\xed\x00\x05t\x00\x04user
#用自己写的序列化的redisTemplate
>keys *user
>user
```

最后又会被封装成RedisUtil，被程序员大肆使用

# Redis.conf详解

```bash
#网络 
#只能本地访问 改为0.0.0.0，哪里都能访问 bind 0.0.0.0
bind 127.0.0.1
#保护模式，一般都开启，比较安全 
#如果设置bind 0.0.0.0，要设置protected-mode no
protected-mode yes

#是否以守护进程开启，一般将改为daemonize yes
daemonize no
#如果以后台的方式进行，我们就需要指定一个pid文件！
pidfile /var/run/redis_6379.pid

#日志
# Specify the server verbosity level.
# This can be one of:
# debug (a lot of information, useful for development/testing)
# verbose (many rarely useful info, but not a mess like the debug level)
# notice (moderately verbose, what you want in production probably) #生产环境
# warning (only very important / critical messages are logged)
loglevel notice
logfile "" #日志的文件位置名

#快照
#持久化，在规定的时间内，执行了多少次操作，则会持久化到文件.rdb.aof
#redis是内存数据库，如果没有持久化，那么数据断电即失！
save 900 1 #如果900秒(15min)之后，至少有一个key进行了修改，我们就进行持久化操作
save 300 10
save 60 10000
#持久化出错了redis是否还继续写操作
stop-writes-on-bgsave-error yes
#是否压缩rdb文件，需要消耗一些cpu资源！
rdbcompression yes
#保存rdb文件的时候，进行错误的检查校验
rdbchecksum yes
#rdb文件保存的目录，默认当前目录
dir ./

#安全
#设置密码，默认是注释掉的
requirepass redis123

#客户端
maxclients 10000 #最多能连接多少客户端

#redis内存的容量
maxmemory <bytes>
#redis内存达到上限之后的处理策略
maxmemory-policy noeviction
1.noeviction(默认策略)：对于写请求不再提供服务，直接返回错误（DEL请求和部分特殊请求除外）
2.allkeys-lru：从所有key中使用LRU算法进行淘汰（LRU算法：即最近最少使用算法）
3.volatile-lru：从设置了过期时间的key中使用LRU算法进行淘汰
4.allkeys-random：从所有key中随机淘汰数据
5.volatile-random：从设置了过期时间的key中随机淘汰
6.volatile-ttl：在设置了过期时间的key中，淘汰过期时间剩余最短的

#APPEND ONLY MODE aof配置
#默认是不开启aof的，默认是使用rdb模式的持久化的，在大部分情况下，rdb完全够用了！
appendonly no 
#aof持久化文件的名称
appendfilename "appendonly.aof"

# appendfsync always #每次修改都会同步，消耗性能
appendfsync everysec #每秒同步一次，可能会丢失这一秒的数据
# appendfsync no #主动不同步，这个时候操作系统自己同步数据，性能最好！
```

# Redis持久化

Redis是内存数据库，如果不能将内存中的数据库状态保存到磁盘，那么一旦服务器进程退出，服务器上的数据库状态也会消失，所以Redis提供了持久化功能！

## RDB（Redis DataBase）

在主从复制中，rdb就是备用的，从机上面

![](https://img2022.cnblogs.com/blog/2333762/202210/2333762-20221024155638589-155138472.png)

在指定的时间间隔内将内存中的数据集快照写入磁盘，也就是行话讲的Snapshot快照，它恢复时是将快照文件直接读到内存里。

Redis会单独创建（fork）一个子进程来进行持久化，会先将数据写入到一个临时文件中，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件。整个过程中，主进程是不进行任何IO操作的。这就确保了极高的性能。如果需要进行大规模数据的恢复，且对于数据恢复的完整性不是非常敏感，那RDB方式要比AOF方式更加的高效。**RDB的缺点**是最后一次持久化后的数据可能丢失。**我们默认的就是RDB**，一般情况下不需要修改这个配置！

RDB保存的文件是dump.rdb，可以在配置文件中进行配置

有时候在生产环境我们会将这个文件进行备份！

```bash
# The filename where to dump the DB
dbfilename dump.rdb
```

> 触发机制

1.在配置文件Redis.conf中save的规则满足的情况下，会自动触发rdb规则

2.执行flushall命令，也会触发我的的rdb规则

3.退出redis，也会产生rdb文件

> 如何恢复rdb文件

1.只需要将rdb文件放在我们redis启动目录就可以了，redis启动的时候会自动检查dump.rdb恢复其中的数据！

2.查看需要存在的位置

```bash
>config get dir
>"dir"
>"/" #如果在这个目录下存在dump.rdb文件，启动就会自动恢复其中的数据
```

**优点：**
1、适合大规模的数据恢复！

2、对数据的完整性要不高！

**缺点：**

1、需要一定的时间间隔进程操作！如果redis意外宕机了，这个最后一次修改数据就没有了！

2、fork进程的时候，会占用一定的内容空间！！

3、<font color=red>如果没有设置配置文件中rdb的生成目录，那么启动redis-server时候你所在的目录，就是读取或追加后续dump.rdb的文件目录</font>

## AOF（Append Only File）

**将我们所有命令记录下来，history，恢复的时候就把这个文件全部再执行一遍！**

![](https://img2022.cnblogs.com/blog/2333762/202210/2333762-20221024155738804-1998543024.png)

以日志的形式来记录每个写操作，将Redis执行过的所有指令记录下来（读操作不记录），只许追加文件但不可以改写文件，redis启动之初会读取该文件重新构建数据，换言之，redis重启的话就根据日志文件的内容将写指令从前到后执行一次以完成数据的恢复工作。**海量数据的时候读aof恢复的速度会比较慢**

<font color=red>当Redis同时开启这两种持久化方式时，‌它会优先使用AOF文件来恢复数据集</font>

**aof保存的是appendonly.aof文件**

```bash
#默认是不开启的，要将no改成yes开启
appendonly no 
```

重启redis就会读取或生成aof文件

**如果这个aof文件有错位，这时候redis是启动不起来的，我们需要修复这个aof文件**

redis给我们提供了一个工具 ==redis-check-aof==

```bash
>redis-check-aof --fix appendonly.aof
```
> 重写规则说明

aof 默认就是文件的无限追加，文件会越来越大！

如果 aof 文件大于 64m，太大了！ fork一个新的进程来将我们的文件进行重写！Redis可以在后台进行AOF重写（rewrite），通过压缩和去除冗余命令来减小文件的大小。最后新的aof文件替代旧的aof文件

```bash
no-appendfsync-on-rewrite no

# Automatic rewrite of the append only file.
# Redis is able to automatically rewrite the log file implicitly calling
# BGREWRITEAOF when the AOF log size grows by the specified percentage.
#
# This is how it works: Redis remembers the size of the AOF file after the
# latest rewrite (if no rewrite has happened since the restart, the size of
# the AOF at startup is used).
#
# This base size is compared to the current size. If the current size is
# bigger than the specified percentage, the rewrite is triggered. Also
# you need to specify a minimal size for the AOF file to be rewritten, this
# is useful to avoid rewriting the AOF file even if the percentage increase
# is reached but it is still pretty small.
#
# Specify a percentage of zero in order to disable the automatic AOF
# rewrite feature.

auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb #aof文件到64MB，就开始重写
```




```bash
# appendfsync always # 每次修改都会 sync。消耗性能
appendfsync everysec # 每秒执行一次 sync，可能会丢失这1s的数据！
# appendfsync no # 不执行 sync，这个时候操作系统自己同步数据，速度最快！
```

**优点(同步策略)：**
1、每一次修改都同步，文件的完整会更加好！

2、每秒同步一次，可能会丢失一秒的数据

3、从不同步，效率最高的！

**缺点：**

1、相对于数据文件来说，aof远远大于 rdb，修复的速度也比 rdb慢！

2、Aof 运行效率也要比 rdb 慢，所以我们redis默认的配置就是rdb持久化！

## 扩展

1、RDB 持久化方式能够在指定的时间间隔内对你的数据进行快照存储

2、AOF 持久化方式记录每次对服务器写的操作，当服务器重启的时候会重新执行这些命令来恢复原始的数据，AOF命令以Redis 协议追加保存每次写的操作到文件末尾，Redis还能对AOF文件进行后台重写，使得AOF文件的体积不至于过大。

3、**只做缓存，如果你只希望你的数据在服务器运行的时候存在，你也可以不使用任何持久化**

4、同时开启两种持久化方式

在这种情况下，当redis重启的时候会**优先载入AOF文件来恢复原始的数据**，因为在通常情况下AOF文件保存的数据集要比RDB文件保存的数据集要完整。
RDB 的数据不实时，同时使用两者时服务器重启也只会找AOF文件，那要不要只使用AOF呢？作者建议不要，因为RDB更适合用于备份数据库（AOF在不断变化不好备份），快速重启，而且不会有AOF可能潜在的Bug，留着作为一个万一的手段。
5、性能建议

因为RDB文件只用作后备用途，建议只在Slave上持久化RDB文件，而且只要15分钟备份一次就够了，只保留 save 900 1 这条规则。
如果Enable AOF ，好处是在最恶劣情况下也只会丢失不超过两秒数据，启动脚本较简单只load自己的AOF文件就可以了，代价一是带来了持续的IO，二是AOF rewrite 的最后将 rewrite 过程中产生的新数据写到新文件造成的阻塞几乎是不可避免的。只要硬盘许可，应该尽量减少AOF rewrite的频率，AOF重写的基础大小默认值64M太小了，可以设到5G以上，默认超过原大小100%大小重
写可以改到适当的数值。
如果不Enable AOF ，仅靠 Master-Slave Repllcation 实现高可用性也可以，能省掉一大笔IO，也减少了rewrite时带来的系统波动。代价是如果Master/Slave 同时倒掉，会丢失十几分钟的数据，启动脚本也要比较两个 Master/Slave 中的 RDB文件，载入较新的那个，微博就是这种架构。

# Redis发布订阅

Redis发布订阅(pub/sub)是一种**消息通信模式**：发送者(pub)发送消息，订阅者(sub)接收消息。微信、微博、关注系统

Redis客户端可以订阅任意数量的频道。

订阅/发布消息图：

第一个：消息发送者，第二个：频道，第三个：消息订阅者

![](https://img2022.cnblogs.com/blog/2333762/202210/2333762-20221024155639069-589001979.png)

下图展示了频道channel1，以及订阅这个频道的三个客户端--client2、client5、client1之间的关系：

![](https://img2022.cnblogs.com/blog/2333762/202210/2333762-20221024155639209-930488226.png)

当有新消息通过PUBLISH命令发送给频道chanel1时，这个消息就会被发送给订阅它的三个客户端：

![](https://img2022.cnblogs.com/blog/2333762/202210/2333762-20221024155639191-255172852.png)

> 命令

这些命令被广泛用于构建通信应用，比如网络聊天室(chatroom)和实时广播、实时提醒等

```bash
下表列出了 redis 发布订阅常用命令：

序号	命令及描述
1	PSUBSCRIBE pattern [pattern ...]
订阅一个或多个符合给定模式的频道。

2	PUBSUB subcommand [argument [argument ...]]
查看订阅与发布系统状态。

3	PUBLISH channel message #常用
将信息发送到指定的频道。

4	PUNSUBSCRIBE [pattern [pattern ...]]
退订所有给定模式的频道。

5	SUBSCRIBE channel [channel ...] #常用
订阅给定的一个或多个频道的信息。

6	UNSUBSCRIBE [channel [channel ...]]
指退订给定的频道。
```

> 测试

```bash
#发消息
127.0.0.1:6379> PUBLISH luowensa "hello,lws"
(integer) 1
127.0.0.1:6379> PUBLISH luowensa "hello,redis"
(integer) 1
#收消息
127.0.0.1:6379> SUBSCRIBE luowensa
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "luowensa"
3) (integer) 1

1) "message" #消息头
2) "luowensa" #频道
3) "hello,lws" #消息内容

1) "message"
2) "luowensa"
3) "hello,redis"

```

Redis是使用C实现的，通过分析 Redis 源码里的 pubsub.c 文件，了解发布和订阅机制的底层实现，籍此加深对 Redis 的理解。

Redis 通过 PUBLISH 、SUBSCRIBE 和 PSUBSCRIBE 等命令实现发布和订阅功能。

微信：

通过 SUBSCRIBE 命令订阅某频道后，**redis-server 里维护了一个字典，字典的键就是一个个 频道！，而字典的值则是一个链表**，链表中保存了所有订阅这个 channel 的客户端。SUBSCRIBE 命令的关键，就是将客户端添加到给定 channel 的订阅链表中。

![](https://img2022.cnblogs.com/blog/2333762/202210/2333762-20221024155738991-990249189.png)

通过 PUBLISH 命令向订阅者发送消息，redis-server 会使用给定的频道作为键，在它所维护的 channel字典中查找记录了订阅这个频道的所有客户端的链表，遍历这个链表，将消息发布给所有订阅者。

Pub/Sub 从字面上理解就是发布（Publish）与订阅（Subscribe），在Redis中，你可以设定对某一个key值进行消息发布及消息订阅，当一个key值上进行了消息发布后，所有订阅它的客户端都会收到相应的消息。这一功能最明显的用法就是用作实时消息系统，比如普通的即时聊天，群聊等功能。

使用场景：
1、实时消息系统！
2、事实聊天！（频道当做聊天室，将信息回显给所有人即可！）
3、订阅，关注系统都是可以的！

稍微复杂的场景我们就会使用 消息中间件 MQ

# Redis主从复制

## 概念

主从复制，是指将一台Redis服务器的数据，复制到其他的Redis服务器。前者称为主节点(master/leader)，后者称为从节点(slave/follower)；==数据的复制是单向的，只能由主节点到从节点。==**master以写为主，slave以读为主。**

**默认情况下，每台Redis服务器都是主节点；**且一个主节点可以有多个从节点(或没有从节点)，但一个从节点只能有一个主节点。

![](https://img2022.cnblogs.com/blog/2333762/202210/2333762-20221024155739237-1606262996.png)

**主从复制，读写分离！80%的情况下都在进行读操作！减缓服务器压力！架构中经常使用！**至少1主2从

**主从复制的作用主要包括：**

1、数据冗余：主从复制实现了数据的热备份，是持久化之外的一种数据冗余方式。

2、故障恢复：当主节点出现问题时，可以由从节点提供服务，实现快速的故障恢复；实际上是一种服务的冗余。

3、负载均衡：在主从复制的基础上，配合读写分离，可以由主节点提供写服务，由从节点提供读服务（即写Redis数据时应用连接主节点，读Redis数据时应用连接从节点），分担服务器负载；尤其是在写少读多的场景下，通过多个从节点分担读负载，可以大大提高Redis服务器的并发量。

4、**高可用（集群）**基石：除了上述作用以外，主从复制还是哨兵和集群能够实施的基础，因此说主从复制是Redis高可用的基础。

一般来说，要将Redis运用于工程项目中，只使用一台Redis是万万不能的（宕机），原因如下：

1、从结构上，单个Redis服务器会发生单点故障，并且一台服务器需要处理所有的请求负载，压力较大；

2、从容量上，单个Redis服务器内存容量有限，就算一台Redis服务器内存容量为256G，也不能将所有内存用作Redis存储内存，==一般来说，单台Redis最大使用内存不应该超过20G。==

## 环境配置

只配置从库，不配置主库！

```bash
127.0.0.1:6379> info replication #查看当前库的信息
# Replication
role:master #角色 master
connected_slaves:0 # 没有从机
master_replid:91f52a81fb001ab57e92b627a93cc8176adcfe7a
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:0
second_repl_offset:-1
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
```

复制3个配置文件，然后修改对应的信息

1、端口
2、pid 名字
3、log文件名字
4、dump.rdb 名字

修改完毕之后，启动我们的3个redis服务，可以通过进程信息查看！

## 一主二从

默认情况下，每台Redis服务器都是主节点； 我们一般情况下只用配置从机就好了！

认老大！ 一主 （79）二从（80，81）

```bash
127.0.0.1:6380> SLAVEOF 127.0.0.1 6379 # SLAVEOF host 6379 找谁当自己的老大！
OK
127.0.0.1:6380> info replication
# Replication
role:slave # 当前角色是从机
master_host:127.0.0.1 # 可以的看到主机的信息
master_port:6379
master_link_status:up
master_last_io_seconds_ago:3
master_sync_in_progress:0
slave_repl_offset:14
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:a81be8dd257636b2d3e7a9f595e69d73ff03774e
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:14
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:14
# 在主机中查看！
127.0.0.1:6379> info replication
# Replication
role:master
connected_slaves:1 # 多了从机的配置
slave0:ip=127.0.0.1,port=6380,state=online,offset=42,lag=1 # 多了从机的配置
master_replid:a81be8dd257636b2d3e7a9f595e69d73ff03774e
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:42
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:42
```

**<font color=red>真实的从主配置应该在配置文件中配置，这样的话是永久的，我们这里使用的是命令，暂时的！</font>**

==在配置文件中的replication信息中配置，比较规范，去配置==

测试：

- 主机断开连接，从机依旧能正常读。

- 过一会，主机如果回来了，从机依旧可以直接获取到主机新写入的信息！

**<font color=red>如果是使用命令行，来配置的主从，这个时候如果重启了，就会变回主机！只要变为从机，立马就会从主机中获取值！</font>**

> 复制的原理

Slave 启动成功连接到 master 后会发送一个sync同步命令

Master 接到命令，启动后台的存盘进程，同时收集所有接收到的用于修改数据集命令，在后台进程执行完毕之后，**master将传送整个数据文件到slave，并完成一次完全同步（全量复制）。**

**全量复制**：slave服务在接收到数据库文件数据后，将其存盘并加载到内存中。

**增量复制**：Master 继续将新的所有收集到的修改命令依次传给slave，完成同步

**<font color=red>但是只要是重新连接master，一次完全同步（全量复制）将被自动执行！ 我们的数据一定可以在从机中看到！</font>**

> 层层链路

![](https://img2022.cnblogs.com/blog/2333762/202210/2333762-20221024155739214-1658089539.png)

80，role:slave依旧只能读不能写

如果没有老大了，这个时候能不能选择一个老大出来呢？ 手动！

谋朝篡位

如果上图的主机（79）断开了连接，我们可以使用 

==slaveof no one== 让自己(80)变成主机！其他的节点就可以手动连接到最新的这个主节点（手动）！如果这个时候老大（79）修复了，那就重新配置从机！

```bash
slaveof no one
```

## 哨兵模式

主从切换技术的方法是∶当主服务器宕机后，需要手动把一台从服务器切换为主服务器，这就需要人工干预，费事费力，还会造成一段时间内服务不可用。这不是一种推荐的方式，更多时候，我们优先考虑哨兵模式。Redis从2.8开始正式提供了Sentinel (哨兵）架构来解决这个问题。


谋朝篡位的自动版，能够后台监控主机是否故障，如果故障了根据投票数==自动将从库转换为主库==。


哨兵模式是一种特殊的模式，首先Redis提供了哨兵的命令，哨兵是一个独立的进程，作为进程，它会独立运行。其原理是哨兵通过发送命令，等待Redis服务器响应，从而监控运行的多个Redis实例。

![](https://i-blog.csdnimg.cn/blog_migrate/24ca9767c7484577e81657817ed402e4.png)

然而一个哨兵进程对Redis服务器进行监控，可能会出现问题，为此，我们可以使用多个哨兵进行监控。各个哨兵之间还会进行监控，这样就形成了**多哨兵模式**。==最少3个哨兵，或大于3的奇数个哨兵组成集群==

![](https://i-blog.csdnimg.cn/blog_migrate/8f0eaf76b4429ab4621b2a2649c427a4.png)

假设主服务器宕机，哨兵1先检测到这个结果，系统并不会马上进行failover（重新选举）过程，仅仅是哨兵1主观的认为主服务器不可用，这个现象成为**主观下线**。当后面的哨兵也检测到主服务器不可用，并且数量达到一定值时（都认为之前的主节点宕机了），那么哨兵之间就会进行一次投票（投票算法给剩下的从节点投，一个哨兵只能投一次），投票的结果（得票最多的从节点）由一个哨兵发起，进行**failover[故障转移]**（重新选举为主节点的）操作。切换成功后，就会通过发布订阅模式，让各个哨兵把自己监控的从服务器实现切换主机，这个过程称为**客观下线**。



1.编写哨兵配置文件sentinel.conf

```bash
[root@192 helloconfig]vim sentinel.conf #新建一个哨兵文件
-----------------------------------------
# 哨兵sentinel监控的redis主节点的 ip port 
# master-name  可以自己命名的主节点名字 只能由字母A-z、数字0-9 、这三个字符".-_"组成。
# quorum 当这些quorum个数sentinel哨兵认为master主节点失联 那么这时 客观上认为主节点失联了
# sentinel monitor <master-name> <ip> <redis-port> <quorum>
sentinel monitor myredis 192.168.178.132 6379 1
-----------------------------------------
```

2.按配置文件启动哨兵

```bash
[root@192 bin] redis-sentinel helloconfig/sentinel.conf
```

如果被哨兵监控的6379redis Master宕机了，哨兵就会过一会自动从两个从机中选择一个作为主机（里面有一个投票算法）

```bash
51558:X 07 May 2023 04:55:32.025 # +sdown master myredis 192.168.178.132 6379

51558:X 07 May 2023 04:55:32.025 # +odown master myredis 192.168.178.132 6379 #quorum 1/1
51558:X 07 May 2023 04:55:32.025 # +new-epoch 1
51558:X 07 May 2023 04:55:32.025 # +try-failover master myredis 192.168.178.132 6379
51558:X 07 May 2023 04:55:32.026 # +vote-for-leader 68965939737c110af76e33bba61289364c3c8ac2 1
51558:X 07 May 2023 04:55:32.026 # +elected-leader master myredis 192.168.178.132 6379
51558:X 07 May 2023 04:55:32.026 # +failover-state-select-slave master myredis 192.168.178.132 6379    #确认6379崩了
51558:X 07 May 2023 04:55:32.103 # +selected-slave slave 192.168.178.132:6380 192.168.178.132 6380 @ myredis 192.168.178.132 6379
51558:X 07 May 2023 04:55:32.103 * +failover-state-send-slaveof-noone slave 192.168.178.132:6380 192.168.178.132 6380 @ myredis 192.168.178.132 6379
51558:X 07 May 2023 04:55:32.189 * +failover-state-wait-promotion slave 192.168.178.132:6380 192.168.178.132 6380 @ myredis 192.168.178.132 6379
51558:X 07 May 2023 04:55:33.015 # +promoted-slave slave 192.168.178.132:6380 192.168.178.132 6380 @ myredis 192.168.178.132 6379
51558:X 07 May 2023 04:55:33.015 # +failover-state-reconf-slaves master myredis 192.168.178.132 6379
51558:X 07 May 2023 04:55:33.074 * +slave-reconf-sent slave 192.168.178.132:6381 192.168.178.132 6381 @ myredis 192.168.178.132 6379
51558:X 07 May 2023 04:55:34.078 * +slave-reconf-inprog slave 192.168.178.132:6381 192.168.178.132 6381 @ myredis 192.168.178.132 6379
51558:X 07 May 2023 04:55:34.078 * +slave-reconf-done slave 192.168.178.132:6381 192.168.178.132 6381 @ myredis 192.168.178.132 6379

#产生了新主机Master 6380
51558:X 07 May 2023 04:55:34.141 # +failover-end master myredis 192.168.178.132 6379
51558:X 07 May 2023 04:55:34.141 # +switch-master myredis 192.168.178.132 6379 192.168.178.132 6380
51558:X 07 May 2023 04:55:34.141 * +slave slave 192.168.178.132:6381 192.168.178.132 6381 @ myredis 192.168.178.132 6380
51558:X 07 May 2023 04:55:34.141 * +slave slave 192.168.178.132:6379 192.168.178.132 6379 @ myredis 192.168.178.132 6380
51558:X 07 May 2023 04:56:04.186 # +sdown slave 192.168.178.132:6379 192.168.178.132 6379 @ myredis 192.168.178.132 6380
```

如果主机6379此时回来了，**但哨兵故障转移后已经制定了新的主从关系**，就只能归并到新的从机下，当做从机，这就是哨兵模式的规则！

**优点∶**
1、哨兵集群，基于主从复制模式，所有的主从配置优点，它全有

2、主从可以切换，故障可以转移，系统的可用性就会更好

3、哨兵模式就是主从模式的升级，手动到自动，更加健壮!
**缺点∶**
1、Redis不好在线扩容的，集群容量一旦到达上限，水平扩容就十分麻烦（如果现在有10台Redis服务器都满了，再加1台，因为配置都是写死的，那么就需要改很多的配置文件）!

2、实现哨兵模式的配置其实是很麻烦的，需要配置多个哨兵实例来监控和管理主节点，配置相对复杂。

> 哨兵模式的全部配置

```bash
# Example sentinel.conf
 
# 哨兵sentinel实例运行的端口 默认26379，如果有哨兵集群，我们还需要配置每个哨兵的端口
port 26379
 
# 哨兵sentinel的工作目录
dir /tmp
 
# 哨兵sentinel监控的redis主节点的 ip port 
# master-name  可以自己命名的主节点名字 只能由字母A-z、数字0-9 、这三个字符".-_"组成。
# quorum 当这些quorum个数sentinel哨兵认为master主节点失联 那么这时 客观上认为主节点失联了
# sentinel monitor <master-name> <ip> <redis-port> <quorum>
sentinel monitor mymaster 127.0.0.1 6379 1
 
# 当在Redis实例中开启了requirepass foobared 授权密码 这样所有连接Redis实例的客户端都要提供密码
# 设置哨兵sentinel 连接主从的密码 注意必须为主从设置一样的验证密码
# sentinel auth-pass <master-name> <password>
sentinel auth-pass mymaster MySUPER--secret-0123passw0rd
 
 
# 指定多少毫秒之后 主节点没有应答哨兵sentinel 此时 哨兵主观上认为主节点下线 默认30秒
# sentinel down-after-milliseconds <master-name> <milliseconds>
sentinel down-after-milliseconds mymaster 30000
 
# 这个配置项指定了在发生failover主备切换时最多可以有多少个slave同时对新的master进行 同步，
这个数字越小，完成failover所需的时间就越长，
但是如果这个数字越大，就意味着越 多的slave因为replication而不可用。
可以通过将这个值设为 1 来保证每次只有一个slave 处于不能处理命令请求的状态。
# sentinel parallel-syncs <master-name> <numslaves>
sentinel parallel-syncs mymaster 1
 
 
 
# 故障转移的超时时间 failover-timeout 可以用在以下这些方面： 
#1. 同一个sentinel对同一个master两次failover之间的间隔时间。
#2. 当一个slave从一个错误的master那里同步数据开始计算时间。直到slave被纠正为向正确的master那里同步数据时。
#3.当想要取消一个正在进行的failover所需要的时间。  
#4.当进行failover时，配置所有slaves指向新的master所需的最大时间。不过，即使过了这个超时，slaves依然会被正确配置为指向master，但是就不按parallel-syncs所配置的规则来了
# 默认三分钟
# sentinel failover-timeout <master-name> <milliseconds>
sentinel failover-timeout mymaster 180000
 
# SCRIPTS EXECUTION
 
#配置当某一事件发生时所需要执行的脚本，可以通过脚本来通知管理员，例如当系统运行不正常时发邮件通知相关人员。
#对于脚本的运行结果有以下规则：
#若脚本执行后返回1，那么该脚本稍后将会被再次执行，重复次数目前默认为10
#若脚本执行后返回2，或者比2更高的一个返回值，脚本将不会重复执行。
#如果脚本在执行过程中由于收到系统中断信号被终止了，则同返回值为1时的行为相同。
#一个脚本的最大执行时间为60s，如果超过这个时间，脚本将会被一个SIGKILL信号终止，之后重新执行。
 
#通知型脚本:当sentinel有任何警告级别的事件发生时（比如说redis实例的主观失效和客观失效等等），将会去调用这个脚本，
#这时这个脚本应该通过邮件，SMS等方式去通知系统管理员关于系统不正常运行的信息。调用该脚本时，将传给脚本两个参数，
#一个是事件的类型，
#一个是事件的描述。
#如果sentinel.conf配置文件中配置了这个脚本路径，那么必须保证这个脚本存在于这个路径，并且是可执行的，否则sentinel无法正常启动成功。
#通知脚本
# shell编程
# sentinel notification-script <master-name> <script-path>
  sentinel notification-script mymaster /var/redis/notify.sh
 
# 客户端重新配置主节点参数脚本
# 当一个master由于failover而发生改变时，这个脚本将会被调用，通知相关的客户端关于master地址已经发生改变的信息。
# 以下参数将会在调用脚本时传给脚本:
# <master-name> <role> <state> <from-ip> <from-port> <to-ip> <to-port>
# 目前<state>总是“failover”,
# <role>是“leader”或者“observer”中的一个。 
# 参数 from-ip, from-port, to-ip, to-port是用来和旧的master和新的master(即旧的slave)通信的
# 这个脚本应该是通用的，能被多次调用，不是针对性的。
# sentinel client-reconfig-script <master-name> <script-path>
sentinel client-reconfig-script mymaster /var/redis/reconfig.sh   #一般有运维人员配置
```

# Redis缓存穿透和雪崩

**<font color=red>服务器的高可用问题</font>**

## 缓存穿透（redis查不到，一直去查mysql）

> 概念

缓存穿透的概念很简单，用户想要查询一个数据，发现redis内存数据库没有，也就是缓存没有命中，于是向持久层数据库查询。发现也没有，于是本次查询失败。**当用户很多的时候，缓存都没有命中(秒杀!），于是都去请求了持久层数据库。这会给持久层数据库造成很大的压力**，这时候就相当于出现了缓存穿透。

> 解决方案

### 1.布隆过滤器

布隆过滤器是一种数据结构，对所有可能查询的参数以hash形式存储，在控制层先进行校验，不符合则丢弃，从而避免了对底层存储系统的查询压力;

![](https://i-blog.csdnimg.cn/blog_migrate/b0cb6d5d1fcaff6817c37a860bcd10dc.png)

**spring使用布隆过滤器**

#### 步骤 1: 添加依赖

```xml
<!-- 添加Guava的BloomFilter依赖 -->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>30.1-jre</version>
</dependency>
```

#### 步骤 2: 配置布隆过滤器

```yaml
# 布隆过滤器配置
bloom-filter:
  expected-insertions: 1000000  # 期望插入的元素数量
  fpp: 0.01  # 误判率
```

#### 步骤 3: 创建布隆过滤器 Bean

```java
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BloomFilterConfig {

    @Value("${bloom-filter.expected-insertions}")
    private int expectedInsertions;

    @Value("${bloom-filter.fpp}")
    private double falsePositiveProbability;

    @Bean
    public BloomFilter<String> bloomFilter() {
        return BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), expectedInsertions, falsePositiveProbability);
    }
}
```

#### 步骤 4: 使用布隆过滤器

```java
package fun.bo.controller;

import com.google.common.hash.BloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/bloom-filter")
public class BloomFilterController {

    @Resource
    private BloomFilter<String> bloomFilter;

    @GetMapping("/add/{value}")
    public String addToBloomFilter(@PathVariable String value) {
        bloomFilter.put(value);
        return "Added to Bloom Filter: " + value;
    }

    @GetMapping("/contains/{value}")
    public String checkBloomFilter(@PathVariable String value) {
        boolean contains = bloomFilter.mightContain(value);
        return "Bloom Filter contains " + value + ": " + contains;
    }
}
```

### 2、缓存空对象

当存储层不命中后，即使返回的空对象也将其缓存起来，同时会设置一个过期时间，之后再访问这个数据将会从缓存中获取，保护了后端数据源；

![](https://i-blog.csdnimg.cn/blog_migrate/44f94d370cadb3eeac2ecaaa25f55c5a.png)

但是这种缓存空对象方法会存在**两个问题**︰
1、如果空值能够被缓存起来，这就意味着缓存需要更多的空间存储更多的键，因为这当中可能会有很多的空值的键;
2、即使对空值设置了过期时间，还是会存在缓存层和存储层的数据会有一段时间窗口的不一致，这对于需要保持一致性的业务会有影响。

## 缓存击穿（并发量太大，某时刻缓存过期！redis查不到，一瞬间大量去查mysql）

> 概念

这里需要注意和缓存击穿的区别，缓存击穿，是指一个key非常热点，在不停的扛着大并发，大并发集中对这一个点进行访问，**当这个key在失效的瞬间，持续的大并发就穿破 缓存，直接请求数据库，就像在一个屏障上凿开了一个洞。**
当某个key在过期的瞬间，有大量的请求并发访问，这类数据一般是热点数据，由于缓存过期，会同时访问数据库来查询最新数据，并且回写缓存，会导使数据库瞬间压力过大。

> 解决方案

#### 1、设置热点数据永不过期

从缓存层面来看，没有设置过期时间，所以不会出现热点 key过期后产生的问题。

#### 2、加互斥锁

https://blog.csdn.net/m0_72853403/article/details/135145363

分布式锁∶使用分布式锁，保证对于每个key同时只有一个线程去查询后端服务，其他线程没有获得分布式锁的权限，因此只需要等待即可。这种方式将高并发的压力转移到了分布式锁，因此对分布式锁的考验很大。这样当热点数据在缓存中到期消失时，大量用户请求查询热点数据时，就会只有一个线程去后台mysql查数据并写到缓存中，其余线程等待。（setnx）

## 缓存雪崩（redis集体宕机，redis存的一批数据集体过期）

> 概念

**缓存雪崩，是指在某一个时间段，缓存集中过期失效。 Redis宕机!** 
产生雪崩的原因之一，比如马上就要到双十二零点，很快就会迎来一波抢购，这波商品时间比较集中的放入了缓存，假设缓存一个小时。那么到了凌晨一点钟的时候，这批商品的缓存就都过期了。而对这批商品的访问查询，都落到了数据库上，对于数据库而言，就会产生周期性的压力波峰。于是所有的请求都会达到存储层，存储层的调用量会暴增，造成存储层也会挂掉的情况。

![](https://i-blog.csdnimg.cn/blog_migrate/658ca289cbee6d9a6cd459a05eb5c28f.png)

其实集中过期，倒不是非常致命，**比较致命的缓存雪崩，是缓存服务器某个节点宕机或断网**。因为自然形成的缓存雪崩，一定是在某个时间段集中创建缓存，这个时候，数据库也是可以顶住压力的。无非就是对数据库产生周期性的压力而已。而缓存服务节点的宕机，对数据库服务器造成的压力是不可预知的，很有可能瞬间就把数据库压垮。

> 解决方案
>

#### 1.redis高可用

这个思想的含义是，既然redis有可能挂掉，那我多增设几台redis，这样一台挂掉之后其他的还可以继续工作，其实就是搭建的集群。（异地多活!)

#### 2.限流降级（在SpringCloud讲解过!)

这个解决方案的思想是，在缓存失效后，通过加锁或者队列来控制读数据库写缓存的线程数量。比如对某个key只允许一个线程查询数据和写缓存，其他线程等待。

#### 3.数据预热

数据加热的含义就是在正式部署之前（即测试的时候），我先把可能的数据先预先访问一遍，这样部分可能大量访问的数据就会加载到缓存中。在即将发生大并发访问前手动触发加载缓存不同的key，设置不同的过期时间，让缓存失效的时间点尽量均匀。