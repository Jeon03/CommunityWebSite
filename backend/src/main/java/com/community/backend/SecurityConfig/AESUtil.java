package com.community.backend.SecurityConfig;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


@Component //스프링 컨테이너에 등록해서 자동으로 의존성 주입이 가능하게 해주는 어노테이션
public class AESUtil {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String STATIC_SECRET_KEY = dotenv.get("AES_SECRET_KEY");

    //Cipher는 암호화 알고리즘을 사용하여 데이터를 암호화또는 복호화하는 클래스
    //이메일 암호화
    public static String encrypt(String value) {
        try {
            SecretKeySpec key = new SecretKeySpec(STATIC_SECRET_KEY.getBytes(), "AES"); //암호화 키 설정
            Cipher cipher = Cipher.getInstance("AES"); //Cipher 객체 생성 및 초기화
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //평문 -> 암호화 -> base64 인코딩
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("암호화 실패", e);
        }
    }

    public static String decrypt(String encryptedValue) {
        try {
            SecretKeySpec key = new SecretKeySpec(STATIC_SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);//복호화모드로 초기화
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("복호화 실패", e);
        }
    }
}

