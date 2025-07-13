package com.community.backend.SecurityConfig;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


//속성변환기
@Converter
public class EmailEncryptConverter implements AttributeConverter<String, String> {
    //DB에 저장하기 전에 암호화
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        return AESUtil.encrypt(attribute); //이메일을 암호화하여 반환
    }

    //DB에서 데이터를 가져와 엔티티에 담을때 복호화
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return AESUtil.decrypt(dbData); //이메일을 복호화하여 반환
    }
}