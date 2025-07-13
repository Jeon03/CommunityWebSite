# 프로젝트 Coummunity Web Site
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/0ffab5de-2e5e-44ab-b24a-05d0e509468a" />

## 📙 프로젝트 개요

**Community Web Site**는 실명과 익명 게시판 기능을 동시에 제공하는 커뮤니티 플랫폼입니다.  
사용자 인증, 정보 공유, 실시간 소통을 지원하며,  
AI 뉴스 요약 기능과 공감/댓글 시스템을 통해 **참여형 정보 커뮤니티**를 지향합니다.

이 프로젝트는 단순 커뮤니티를 넘어,  
외부 뉴스 수집 및 GPT API 기반 요약 기능을 통해 **정보 제공과 사용자 간 상호작용을 결합**한 구조로 개발되었습니다.

## 🎯 프로젝트 목표

- 사용자 인증(실명/익명) 기반의 안전하고 자유로운 커뮤니티 환경 구축
- 이메일 인증 + JWT 기반 로그인 시스템 구현을 통해 **보안 강화**
- 자유게시판, 익명게시판, 장터게시판 등 다양한 목적의 게시판 제공
- **뉴스 API와 GPT 요약 기능**을 결합하여 실시간 정보 제공 기능 도입
- 댓글, 대댓글, 공감 기능을 통해 **사용자 간 활발한 소통 유도**
- AWS S3, .env 환경 설정 등 **실제 서비스에 근접한 인프라 구성**

> 단순 CRUD 게시판을 넘어, **정보 소비와 커뮤니티 참여가 결합된 플랫폼**을 구현하는 것이 최종 목표입니다.


## ⚙️ 개발 환경

- **Front-end**  
  <img src="https://img.shields.io/badge/React-20232A?style=flat&logo=react&logoColor=61DAFB" height="25"/>

- **Back-end**  
  <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white" height="25"/>

- **Database & Storage**  
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white" height="25"/>
  <img src="https://img.shields.io/badge/AWS%20S3-569A31?style=flat&logo=amazon-aws&logoColor=white" height="25"/>

- **디자인 도구**  
  <img src="https://img.shields.io/badge/Figma-F24E1E?style=flat&logo=figma&logoColor=white" height="25"/>
  
## 프로젝트 

### 시스템 구성도
<img width="2400" height="1350" alt="image" src="https://github.com/user-attachments/assets/1ab460e0-733b-4c66-943b-ccc7c66a4710" />

### ERD
<img width="2400" height="1350" alt="image" src="https://github.com/user-attachments/assets/c34495f7-0517-4749-aca7-b8e5647be062" />

## 🚀 주요 기능

### 회원 기능
- 이메일 인증 기반 회원가입 (JavaMailSender 사용)
- JWT 기반 로그인 및 인증 처리
- AES256을 활용한 이메일 암호화 저장
- 비밀번호는 Spring Security의 PasswordEncoder로 암호화

### 게시판 기능
- 자유게시판, 익명게시판, 장터게시판 CRUD
- AWS S3 연동 이미지 업로드
- 게시글 공감(좋아요) 기능
- 댓글 및 대댓글(답글) 작성 기능
- 익명게시판은 익명1, 익명2 형식으로 자동 표시

### AI 뉴스 요약 기능
- NewsAPI를 통해 실시간 해외 뉴스 수집
- GPT API를 활용한 뉴스 본문 요약 기능
- 요약 결과는 뉴스 카드 내 모달 팝업 형태로 출력

### 통합 검색 기능
- 게시글 제목/내용 기준 전체 게시판 통합 검색 지원

### 마이페이지
- 내가 쓴 글 / 댓글 단 글 / 공감 누른 글 목록 확인
- 회원 탈퇴 기능 제공
