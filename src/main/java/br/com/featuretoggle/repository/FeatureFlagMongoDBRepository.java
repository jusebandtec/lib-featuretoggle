package br.com.featuretoggle.repository;
import br.com.featuretoggle.domain.Feature;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureFlagMongoDBRepository extends MongoRepository<Feature, String> {

    @Query("{key:'?0'}")
    Feature findFeatureByKey(String key);

}
