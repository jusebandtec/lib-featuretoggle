package br.com.featuretoggle.service;

import br.com.featuretoggle.domain.Feature;
import org.springframework.stereotype.Service;

@Service
public interface FeatureFlagService {

    Feature obter(String key);
    Feature atualizar(String key, Feature feature);
    void deletar(String id);
    Feature inserir(String key, Feature feature);

}
