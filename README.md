# Todo Calendar Backend

> Todo Calendar의 백엔드 서버 저장소입니다.

## 📚 목차
- [관련 저장소](#관련-저장소)
- [기술 스택](#기술-스택)
- [프론트엔드 미리보기](#프론트엔드-미리보기)

## 🔗 관련 저장소
이 프로젝트는 Frontend와 Backend 저장소로 분리되어 있습니다:

- **Backend Repository** (현재 저장소)
- [**Frontend Repository**](https://github.com/dongkeonkim/daily-todo-calendar-frontend)
  - 기술 스택: React
  - 사용자 인터페이스 및 클라이언트 로직
  - [프론트엔드 기능 살펴보기](https://github.com/dongkeonkim/daily-todo-calendar-frontend/blob/main/README.md)

## 🛠️ 기술 스택

### 💻 핵심 프레임워크 및 언어
- **Java 17**
- **Spring Boot 3.2.x**
- **Spring Data JPA**
- **Spring Security**

### 🏗️ 아키텍처
- **헥사고날 아키텍처(Ports & Adapters)**
  - `adapter` - 외부 시스템과의 통신
  - `application` - 유스케이스 및 서비스 계층
  - `domain` - 핵심 비즈니스 로직 및 엔티티
  - `infrastructure` - 기술적 구현 세부사항
- **DDD(Domain-Driven Design)**

### 💾 데이터베이스 및 쿼리 기술
- **PostgreSQL**
- **QueryDSL 5.0**
- **Hibernate**

### 🔐 인증 및 보안
- **JWT (JSON Web Token)**
- **Spring OAuth2 Client**
- **Spring Security**

### 📝 API 문서화 및 개발 도구
- **SpringDoc OpenAPI (Swagger)** - API 문서 자동화

### 📊 모니터링 및 운영
- **Spring Actuator**

### 🔧 개발 환경 및 도구
- **Gradle**
- **Docker & Docker Compose**
- **Spotless** - 코드 스타일 및 포맷팅 자동화
- **Git Hooks** - 커밋 전 코드 스타일 검사

### ☁️ 클라우드 및 인프라
- **Google Cloud SQL**
- **Google Cloud Platform**
- **Google Run** - 서버리스 컨테이너 배포 환경

## 🖼️ 프론트엔드 미리보기
### 일정 관리
#### 메인 화면
<img src="https://raw.githubusercontent.com/dongkeonkim/daily-todo-calendar-frontend/main/public/images/main_dark.png" alt="캘린더 메인 화면" width="600"/>