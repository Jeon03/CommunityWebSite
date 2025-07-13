package com.community.backend.service.Email;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final Map<String, CodeInfo> verificationCodes = new ConcurrentHashMap<>();
    private final Set<String> verifiedEmails = ConcurrentHashMap.newKeySet();

    //인증코드 생성
    private String generateCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000); // 6자리 숫자
    }

    //이메일로 인증코드 전송
    @Async
    public void sendCodeToEmail(String email) {
        String code = generateCode();
        verificationCodes.put(email, new CodeInfo(code, System.currentTimeMillis()));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[Community Website] 이메일 인증번호");
        message.setText("인증번호는: " + code + " 입니다.");
        mailSender.send(message);
    }

    //일치 검증
    public boolean verifyCode(String email, String inputCode) {
        CodeInfo info = verificationCodes.get(email);
        if (info == null) return false;

        //유효시간 체크 (5분 = 300,000ms)
        long now = System.currentTimeMillis();
        if (now - info.getCreatedAt() > 300_000) {
            verificationCodes.remove(email);  // 만료되었으니 제거
            return false;
        }

        //코드 일치 체크
        boolean match = info.getCode().equals(inputCode);
        if (match) {
            verifiedEmails.add(email);               // 인증 기록
            verificationCodes.remove(email);         // 재사용 방지
        }

        return match;
    }

    public boolean isVerified(String email) {
        return verifiedEmails.contains(email);
    }

}
