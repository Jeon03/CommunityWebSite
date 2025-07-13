package com.community.backend.controller;



import com.community.backend.SecurityConfig.JwtTokenProvider;
import com.community.backend.dto.Login.LoginRequest;
import com.community.backend.dto.Login.LoginResponse;
import com.community.backend.dto.Login.SignupRequest;
import com.community.backend.entity.User;
import com.community.backend.repository.UserRepository;
import com.community.backend.service.Email.EmailService;
import com.community.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            //Spring Security 인증 처리
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            //정보 로드
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("사용자 없음"));

            //JWT 토큰 생성
            String token = jwtTokenProvider.createToken(
                    user.getEmail(),
                    user.getNickname(),
                    List.of(user.getRole())
            );

            return ResponseEntity.ok(new LoginResponse(token));

        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("로그인 실패: 이메일 또는 비밀번호가 틀렸습니다.");
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userRepository.existsByEmail(email);
        return ResponseEntity.ok(!exists);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean exists = userRepository.existsByNickname(nickname);
        return ResponseEntity.ok(!exists);
    }

    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        emailService.sendCodeToEmail(email);
        return ResponseEntity.ok("인증번호가 이메일로 전송되었습니다.");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");

        boolean valid = emailService.verifyCode(email, code);
        if (valid) {
            return ResponseEntity.ok("이메일 인증 성공");
        } else {
            return ResponseEntity.badRequest().body("이메일 인증코드가 일치하지 않습니다.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        boolean verified = emailService.isVerified(request.getEmail());
        if (!verified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 인증이 필요합니다.");
        }

        userService.registerUser(request);
        return ResponseEntity.ok("회원가입 완료");
    }
}

