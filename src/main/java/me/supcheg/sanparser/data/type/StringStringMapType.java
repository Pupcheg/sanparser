package me.supcheg.sanparser.data.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;

import java.util.HashMap;

public class StringStringMapType extends StringConvertingType<HashMap<String, String>>{
    private final ObjectMapper mapper = new ObjectMapper();
    private final TypeReference<HashMap<String, String>> typeRef = new TypeReference<>() {};

    @Override
    public HashMap<String, String> fromStringValue(CharSequence sequence) {
        try {
            return mapper.readValue(sequence.toString(), typeRef);
        } catch (JsonProcessingException e) {
            throw new HibernateException(e);
        }
    }

    @Override
    public String toString(HashMap<String, String> value) throws HibernateException {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new HibernateException(e);
        }
    }
}
