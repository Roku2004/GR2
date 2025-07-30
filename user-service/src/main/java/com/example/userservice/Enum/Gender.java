package com.example.userservice.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    NAM(0),
    NỮ(1);

    private final int value;

    public static Gender fromValue(int value) {
        for (Gender gender : values()) {
            if (gender.getValue() == value) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender value: " + value);
    }
}
