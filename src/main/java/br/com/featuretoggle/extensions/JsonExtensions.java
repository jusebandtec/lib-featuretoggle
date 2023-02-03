package br.com.featuretoggle.extensions;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonExtensions {

    public static String serializeToJson(Object objeto) {
        return new Gson().toJson(objeto);
    }

    public static <T> T serializeToOjbect(String jsonReader, Class<?> object) {
        return new Gson().fromJson(jsonReader, (Type) object);
    }
}
