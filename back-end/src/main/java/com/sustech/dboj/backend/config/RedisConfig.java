package com.sustech.dboj.backend.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@EnableCaching
@Configuration
public class RedisConfig {

    private final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    //Redis数据库索引（默认为0）
    @Value("${spring.redis.database}")
    private int database;

    //Redis服务器地址
    @Value("${spring.redis.host}")
    private String host;

    //Redis服务器连接端口
    @Value("${spring.redis.port}")
    private int port;

    //Redis服务器连接密码
    @Value("${spring.redis.password}")
    private String password;

    //客户端超时时间单位是毫秒 默认是2000
    @Value("${spring.redis.timeout}")
    private int timeout;

    //连接池的最大数据库连接数。设为0表示无限制,如果是jedis 2.4以后用redis.maxTotal
    @Value("${spring.redis.pool.max-active}")
    private int maxActive;

    //最大建立连接等待时间
    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

    //连接池中的最大空闲连接
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    //连接池中的最小空闲连接
    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;


    /**
     * 注入 RedisConnectionFactory
     */
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            if (!StringUtils.isEmpty(host)) {
                logger.info("init JredisPool ......");
                //连接池的最大数据库连接数
                jedisPoolConfig.setMaxTotal(maxActive);
                //最大建立连接等待时间
                jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
                //连接池中的最大空闲连接
                jedisPoolConfig.setMaxIdle(maxIdle);
                //连接池中的最小空闲连接
                jedisPoolConfig.setMinIdle(minIdle);
            }
            return jedisPoolConfig;
        }catch (Exception e){
            logger.info("init jedisPool error:{0}", e);
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory( JedisPoolConfig jedisPoolConfig){
        logger.info("***jedisPoolConfig***"+jedisPoolConfig);
        logger.info("init jedisConnectionFactory...");
        logger.info("spring.redis.pool.host:{}", this.host);
        logger.info("spring.redis.pool.port:{}", this.port);
        logger.info("spring.redis.pool.timeout:{}", this.timeout);
        logger.info("spring.redis.pool.database:{}", this.database);
        logger.info("spring.redis.pool.max-idle:{}", jedisPoolConfig.getMaxIdle());
        logger.info("spring.redis.pool.min-idle:{}", jedisPoolConfig.getMinIdle());
        logger.info("spring.redis.pool.max-active:{}", jedisPoolConfig.getMaxTotal());
        logger.info("spring.redis.pool.max-wait:{}", jedisPoolConfig.getMaxWaitMillis());
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        //连接池
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        //IP地址
        jedisConnectionFactory.setHostName(this.host);
        //端口号
        jedisConnectionFactory.setPort(this.port);
        if (StringUtils.isNotEmpty(this.password)) {
            //如果Redis设置有密码
            jedisConnectionFactory.setPassword(this.password);
        }
        //客户端超时时间单位是毫秒
        jedisConnectionFactory.setTimeout(this.timeout);
        //Redis数据库索引
        jedisConnectionFactory.setDatabase(this.database);
        //使用连接池
        jedisConnectionFactory.setUsePool(true);
        return jedisConnectionFactory;
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    public RedisTemplate<String, Object> stringSerializerRedisTemplate() {
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(){
        StringRedisTemplate stringRedisTemplate=new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }
}
