package br.com.featuretoggle.repository;

public interface MongoDbRepository {
    <T> T save(T object);
    <T, O> O get(T object);
    <T> void delete(T object);
}
