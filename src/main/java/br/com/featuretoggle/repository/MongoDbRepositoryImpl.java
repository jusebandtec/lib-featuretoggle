package br.com.featuretoggle.repository;

import br.com.featuretoggle.domain.Feature;
import br.com.featuretoggle.extensions.JsonExtensions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MongoDbRepositoryImpl implements MongoDbRepository{

    private static final Logger logger = LogManager.getLogger(MongoDbRepositoryImpl.class);

    @Autowired
    private FeatureFlagMongoDBRepository featureFlagMongoDBRepository;

    @Override
    public <T> T save(T object) {
        return (T) this.featureFlagMongoDBRepository.save((Feature) object);
    }

    @Override
    public <T, O> O get(T object) {
        var featureCollection = (O) this.featureFlagMongoDBRepository.findFeatureByKey((String) object);
        logger.info("MongoDb", String.format("MongoDB - PUT: %s", JsonExtensions.serializeToJson(featureCollection)));
        return featureCollection;
    }

    @Override
    public <T> void delete(T object) {
        logger.info("MongoDb", String.format("MongoDB - DEL: %s", ((Feature) object).getId()));
        this.featureFlagMongoDBRepository.deleteById(((Feature) object).getId());
    }
}
