package br.com.featuretoggle.service;

import br.com.featuretoggle.domain.Feature;
import br.com.featuretoggle.repository.MongoDbRepository;
import br.com.featuretoggle.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@Service
@EnableCaching
public class FeatureFlagServiceImpl implements FeatureFlagService {

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private MongoDbRepository mongoDbRepository;

    private final String KEY_FF = "ACBC_FF_%s";


    @Override
    public Feature obter(String key) {
        var featureRedis = this.redisRepository.<Feature>get(String.format(KEY_FF, key), new Feature());

        if (featureRedis != null)
            return featureRedis;

        var featureCollection = this.mongoDbRepository.<String, Feature>get(String.format(KEY_FF, key));

        if (featureCollection != null)
            this.redisRepository.save(String.format(KEY_FF, key), featureCollection);

        return featureCollection;
    }

    @Override
    public Feature atualizar(String key, Feature feature) {
        return this.redisRepository.<Feature>update(String.format(KEY_FF, key), feature);
    }

    @Override
    public void deletar(String key) {
        var feature = this.obter(key);
        this.redisRepository.delete(String.format(KEY_FF, key));
        this.mongoDbRepository.delete(feature);
    }

    @Override
    public Feature inserir(String key, Feature feature) {
        var obterFeature = this.obter(key);

        if (obterFeature != null)
            return obterFeature;

        feature.setKey(String.format(KEY_FF, feature.getKey()));
        var featureMongoDb = this.mongoDbRepository.<Feature>save(feature);
        return this.redisRepository.<Feature>save(String.format(KEY_FF, key), featureMongoDb);
    }
}
