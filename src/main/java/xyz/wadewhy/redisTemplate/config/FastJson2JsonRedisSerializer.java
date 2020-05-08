package xyz.wadewhy.redisTemplate.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @PACKAGE_NAME: xyz.wadewhy.springboot_redis01.base.utils
 * @NAME: FastJson2JsonRedisSerializer
 * @Author: 钟子豪
 * @DATE: 2020/3/25
 * @MONTH_NAME_FULL: 三月
 * @DAY: 25
 * @DAY_NAME_FULL: 星期三
 * @PROJECT_NAME: springboot_redis01
 * 自定义序列化，完成redis值的序列化
 **/
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super(); this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    /**
     * 反序列化
     * @param bytes
     * @return
     * @throws SerializationException
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String srt = new String(bytes, DEFAULT_CHARSET);
        //解决com.alibaba.fastjson.JSONException: autoType is not support,添加白名单
        ParserConfig.getGlobalInstance().addAccept("xyz.wadewhy.springboot_redis01");
        return (T)JSON.parseObject(srt, clazz);
    }
}
