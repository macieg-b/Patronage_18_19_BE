package mba.bookingsystem.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ModelMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T convertToView(Object model, Class<T> viewClass) {
        return objectMapper.convertValue(model, viewClass);
    }

    public static <T> List<T> convertToView(List<?> modelList, Class<T> viewClass) {
        List<T> views = new ArrayList<>();
        for (Object model : modelList) {
            T view = convertToView(model, viewClass);
            views.add(view);
        }
        return views;
    }

    public static <T> T convertToModel(Object view, Class<T> modelClass) {
        return objectMapper.convertValue(view, modelClass);
    }

    public static String modelToString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
