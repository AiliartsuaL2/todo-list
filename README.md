# 모아이스 과제 테스트

### 이주호

## TO-DO List 서비스

### 서버 접속 링크
- 사용하지않는 도메인과 연결된 프리티어 EC2 인스턴스가 있어 해당 인스턴스에 배포를 진행하였습니다.
- 링크: https://api.simple-closet.shop/views/main

### 기술 스택
- 프레임워크
  - Spring Boot 3.3.0
- 언어
  - Java 17
- JDBC
  - Spring Data JPA
  - Query Dsl 5.0.0
- View
  - Thymeleaf
- Database
  - H2 (Embedded)

## 요구사항

### 비기능 요구사항
- 간단한 설계 문서.
    - API 목록, 코드 구조, 서비스 흐름 또는 추가 고려한 사항 등.
    - 실 서버를 구성했다면, 해당 서버 사용 방법.
- API는 Rest 또는 GraphQL 방식을 이용해서 설계한다.
- 주요 서비스 흐름에 따른 따른 로깅
- 회원은 닉네임을 가진다.
- 보안이 고려되어 있어야 한다.
  - ID UUID화 (Injection 방어)
  - JWT 인증
  - 비밀번호 단방향 암호화
  - 비밀번호 정책

### 기능 요구사항
- API
  - 회원
    - 회원가입
      - 필수 인자(로그인 ID, 비밀번호, 닉네임)가 존재하지 않는 경우 예외가 발생한다.
      - 로그인 ID가 중복되는 경우 예외가 발생한다.
      - 로그인 ID, 닉네임의 입력이 최대 길이가 넘어가는 경우 예외가 발생한다.
      - 비밀번호 정책
        - 최소 8자리 이상의 비밀번호여야 한다.
        - 특수 문자(!, @, #, $, %) 중 하나 이상을 포함해야 한다.
        - 하나 이상의 숫자를 포함해야 한다.
        - 하나 이상의 영어 소문자를 포함해야 한다.
        - 영어 대문자는 허용이지만 필수는 아니다.
    - 로그인
      - 필수 인자(로그인 ID, 비밀번호)가 존재하지 않는 경우 예외가 발생한다. 
      - 로그인 ID에 해당하는 회원이 존재하지 않는 경우 예외가 발생한다.
      - 비밀번호가 일치하지 않는 경우 예외가 발생한다.
    - 회원탈퇴
      - 액세스 토큰이 존재하지 않는 경우 예외가 발생한다.
      - 필수 인자(로그인 ID, 비밀번호)가 존재하지 않는 경우 예외가 발생한다.
      - 토큰의 payload로 조회한 데이터와 입력한 데이터(로그인 ID, 비밀번호)가 일치하지 않는 경우 예외가 발생한다.
  - TODO
    - 추가
      - 액세스 토큰이 존재하지 않는 경우 예외가 발생한다. 
      - 필수 인자(내용)가 존재하지 않는 경우 예외가 발생한다.
      - 내용의 입력이 최대 길이가 넘어가는 경우 예외가 발생한다.
    - 조회
      - Todo List 조회
        - 액세스 토큰이 존재하지 않는 경우 예외가 발생한다.
        - 필수 인자(조회 할 페이지 번호)가 존재하지 않는 경우 예외가 발생한다.
        - TODO가 존재하지 않는 경우 빈 배열이 응답된다.
      - 가장 최근 Todo 상세 조회
        - 액세스 토큰이 존재하지 않는 경우 예외가 발생한다.
        - 최근 TODO가 존재하지 않는 경우 예외가 발생한다.
    - TODO 상태 변경 
      - 상태 정의
        - TODO (할 일)
        - IN PROGRESS (진행 중)
        - DONE (완료)
        - PENDING (대기)
          - 진행 중 상태에서만 대기 상태로 변경될 수 있다.
          - 대기 상태에서는 어떤 상태로든 변경할 수 있다.
      - 예외
        - 액세스 토큰이 존재하지 않는 경우 예외가 발생한다.
        - 필수 인자(할 일 ID, 변경할 상태)가 존재하지 않는 경우 예외가 발생한다.
        - 할 일의 MEMBER_ID와 변경자의 ID가 일치하지 않는 경우 예외가 발생한다.
        - 정의되지 않은 상태로 변경 요청을 하는 경우 예외가 발생한다.
        - 정의된 상태에서 변경 할 수 없는 경우 예외가 발생한다.
- View
  - 회원가입 페이지: /views/sign-up
    - 회원가입
  - 로그인 페이지: /views/sign-in
    - 로그인 
    - 회원가입 페이지 이동
  - 메인 페이지: /views/main
    - TODO 목록 이동
    - 최근 TODO 조회
    - 회원 탈퇴
    - 로그아웃
  - TODO 목록 페이지: /views/todos
    - TODO 생성
    - TODO 목록 조회
      - TODO 수정
    - 메인 페이지 이동

## ERD
![todo-list](./src/main/resources/image/todo-list.png)

## API 명세

| 기능            | path                          | method | status code |
|---------------|--------------------------------|--------|-------------|
| 회원 가입         | /api/v10/members/sign-up     | POST   | 201 CREATED |
| 로그인           | /api/v10/members/sign-in     | POST   | 200 OK      |
| 회원 탈퇴         | /api/v10/members             | DELETE | 200 OK      |
| 액세스 토큰 재발급    | /api/v10/token/refresh    | POST   | 200 OK      |
| TODO 추가       | /api/v10/todos                | POST   | 201 CREATED |
| TODO 목록 조회    | /api/v10/todos               | GET    | 200 OK      |
| 가장 최근 TODO 조회 | /api/v10/todos/recent       | GET    | 200 OK      |
| TODO 상태 변경    | /api/v10/todos/status/{todoId}| PATCH  | 200 OK      |

### 공통 형식
- 요청 형식
```markdown
query parameter: Snake Case 사용
body: Camel Case 사용
```

- 인증이 필요한 요청

```markdown
Header의 Authorization 항목에 Bearer 타입의 Access token을 추가한다.
ex) Authorization: Bearer {accessToken}
```

- 정상 응답
```json
{
    "success": true,
    "result": { 
      result
    }
}
```
- 예외 응답
```json
{
    "success": false,
    "result": { 
      "errorMessage": "예외 메세지"
    }
}
```

- *는 필수 필드 입니다.
- 회원가입 API
  - Request
    ```markdown
    method: POST

    path: /api/v10/members/sign-up

    body
      - *loginId: (String) 로그인시 사용할 ID
      - *password: (String) 로그인시 사용할 비밀번호
      - *nickname: (String) 서비스에서 사용할 닉네임
    ```
    
  - Response
    - Http Status
      - 201 CREATED
    - Body
      ```json
      {
        "success": true,
        "result": {
          "message": "회원 가입에 성공하였습니다."
        }
      }
      ```
- 로그인 API
  - Request
    ```markdown
    method: POST

    path: /api/v10/members/sign-in

    body
      - *loginId: (String) 로그인 ID
      - *password: (String) 비밀번호
    ```

  - Response
    - Http Status
      - 200 OK
    - Body
      - *memberId: (String) 회원의 ID 입니다. 
      - *token
        - *accessToken: (String, header.payload.signature) 액세스 토큰 입니다.
        - *refreshToken: (String, header.payload.signature) 리프레시 토큰 입니다.
      - ex
        ```json
        {
          "success": true,
          "result": {
            "memberId": "memberId",
            "token": {
              "accessToken": "accessToken", 
              "refreshToken": "refreshToken"
            }
          }
        }
        ```
- 회원탈퇴 API
  - Request
    ```markdown
    method: DELETE
  
    path: /api/v10/members
    
    *header:
      - *Authorization: Bearer {accessToken}
  
    body
      - *loginId: (String) 로그인 ID
      - *password: (String) 비밀번호
    ```

  - Response
    - Http Status
      - 200 OK
    - Body
      ```json
      {
        "success": true,
        "result": {
          "message": "회원 탈퇴가 정상적으로 처리되었습니다."
        }
      }
      ```
- 액세스 토큰 재발급 API
  - Request
    ```markdown
    method: POST
  
    path: /api/v10/token/refresh
    
    body:
      - *refreshToken: (String) 로그인시 발급 받은 리프레시 토큰
    ```

  - Response
    - Http Status
      - 200 OK
    - Body
      - *accessToken: (String, header.payload.signature) 액세스 토큰 입니다.
      - ex 
        ```json
        {
          "success": true,
          "result": {
            "accessToken": "{accessToken}"
          }
        }
        ```
- TODO 추가 API
  - Request
    ```markdown
    method: POST
  
    path: /api/v10/todos
    
    *header:
      - *Authorization: Bearer {accessToken}
  
    body
      - *content: (String) 할 일의 내용
    ```

  - Response
    - Http Status
      - 201 CREATED
    - Body
      ```json
      {
        "success": true,
        "result": {
          "message": "할 일이 정상적으로 등록되었습니다."
        }
      }
      ```
- TODO 리스트 조회 API
  - Request
    ```markdown
    method: GET
  
    path: /api/v10/todos

    *header:
      - *Authorization: Bearer {accessToken}
  
    query parameter
      - *page_no: 조회 할 페이지 번호
    ```

  - Response
    - Http Status
      - 200 OK
    - Body
      - *count: 해당 회원이 작성한 TODO의 총 개수입니다.
      - *todos
        - *todoId: (String) TODO의 ID 입니다.
        - *content: (String) TODO의 내용 입니다.
        - *status: (String, { 할 일 | 진행 중 | 완료 | 대기 }) TODO의 상태 입니다.
        - *createdAt: (String, yyyy-MM-dd hh:mm) TODO의 생성일시 입니다.
        - updatedAt: (String, yyyy-MM-dd hh:mm) TODO의 수정일시 입니다.
      - ex
        ```json
        {
          "success": true,
          "result": {
            "count": 123,
            "todos": [
              {
                "todoId": "{todoID}", 
                "content": "{content}",
                "status": "{status}", 
                "createdAt": "{createdAt}", 
                "updatedAt": "{updatedAt}" 
              },
              {
                "todoId": "{todoID}",
                "content": "{content}",
                "status": "{status}",
                "createdAt": "{createdAt}",
                "updatedAt": "{updatedAt}"
              }, 
              ...
              {
                "todoId": "{todoID}",
                "content": "{content}",
                "status": "{status}",
                "createdAt": "{createdAt}", 
                "updatedAt": "{updatedAt}" 
              }
            ] 
          }
        }
        ```
- 가장 최근 TODO 조회 API
  - Request
    ```markdown
    method: GET
  
    path: /api/v10/todos/recent
  
    *header:
      - *Authorization: Bearer {accessToken}
    ```

  - Response
    - Http Status
      - 200 OK
    - Body
      - *todoId: (String) TODO의 ID 입니다.
      - *content: (String) TODO의 내용 입니다.
      - *status: (String, { 할 일 | 진행 중 | 완료 | 대기 }) TODO의 상태 입니다.
      - *createdAt: (String, yyyy-MM-dd hh:mm) TODO의 생성일시 입니다.
      - updatedAt: (String, yyyy-MM-dd hh:mm) TODO의 수정일시 입니다. 
      - ex
        ```json
        {
          "success": true,
          "result": {
              "todoId": "{todoID}",
              "content": "{content}",
              "status": "{status}",
              "createdAt": "{createdAt}",
              "updatedAt": "{updatedAt}"
          }
        }
        ```
- TODO 상태 변경 API
  - Request
    ```markdown
    method: PATCH
  
    path: /api/v10/todos/status/{todoId}
      - *todoId: 변경할 할 일의 ID
         
    *header:
      - Authorization: Bearer {accessToken}
  
    body
      - *status: (String) 변경할 할 일의 상태
    ```

  - Response
    - Http Status
      - 200 OK
    - Body
      ```json
      {
        "success": true,
        "result": {
          "message": "할 일의 상태가 정상적으로 변경되었습니다."
        }
      }
      ```
      
## 추가사항

### 서비스 흐름도
- 회원가입
  - ![회원가입.png](src%2Fmain%2Fresources%2Fimage%2F%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85.png)
- 로그인
  - ![로그인.png](src%2Fmain%2Fresources%2Fimage%2F%EB%A1%9C%EA%B7%B8%EC%9D%B8.png)
- 액세스 토큰 재발급
  - ![액세스 토큰 재발급.png](src%2Fmain%2Fresources%2Fimage%2F%EC%95%A1%EC%84%B8%EC%8A%A4%20%ED%86%A0%ED%81%B0%20%EC%9E%AC%EB%B0%9C%EA%B8%89.png)
- 회원 탈퇴
  - ![회원 탈퇴.png](src%2Fmain%2Fresources%2Fimage%2F%ED%9A%8C%EC%9B%90%20%ED%83%88%ED%87%B4.png)
- TODO 생성
  - ![TODO 생성.png](src%2Fmain%2Fresources%2Fimage%2FTODO%20%EC%83%9D%EC%84%B1.png)
- 최근 TODO 조회
  - ![최근 TODO 조회.png](src%2Fmain%2Fresources%2Fimage%2F%EC%B5%9C%EA%B7%BC%20TODO%20%EC%A1%B0%ED%9A%8C.png)
- TODO 리스트 조회
  - ![TODO 리스트 조회.png](src%2Fmain%2Fresources%2Fimage%2FTODO%20%EB%A6%AC%EC%8A%A4%ED%8A%B8%20%EC%A1%B0%ED%9A%8C.png)
- TODO 상태 수정
  - ![TODO 상태 수정.png](src%2Fmain%2Fresources%2Fimage%2FTODO%20%EC%83%81%ED%83%9C%20%EC%88%98%EC%A0%95.png)

### 코드 구조
```markdown
src
├── main
│   ├── generated
│   │   └── moais
│   │       └── todolist
│   │           ├── global
│   │           │   ├── auth
│   │           │   │   └── domain
│   │           │   └── domain
│   │           ├── member
│   │           │   └── domain
│   │           └── todo
│   │               └── domain
│   ├── java
│   │   └── moais
│   │       └── todolist
│   │           ├── global
│   │           │   ├── annotation
│   │           │   ├── aop
│   │           │   ├── auth
│   │           │   │   ├── application
│   │           │   │   │   ├── provider
│   │           │   │   │   └── usecase
│   │           │   │   ├── domain
│   │           │   │   ├── persistence
│   │           │   │   └── presentation
│   │           │   │       └── dto
│   │           │   │           ├── request
│   │           │   │           └── response
│   │           │   ├── config
│   │           │   ├── domain
│   │           │   │   └── converter
│   │           │   ├── dto
│   │           │   ├── exception
│   │           │   └── handler
│   │           ├── member
│   │           │   ├── application
│   │           │   │   ├── dto
│   │           │   │   │   ├── request
│   │           │   │   │   └── response
│   │           │   │   └── usecase
│   │           │   ├── domain
│   │           │   ├── exception
│   │           │   ├── persistence
│   │           │   ├── presentation
│   │           │   └── utils
│   │           ├── todo
│   │           │   ├── application
│   │           │   │   ├── dto
│   │           │   │   │   ├── request
│   │           │   │   │   └── response
│   │           │   │   └── usecase
│   │           │   ├── domain
│   │           │   │   └── converter
│   │           │   ├── exception
│   │           │   ├── persistence
│   │           │   ├── presentation
│   │           │   └── utils
│   │           └── views
│   └── resources
│       ├── image
│       └── templates
│           └── views
└── test
└── java
└── moais
└── todolist
├── global
│   ├── auth
│   │   ├── application
│   │   │   └── provider
│   │   ├── persistence
│   │   └── presentation
│   └── config
├── member
│   ├── application
│   │   └── dto
│   │       └── request
│   ├── domain
│   ├── persistence
│   └── presentation
├── testconfig
└── todo
├── application
│   └── dto
│       ├── request
│       └── response
├── domain
├── persistence
└── presentation
```

### 프로젝트 아키텍처
- 프로젝트 아키텍처는 클린 아키텍처를 차용하였습니다.
  - 기능 단위는 UseCase로 분류하였고, 각 도메인별 Service는 UseCase를 구현하는 구현체로 사용되었습니다.
- 시간 제약으로 인하여 Out에 대한 Port And Adapter 패턴은 생략하였습니다.
- JPA의 장점(Dirty Checking, 1차 캐시, Auditing 등)을 살리기 위해 Domain과 Entity를 구분하지 않고 같은 클래스로 관리 하였습니다.
- 각 도메인간의 의존성을 줄이기 위해 이벤트 기반 설계를 하였으며 , 엔티티간 연관관계를 맺지 않았습니다.

### 기타 고려 사항
- Todo List Service를 사용 할 수 있도록 서버 배포 및 사용자 화면 간단하게 구성하였습니다.
- Semantic Versioning을 통해 API 버전에 대한 확장성을 고려하였습니다.
- 비즈니스 고려사항
  - 최근 TODO 조회라는 말이 모호하여 UX를 고려하였을 때 최근 TODO는 변경(생성, 수정)이 가장 최근 시점에 일어난 TODO라고 생각했기 때문에 수정 일자와 생성 일자를 비교하여 가장 최근 변경되거나 생성된 TODO를 노출시키도록 결정하였습니다.
  - TODO 목록의 정렬조건 또한 변경이 가장 최근 시점에 일어난 순서대로 정렬을 진행하였습니다.
  - 회원 가입, 탈퇴의 경우 이벤트가 발행이되고, 그에 따른 추가적인 트랜잭션 작업이 이루어지기 때문에 트랜잭션 전파를 위해 `@TransactionalEventListener`를 사용하였습니다.
- `Spring AOP`를 이용하여 주요 레이어별 접근에 대한 로깅을 처리하였습니다.
  - 추가 기능에 대한 확장성을 고려하여 내부 클래스를 이용하여 Advisor를 구현하였습니다.
  - 유지보수를 고려하여 Pointcut을 한 곳에서 관리하여 호출하는 방식을 사용하였습니다.
- 단위 테스트
  - 총 163개의 단위 테스트를 작성하였고, 82%의 구문 커버리지를 달성하였습니다. (QClass, Config, ViewRouter 등을 제거하면 더 높을 것으로 기대)
    - ![테스트 커버리지.png](src%2Fmain%2Fresources%2Fimage%2F%ED%85%8C%EC%8A%A4%ED%8A%B8%20%EC%BB%A4%EB%B2%84%EB%A6%AC%EC%A7%80.png)
    - ![테스트 결과.png](src%2Fmain%2Fresources%2Fimage%2F%ED%85%8C%EC%8A%A4%ED%8A%B8%20%EA%B2%B0%EA%B3%BC.png)
  - 레이어별로 책임을 분리하여 책임에 대한 검증 테스트를 진행하였습니다.
    - Domain (단위 테스트)
      - 입력 데이터 검증
      - 도메인 내부의 비즈니스 로직
    - Application (단위 테스트)
      - 통합된 비즈니스 로직 검증
    - DTO (단위 테스트)
      - 입력 데이터 검증
    - Persistence (통합 테스트)
      - DB 접근 로직 검증
    - API (부분 통합 테스트)
      - Request, Response에 대한 데이터 검증
      - HttpMethod, HttpStatus에 대한 검증
- 보안
  - Spring Security와 Jwt를 이용하여 인증 로직을 구현하였습니다.
    - 요청 헤더를 통한 토큰 인증 방식
    - 토큰 유효성 검사를 통한 부적절한 접근 방지
    - JWT 관련 예외 처리 세분화
  - BCrypt 암호화를 통해 회원의 비밀번호를 해싱하여 저장하였습니다.
  - 각 Entity별 ID를 Auto Increment가 아닌, UUID를 활용하여 Injection에 대한 안정성을 높혔습니다.
  - yml 파일을 통해 서버내 secret key등 중요한 데이터를 Property를 통해 관리하게끔 하였고, Profile을 활용하여 운영 환경에 맞는 파일을 설정하도록 구성하였습니다.
