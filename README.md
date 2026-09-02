# 📋 Spring Boot 게시판

Spring Boot와 JPA를 활용하여 구현한 **CRUD 기반 게시판 프로젝트**입니다.

Spring Boot의 기본적인 계층 구조를 이해하고,
Controller → Service → Repository → Entity/DTO로 이어지는 데이터 흐름을 직접 구현하는 것을 목표로 제작했습니다.

---

## 🛠️ Tech Stack

| Category | Technology                 |
| -------- | -------------------------- |
| Backend  | Java, Spring Boot          |
| ORM      | Spring Data JPA, Hibernate |
| Database | MySQL                      |
| Frontend | HTML, Thymeleaf            |
| Build    | Gradle                     |
| IDE      | IntelliJ IDEA              |

---

## ✨ Features

### 📝 게시글 작성

* 작성자, 비밀번호, 제목, 내용을 입력하여 게시글 작성
* 작성한 게시글을 MySQL 데이터베이스에 저장

### 📚 게시글 목록 조회

* DB에 저장된 전체 게시글 조회
* 게시글 번호, 제목, 작성자, 작성일, 조회수 표시

### 🔎 게시글 상세 조회

* 게시글 ID를 기반으로 상세 내용 조회
* 게시글 조회 시 조회수 증가

### ✏️ 게시글 수정

* 게시글 비밀번호 확인
* 제목 및 내용 수정
* 비밀번호가 일치하지 않을 경우 예외 처리

### 🗑️ 게시글 삭제

* 게시글 ID를 기반으로 게시글 삭제

---

## 🏗️ Project Structure

```text
src
└── main
    ├── java
    │   └── com.example.demo
    │       ├── controller
    │       │   ├── BoardController.java
    │       │   └── HomeController.java
    │       │
    │       ├── service
    │       │   └── BoardService.java
    │       │
    │       ├── repository
    │       │   └── BoardRepository.java
    │       │
    │       ├── entity
    │       │   ├── BoardEntity.java
    │       │   └── BaseTimeEntity.java
    │       │
    │       └── dto
    │           └── BoardDTO.java
    │
    └── resources
        ├── templates
        │   ├── index.html
        │   ├── list.html
        │   ├── save.html
        │   ├── detail.html
        │   └── update.html
        │
        └── application.yml
```

---

## 🔄 Data Flow

게시글 작성부터 DB 저장까지의 흐름은 다음과 같습니다.

```text
사용자
  ↓
HTML Form
  ↓
Controller
  ↓
DTO
  ↓
Service
  ↓
Entity 변환
  ↓
Repository
  ↓
MySQL
```

게시글 조회의 경우에는 반대로 DB에서 데이터를 가져와 화면에 전달합니다.

```text
MySQL
  ↓
Repository
  ↓
Entity
  ↓
DTO 변환
  ↓
Service
  ↓
Controller
  ↓
Model
  ↓
Thymeleaf
  ↓
HTML 화면
```

---

## 📌 What I Learned

이번 프로젝트를 통해 Spring Boot의 기본적인 계층 구조와 데이터 흐름을 이해할 수 있었습니다.

특히 다음 개념을 직접 구현하며 익혔습니다.

* `@Controller`를 이용한 HTTP 요청 처리
* `GET` / `POST` 요청의 차이
* DTO와 Entity의 역할
* Service를 통한 비즈니스 로직 처리
* Spring Data JPA Repository를 이용한 DB 접근
* Thymeleaf를 이용한 서버 데이터 출력
* `@PathVariable`을 이용한 URL 데이터 전달
* `Model`을 이용한 Controller → View 데이터 전달
* `redirect`를 이용한 페이지 이동
* JPA/Hibernate를 이용한 MySQL 연동

---

## 🚀 Future Improvements

기본적인 CRUD 구현을 완료한 후 다음 기능을 추가하며 프로젝트를 발전시킬 예정입니다.

* [ ] 쿠키를 이용한 조회수 중복 증가 방지
* [ ] 잘못된 비밀번호 입력 시 오류 메시지 개선
* [ ] 게시판 UI/UX 개선
* [ ] 검색 기능 추가
* [ ] 페이징 처리
* [ ] 로그인 및 사용자 인증 기능 추가

---

## 🎯 Purpose

처음부터 복잡한 기능을 구현하기보다는
**Spring Boot 웹 애플리케이션의 전체적인 구조와 데이터 흐름을 이해하는 것**을 목표로 시작한 프로젝트입니다.

기본 CRUD를 구현한 이후에도 발생하는 문제를 직접 찾아 수정하고 새로운 기능을 추가하면서 Spring Boot에 대한 이해도를 높이는 것을 목표로 하고 있습니다.
