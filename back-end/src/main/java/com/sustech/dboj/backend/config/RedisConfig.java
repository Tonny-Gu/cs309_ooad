//package com.sustech.dboj.backend.config;
//
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisPassword;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import redis.clients.jedis.JedisPoolConfig;
//
//@EnableCaching
//@Configuration
//public class RedisConfig {
//
//    private final Logger logger = LoggerFactory.getLogger( RedisConfig.class );
//
//    //Redis数据库索引（默认为0）
//    @Value("${spring.redis.database}")
//    private int database;
//
//    //Redis服务器地址
//    @Value("${spring.redis.host}")
//    private String host;
//
//    //Redis服务器连接端口
//    @Value("${spring.redis.port}")
//    private int port;
//
//    //客户端超时时间单位是毫秒 默认是2000
//    @Value("${spring.redis.timeout}")
//    private int timeout;
//
//    //连接池的最大数据库连接数。设为0表示无限制,如果是jedis 2.4以后用redis.maxTotal
//    @Value("${spring.redis.jedis.pool.max-active}")
//    private int maxActive;
//
//    //最大建立连接等待时间
//    @Value("${spring.redis.jedis.pool.max-wait}")
//    private long maxWaitMillis;
//
//    //连接池中的最大空闲连接
//    @Value("${spring.redis.jedis.pool.max-idle}")
//    private int maxIdle;
//
//    //连接池中的最小空闲连接
//    @Value("${spring.redis.jedis.pool.min-idle}")
//    private int minIdle;
//
//
//    /**
//     * 注入 RedisConnectionFactory
//     */
//    @Autowired
//    private RedisConnectionFactory redisConnectionFactory;
//
//    @Bean
//    public JedisPoolConfig jedisPoolConfig() {
//        try {
//            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig( );
//            if ( !StringUtils.isEmpty( host ) ) {
//                logger.info( "init JedisPool ......" );
//                //连接池的最大数据库连接数
//                jedisPoolConfig.setMaxTotal( maxActive );
//                //最大建立连接等待时间
//                jedisPoolConfig.setMaxWaitMillis( maxWaitMillis );
//                //连接池中的最大空闲连接
//                jedisPoolConfig.setMaxIdle( maxIdle );
//                //连接池中的最小空闲连接
//                jedisPoolConfig.setMinIdle( minIdle );
//            }
//            return jedisPoolConfig;
//        } catch (Exception e) {
//            logger.info( "init jedisPool error:{0}" , e );
//            e.printStackTrace( );
//        }
//        return null;
//    }
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
//        //单机版jedis
//        RedisStandaloneConfiguration redisStandaloneConfiguration =
//                new RedisStandaloneConfiguration();
//        //设置redis服务器的host或者ip地址
//        redisStandaloneConfiguration.setHostName(host);
//        //设置默认使用的数据库
//        redisStandaloneConfiguration.setDatabase(0);
//        //设置密码
////        redisStandaloneConfiguration.setPassword( RedisPassword.of("123456"));
//        //设置redis的服务的端口号
//        redisStandaloneConfiguration.setPort(port);
//        //获得默认的连接池构造器(怎么设计的，为什么不抽象出单独类，供用户使用呢)
//        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
//                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
//        //指定jedisPoolConifig来修改默认的连接池构造器（真麻烦，滥用设计模式！）
//        jpcb.poolConfig(jedisPoolConfig);
//        //通过构造器来构造jedis客户端配置
//        JedisClientConfiguration jedisClientConfiguration = jpcb.build();
//        //单机配置 + 客户端配置 = jedis连接工厂
//        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
//    }
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @Bean
//    public RedisTemplate<String, Object> stringSerializerRedisTemplate() {
//        RedisSerializer<String> stringSerializer = new StringRedisSerializer( );
//        redisTemplate.setKeySerializer( stringSerializer );
//        redisTemplate.setValueSerializer( stringSerializer );
//        redisTemplate.setHashKeySerializer( stringSerializer );
//        redisTemplate.setHashValueSerializer( stringSerializer );
//        return redisTemplate;
//    }
//
//    @Bean
//    public StringRedisTemplate stringRedisTemplate() {
//        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate( );
//        stringRedisTemplate.setConnectionFactory( redisConnectionFactory );
//        return stringRedisTemplate;
//    }
//}
