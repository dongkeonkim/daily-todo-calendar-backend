# Todo Calendar Backend

Todo Calendar의 백엔드 서버 저장소입니다.

## 관련 저장소
이 프로젝트는 Frontend와 Backend 저장소로 분리되어 있습니다:

- Backend Repository (현재 저장소)
  - 기술 스택: Spring Boot, MySQL, JWT
  - API 서버 및 데이터베이스 관리

- [Frontend Repository](https://github.com/dongkeonkim/daily-todo-calendar-frontend)
  - 기술 스택: React
  - 사용자 인터페이스 및 클라이언트 로직
  - [프론트엔드 기능 살펴보기](https://github.com/dongkeonkim/daily-todo-calendar-frontend/blob/main/README.md)

## Frontend 미리보기
Frontend 애플리케이션의 주요 기능을 아래에서 확인하실 수 있습니다:

### 일정 관리
#### 일정 추가
<img src="https://raw.githubusercontent.com/dongkeonkim/daily-todo-calendar-frontend/main/public/images/check-todo.png" alt="일정 추가 화면" width="600"/>

#### 메인 캘린더
<img src="https://raw.githubusercontent.com/dongkeonkim/daily-todo-calendar-frontend/main/public/images/added-todo.png" alt="캘린더 메인 화면" width="600"/>

## Backend 기술 스택
- 프레임워크 및 ORM: Spring Boot, Spring Data JPA
- 데이터베이스: MySQL, Redis
- 인증: JWT, SSL/TLS 지원 (운영 환경)