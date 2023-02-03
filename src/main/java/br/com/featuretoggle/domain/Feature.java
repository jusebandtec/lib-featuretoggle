package br.com.featuretoggle.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashMap;


@Document("ORQUESTRADOR_FEATURE_FLAG")
@Getter
@Setter
public class Feature implements Serializable {

    @Id
    private String id;
    private String key;
    private boolean enabled;
    private HashMap<String, Object> configurations;

    public Feature(String id, String key, boolean enabled, HashMap<String, Object> configurations) {
        this.id = id;
        this.key = key;
        this.enabled = enabled;
        this.configurations = configurations == null ? new HashMap<>() : configurations;
    }

    public Feature() {}
}
