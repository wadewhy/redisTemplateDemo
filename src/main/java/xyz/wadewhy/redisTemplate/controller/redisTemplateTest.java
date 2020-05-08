/**   
 * Copyright © 2020 eSunny Info. Tech Ltd. All rights reserved.
 * 作者博客：wadewhy.xyz
 * @Package: xyz.wadewhy.redisTemplate.controller 
 * @author: wadewhy   
 * @date: 2020年5月7日 下午8:39:02 
 */
package xyz.wadewhy.redisTemplate.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.wadewhy.redisTemplate.config.RedisTemplate;

@RestController
public class redisTemplateTest {

    @Autowired
    private RedisTemplate redisTemplate;

    // =============================common====================================//
    /**
     * 指定缓存失效时间
     * 
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * 
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * 
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * 
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    // ================================String=================================//
    /**
     * 
     * @Title: set
     * @Description: TODO 普通缓存放入
     * @param key     key的名称
     * @param value   key对应value的名称
     * @param indexdb 数据库的索引
     * @return boolean
     * @author wadewhy
     * @date 2020年5月7日下午8:40:13
     */
    @RequestMapping("/StringSet/{key}/{value}/{indexdb}")
    public String StringSet(@PathVariable("key") String key, @PathVariable("value") Object value,
            @PathVariable("indexdb") Integer indexdb) {
        try {
            // 设置保存到那个库
            redisTemplate.indexdb.set(indexdb);
            // 设置key-value
            redisTemplate.opsForValue().set(key, value);
            String str = "---StringSet---【" + "key:" + key + ":value" + value + "】";
            System.err.println(str);
            return str;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 
     * @Title: StringGet
     * @Description: TODO 获取String类型key下的value
     * @param key     key名称
     * @param indexdb 数据库名称
     * @return Object 返回的value
     * @author wadewhy
     * @date 2020年5月7日下午9:41:18
     */

    @RequestMapping("/StringGet/{key}/{indexdb}")
    public Object StringGet(@PathVariable("key") String key, @PathVariable("indexdb") Integer indexdb) {
        // 选择数据库索引
        redisTemplate.indexdb.set(indexdb);
        Object object = key == null ? null : redisTemplate.opsForValue().get(key);
        System.err.println("-------StringGet-------" + object);
        return object;
    }

    /**
     * 
     * @Title: StringTimeSet
     * @Description: TODO 普通缓存放入并设置时间
     * @param key   key名称
     * @param value key对应value的值
     * @param time  时间(秒)，time>0,如果time<0,设置无期限
     * @return String
     * @author wadewhy
     * @date 2020年5月7日下午9:49:49
     */
    @RequestMapping("/StringTimeSet/{key}/{value}/{time}")
    public String StringTimeSet(@PathVariable("key") String key, @PathVariable("value") Object value,
            @PathVariable("time") Long time) {
        String str = "---StringSet---【" + "key:" + key + "-----value:" + value + "-----time:" + time + "】";
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
                System.err.println(str);
            } else {
                // 设置key-value
                redisTemplate.opsForValue().set(key, value);
                System.err.println(str);
            }
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 
     * @Title: StringIncrDecr
     * @Description: TODO String中key递增或递减
     * @param key
     * @param delta 当大于0递增，小于0递减
     * @return String
     * @author wadewhy
     * @date 2020年5月7日下午9:59:24
     */
    @RequestMapping("/StringIncrDecr/{key}/{delta}")
    public String StringIncrDecr(@PathVariable("key") String key, @PathVariable("delta") long delta) {
        String str = null;
        if (delta < 0) {
            str = "递减后结果：" + redisTemplate.opsForValue().increment(key, delta);
            System.err.println("---------StringIncrDecr----------" + str);
            return str;
        } else if (delta > 0) {
            str = "递增后结果：" + redisTemplate.opsForValue().increment(key, delta);
            System.err.println("---------StringIncrDecr----------" + str);
        } else {
            str = "你他妈耍我,delta=0";
            System.err.println("---------StringIncrDecr----------" + str);
        }

        return str;
    }

    // =====================================Hash==============================================//
    /**
     * 
     * @Title: getMap
     * @Description: TODO 用于模拟
     * @return Map<String,Object>
     * @author wadewhy
     * @date 2020年5月7日下午10:20:00
     */
    public Map<String, Object> getMap() {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", "wadewhy" + (int) (Math.random() * 100));
        hashMap.put("age", (int) (Math.random() * 100));
        hashMap.put("salary", 10 * (int) (Math.random() * 100));
        return hashMap;
    }

    /**
     * 
     * @Title: hmset
     * @Description: TODO 设置key===>[name:wadewhy,age:22]这类型
     * @param key key的名称，getMap()方法模拟map
     * @return String
     * @author wadewhy
     * @date 2020年5月7日下午10:25:11
     */
    @RequestMapping("/hmset/{key}")
    public String hmset(@PathVariable("key") String key) {
        try {
            Map<String, Object> hashMap = getMap();
            redisTemplate.opsForHash().putAll(key, hashMap);
            String str = "---StringSet---【" + "key:" + key + "-----map:" + hashMap;
            System.err.println("------hmset------" + str);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 获取hashKey对应的所有键值
     * 
     * @param key 键
     * @return 对应的多个键值
     */
    @RequestMapping("hmget/{key}")
    public Map<Object, Object> hmget(@PathVariable("key") String key) {
        System.err.println("-------hmget---------" + key + "=====>" + redisTemplate.opsForHash().entries(key));
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet 并设置时间
     * 
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    @RequestMapping("/hmsetTime/{key}/{time}")
    public String hmsetTime(@PathVariable("key") String key, @PathVariable("time") long time) {
        try {
            Map<String, Object> hashMap = getMap();
            redisTemplate.opsForHash().putAll(key, hashMap);
            String str = "---StringSet---【" + "key:" + key + "-----map:" + hashMap + "-------time:" + time;
            System.err.println("------hmsetTime------" + str);
            if (time > 0) {
                expire(key, time);
            }
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * 
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    @RequestMapping("/hset/{key}/{item}/{value}")
    public String hset(@PathVariable("key") String key, @PathVariable("item") String item,
            @PathVariable("value") Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            String str = "---StringSet---【" + "key:" + key + "-----item:" + item + "------value:" + value;
            System.err.println("------hset------" + str);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * 
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    @RequestMapping("/hsetTime/{key}/{item}/{value}/{time}")
    public String hsetTime(@PathVariable("key") String key, @PathVariable("item") String item,
            @PathVariable("value") Object value, @PathVariable("time") long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            String str = "---StringSet---【" + "key:" + key + "-----item:" + item + "-----value:" + value
                    + "-------time:" + time;
            System.err.println("------hsetTime------" + str);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 删除hash表中的值 不演示，Object...item不好模拟
     * 
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public String hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
        return "删除成功------>" + redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     * 
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * 
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)要减少记(小于0)
     * @return
     */
    @RequestMapping("/hincr/{key}/{item}/{by}")
    public String hincrDecr(@PathVariable("key") String key, @PathVariable("item") String item,
            @PathVariable("by") double by) {

        String str = "---hincrDecr---【" + "key:" + key + "-----item:" + item + "------by:" + by + "【】"
                + redisTemplate.opsForHash().increment(key, item, by);
        System.err.println("------hincrDecr------" + str);
        return str;
    }

    // =====================================set==============================================//
    /**
     * 
     * @Title: getObject
     * @Description: TODO 模拟set中key设置多个value值
     * @return Object[]
     * @author wadewhy
     * @date 2020年5月8日上午10:42:55
     */
    public Object[] getObject() {
        int num = (int) (Math.random() * 10);
        System.err.println("-----getObject-----" + num);
        Object[] objects = new Object[num];
        for (int i = 0; i < num; i++) {
            objects[i] = "wade" + (int) (Math.random() * 100) + "why";
        }
        return objects;
    }

    /**
     * 
     * @Title: sSet
     * @Description: TODO 将数据放入set缓存
     * @param key
     * @return String
     * @author wadewhy
     * @date 2020年5月8日上午10:47:20
     */
    @RequestMapping("/sSet/{key}")
    public String sSet(@PathVariable("key") String key) {
        try {
            // 模拟key对应的value
            Object[] values = getObject();
            redisTemplate.opsForSet().add(key, values);
            String str = "---sSet---【" + "key:" + key + "-----values:" + values;
            System.err.println("------sSet------" + str);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 
     * @Title: sSetAndTime
     * @Description: TODO 将set数据放入缓存时间限制，到时间就删除
     * @param key
     * @param time
     * @return String
     * @author wadewhy
     * @date 2020年5月8日上午11:01:47
     */
    @RequestMapping("/sSetAndTime/{key}/{time}")
    public String sSetAndTime(@PathVariable("key") String key, @PathVariable("time") long time) {
        try {
            // 模拟key对应的value
            Object[] values = getObject();
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            String str = "---sSet---【" + "key:" + key + "-----values:" + values + "-----time:" + time + "-----count:"
                    + count;
            System.err.println("------sSetAndTime------" + str);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 
     * @Title: sGetSetSize
     * @Description: TODO 获取set缓存的长度
     * @param key
     * @return String
     * @author wadewhy
     * @date 2020年5月8日上午11:06:21
     */
    @RequestMapping("/sGetSetSize/{key}")
    public String sGetSetSize(@PathVariable("key") String key) {
        try {
            long num = redisTemplate.opsForSet().size(key);
            String str = "---sSet---【" + "key:" + key + "-----num:" + num;
            System.err.println("------sGetSetSize------" + str);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 移除值为value的 这里不演示
     * 
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    // ===============================list===================================================================
    // 剩下的不演示，可以看config中的Util工具类

}
