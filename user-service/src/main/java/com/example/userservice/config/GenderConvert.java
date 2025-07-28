package com.example.userservice.config;

import com.example.userservice.Enum.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GenderConvert implements AttributeConverter<Gender, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Gender gender) {
        return gender != null ? gender.getValue() : null;
    }

    @Override
    public Gender convertToEntityAttribute(Integer dbData) {
        return dbData != null ? Gender.fromValue(dbData) : null;
    }
}