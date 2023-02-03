package br.com.featuretoggle.repository;

import br.com.featuretoggle.extensions.JsonExtensions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Dictionary;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    @Autowired
    private JedisPool jedisPool;

    private final Logger logger = LogManager.getLogger(RedisRepositoryImpl.class);


    @Override
    public Dictionary<Object, Object> findAll() {
        return null;
    }

    @Override
    public <TDocument> TDocument get(String key, TDocument document) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            var responseJson = jedis.get(key);

            logger.info(String.format("KEY: %s - %s", key, responseJson));

            if (StringUtils.hasText(responseJson)) {
                logger.info("Redis", String.format("Redis - GET: %s - %s", key, responseJson));
                return JsonExtensions.<TDocument>serializeToOjbect(responseJson, document.getClass());
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public <TDocument> TDocument update(String key, TDocument document) {
        return this.save(key, document);
    }

    @Override
    public void delete(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            jedis.del(key);

            logger.info("Redis", String.format("Redis - Del: %s", key));
        } catch (Exception e) {
            logger.error("Error occured while flushing specific data from the cache ", e.getMessage());
            releaseJedisInstance(jedis);
            throw new RuntimeException(e);

        } finally {
            releaseJedisInstance(jedis);
        }
    }

    @Override
    public <T> T save(String key, T projection) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            String json = JsonExtensions.serializeToJson(projection);
            logger.info("MongoDb", String.format("Redis - PUT: %s", json));
            jedis.set(key, json);
            jedis.expire(key, 600000);
        } catch (Exception e) {
            logger.error("Error occured while storing data to the cache ", e.getMessage());
            releaseJedisInstance(jedis);
            throw new RuntimeException(e);

        } finally {
            releaseJedisInstance(jedis);
        }

        return projection;
    }

    private void releaseJedisInstance(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
            jedis = null;
        }
    }
}
