package io.concertreservation.common.mapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public class ObjectMapperBasedVoMapper {


    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T, U> U convert(T from, Class<U> to) {
        try {
            String json = objectMapper.writeValueAsString(from);
            return objectMapper.readValue(json, to);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Object mapping failed", e);
        }
    }

    public static <T, U> List<U> convert(List<T> from, Class<U> to) {
        return from.stream()
                .map(each -> convert(each, to))
                .collect(Collectors.toList());
    }



    /**
     * 소스 객체에서 널이 아닌 값들로 목적지 객체의 속성을 업데이트합니다.
     * 이 메소드는 Jackson의 ObjectMapper를 사용하여 목적지 객체를 선택적으로 업데이트합니다.
     * 소스 객체 내의 널이 아닌 필드들만이 목적지 객체의 해당 필드를 업데이트하는 데 사용됩니다.
     * 소스 객체에 존재하지 않거나 소스 객체에서 널인 필드들은 목적지 객체에서 변경되지 않습니다.
     *
     * @param <T> 속성을 복사할 소스 객체의 타입입니다.
     * @param <U> 속성이 복사될 목적지 객체의 타입입니다.
     * @param source 속성을 복사할 소스 객체입니다. 오직 널이 아닌 필드들만 복사됩니다.
     * @param destination 속성이 복사될 목적지 객체입니다. 이 객체는 현장에서 수정되며 메소드에 의해 반환됩니다.
     * @return 소스 객체에서 속성이 복사된 업데이트된 목적지 객체.
     * @throws IllegalArgumentException 소스와 목적지 객체 간의 호환성 문제로 인해 업데이트 작업이 실패한 경우.
     * @throws RuntimeException 객체 매핑 과정이 실패한 경우.
     */
    public static <T, U> U updateRecordBasedEntityWithNonNullValues(T source, U destination) {
        try {
            // 기존 로직
            ObjectReader updater = objectMapper.readerForUpdating(destination);
            JsonNode sourceNode = objectMapper.valueToTree(source);

            ObjectNode nonNullSourceNode = objectMapper.createObjectNode();
            sourceNode.fields().forEachRemaining(entry -> {
                if (!entry.getValue().isNull()) {
                    nonNullSourceNode.set(entry.getKey(), entry.getValue());
                }

            });

            U updatedDestination = updater.readValue(nonNullSourceNode);


            return updatedDestination;
        } catch (IllegalArgumentException | IOException e) {
            throw new RuntimeException("Object update failed", e);
        }
    }




    public static <T, U> U updatePropertiesWithNonNullValues(T source, U destination) {
        try {
            ObjectReader updater = objectMapper.readerForUpdating(destination);
            JsonNode sourceNode = objectMapper.convertValue(source, JsonNode.class);

            ObjectNode nonNullSourceNode = objectMapper.createObjectNode();
            sourceNode.fields().forEachRemaining(entry -> {
                if (!entry.getValue().isNull()) {
                    nonNullSourceNode.set(entry.getKey(), entry.getValue());
                }
            });

            return updater.readValue(nonNullSourceNode);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update properties", e);
        }
    }





}
