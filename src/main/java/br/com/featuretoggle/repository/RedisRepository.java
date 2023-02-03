package br.com.featuretoggle.repository;

import java.util.Dictionary;

public interface RedisRepository {
    Dictionary<Object, Object> findAll();
    <TDocument> TDocument get(String key, TDocument document);
    <TDocument> TDocument update(String key, TDocument document);
    void delete(String key);
    <TDocument> TDocument save(String key, TDocument projection);
}
