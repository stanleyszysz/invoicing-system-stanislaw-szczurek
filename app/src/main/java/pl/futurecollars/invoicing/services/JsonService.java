package pl.futurecollars.invoicing.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonService<T> {

    private ObjectMapper objectMapper;

    public JsonService() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
    }

    public String toJson(final T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public T toObject(final String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }
}
