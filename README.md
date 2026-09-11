# 📋 자유게시판 (Spring Boot Board)

Spring Boot + Thymeleaf + MySQL로 구현한 회원 인증 기반 게시판입니다. 기본 CRUD에서 출발해 인증/인가, 댓글, 페이징, 검색까지 단계적으로 기능을 확장했습니다.

## ✨ 주요 기능

- 🔐 회원가입 / 로그인 / 로그아웃 (Spring Security, BCrypt 비밀번호 암호화)
- 📝 게시글 CRUD (작성 / 목록·상세 조회 / 수정 / 삭제)
- 💬 댓글 CRUD (게시글-댓글 연관관계 매핑)
- 🔒 소유권 기반 권한 제어 (본인이 작성한 글/댓글만 수정·삭제 가능)
- 👀 조회수 중복 증가 방지 (쿠키 기반)
- 🔍 제목/작성자 통합 검색
- 📄 페이징 처리
- 📱 반응형 UI (모바일에서 테이블 → 카드형 자동 전환)

## 🛠 기술 스택

**Backend**
- Java 21
- Spring Boot 4.1.1
- Spring Security
- Spring Data JPA (Hibernate)
- Gradle

**Frontend**
- Thymeleaf
- Vanilla CSS / JavaScript

**Database**
- MySQL

## 📁 프로젝트 구조

```
src/main/java/com/example/demo/
 ├── config/
 │    ├── SecurityBeanConfig.java   # PasswordEncoder 빈 등록
 │    ├── SecurityConfig.java       # URL별 인가 규칙, 로그인/로그아웃 설정
 │    └── MemberDetails.java        # UserDetails 구현체
 ├── controller/
 │    ├── BoardController.java
 │    ├── CommentController.java
 │    └── MemberController.java
 ├── service/
 │    ├── BoardService.java
 │    ├── CommentService.java
 │    ├── MemberService.java
 │    └── MemberDetailsService.java # UserDetailsService 구현체
 ├── repository/
 │    ├── BoardRepository.java
 │    ├── CommentRepository.java
 │    └── MemberRepository.java
 ├── entity/
 │    ├── BoardEntity.java
 │    ├── CommentEntity.java        # BoardEntity와 @ManyToOne 연관관계
 │    └── MemberEntity.java
 └── dto/
      ├── BoardDTO.java
      ├── CommentDTO.java
      └── MemberDTO.java

src/main/resources/
 ├── templates/
 │    ├── join.html / login.html
 │    └── board/
 │         ├── list.html / detail.html
 │         ├── save.html / update.html / delete.html
 └── static/css/board.css
```

## 🚀 시작하기

### 사전 요구사항

- JDK 21+
- MySQL 8.x
- Gradle (프로젝트에 포함된 Gradle Wrapper 사용 가능)

### 1. 저장소 클론

```bash
git clone <저장소 URL>
cd demo
```

### 2. 데이터베이스 생성

```sql
CREATE DATABASE board_db CHARACTER SET utf8mb4;
```

### 3. `application.yml` (또는 `application.properties`) 설정

`src/main/resources/application.yml`에 본인 환경에 맞게 작성:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/board_db?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

> 비밀번호 등 민감한 정보는 환경 변수나 `.gitignore` 처리된 별도 설정 파일로 관리하는 것을 권장합니다.

### 4. 실행

```bash
./gradlew bootRun
```

브라우저에서 `http://localhost:8080/board/` 접속

## 🗺 주요 엔드포인트

| Method | URL | 설명 | 인증 필요 |
|---|---|---|---|
| GET | `/board/` | 게시글 목록 (검색·페이징) | ❌ |
| GET | `/board/{id}` | 게시글 상세 | ❌ |
| GET/POST | `/board/save` | 게시글 작성 | ✅ |
| GET/POST | `/board/update/{id}` | 게시글 수정 (본인 글만) | ✅ |
| GET/POST | `/board/delete/{id}` | 게시글 삭제 (본인 글만) | ✅ |
| POST | `/comment/save` | 댓글 작성 | ✅ |
| POST | `/comment/update` | 댓글 수정 (본인 댓글만) | ✅ |
| POST | `/comment/delete` | 댓글 삭제 (본인 댓글만) | ✅ |
| GET/POST | `/member/join` | 회원가입 | ❌ |
| GET/POST | `/member/login` | 로그인 | ❌ |
| GET | `/member/logout` | 로그아웃 | ✅ |

## 🧠 배운 점 / 트러블슈팅

프로젝트를 진행하며 겪었던 문제와 해결 과정은 별도 문서에 정리했습니다: (포트폴리오 문서 링크 추가)

주요 항목:
- GET 요청만으로 삭제가 실행되던 구조를 HTTP 메서드 원칙에 맞게 개선
- 비밀번호 기반 게시글 인증을 Spring Security 기반 계정 소유권 검증으로 전환
- Spring Security `SecurityFilterChain` 규칙 순서로 인한 URL 매핑 충돌 해결
- 페이징 도입 시 목록 번호 계산 로직 재설계

## 📌 향후 개선 방향

- [ ] CSRF 보호 활성화
- [ ] 테스트 코드 작성 (JUnit, Mockito)
- [ ] 댓글 목록 조회 N+1 문제 점검
- [ ] OAuth2 소셜 로그인
- [ ] 이미지/파일 업로드

## 📄 License

이 프로젝트는 학습 목적으로 제작되었습니다.
