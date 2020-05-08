/**   
 * Copyright © 2020 eSunny Info. Tech Ltd. All rights reserved.
 * 作者博客：wadewhy.xyz
 * @Package: xyz.wadewhy.redisTemplate.config 
 * @author: wadewhy   
 * @date: 2020年5月7日 下午3:27:34
 * @PROJECT_NAME: springboot_redis01 Redis配置类 
 */
package xyz.wadewhy.redisTemplate.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@PropertySource("classpath:application.yaml")
@Slf4j
public class RedisConfig {
    // 用yaml这种方式取RedisTemplate值时需要加改bean，并且下面取值直接取，不需要redis.hostName
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Value("${hostName}")
    private String hostName;
    @Value("${password}")
    private String password;
    @Value("${port}")
    private Integer port;

    @Value("${maxIdle}")
    private Integer maxIdle;

    @Value("${timeout}")
    private Integer timeout;

    @Value("${maxTotal}")
    private Integer maxTotal;

    @Value("${maxWaitMillis}")
    private Integer maxWaitMillis;

    @Value("${minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;

    @Value("${numTestsPerEvictionRun}")
    private Integer numTestsPerEvictionRun;

    @Value("${timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${testWhileIdle}")
    private boolean testWhileIdle;

    /**
     * @Author 钟子豪
     * @Date 2020/3/25
     * @description Jedis连接工厂
     * @Return org.springframework.data.redis.connection.jedis.JedisConnectionFactory
     */
    @Bean
    public JedisConnectionFactory JedisConnectionFactory() {
        // 单击版redis
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        // 设置redis服务器的host或者ip
        redisStandaloneConfiguration.setHostName(hostName);
        // 设置服务器端口
        redisStandaloneConfiguration.setPort(port);
        // 由于我们这里模拟环境，没有使用关系型数据库，用的是动态配置库
        // redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration
                .builder();
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(timeout));
        // 获取默认的连接池构造器
        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration.build());
        return factory;
    }

    /**
     * 实例化RedisTemplate对象
     * 
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("RedisTemplate实例化成功........");
        RedisTemplate redisTemplate = new RedisTemplate();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 引入自定义序列化
     * 
     * @return
     */
    @Bean
    public RedisSerializer fastJson2JsonRedisSerializer() {
        return new FastJson2JsonRedisSerializer<Object>(Object.class);
    }

    /**
     * 设置数据存入 redis 的序列化方式,并开启事务
     * 
     * @param redisTemplate
     * @param redisConnectionFactory
     */
    public void initDomainRedisTemplate(RedisTemplate redisTemplate, RedisConnectionFactory redisConnectionFactory) {
        // 如果不配置Serializer，那么存储的时候缺省使用String.
        // 如果用User类型存储，那么会提示错误User can't cast to String！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer());
        // 开启事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
    }

    /**
     * 注入封装的RedisTemplate
     * 
     * @param redisTemplate
     * @return
     */
    @Bean(name = "redisUtil")
    public RedisTemplateUtil redisUtil(RedisTemplate redisTemplate) {
        log.info("RedisUtil注入成功");
        RedisTemplateUtil redisUtil = new RedisTemplateUtil();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }

}
