# 🛵 배달 어플리케이션 - ToAnyone 🛵 
## 🗂️ 프로젝트 개요
- **프로젝트명**: 누구든지(to anyone), 어디서든지 접근 가능한 배달 서비스!
- **프로젝트 기간**: 2025.04.22 ~ 2025.04.29
- **목표**
  - 데이터베이스 연관관계 설정, AOP 기반 로깅, 응답 통일화를 구현했습니다.
  - JUnit·Mockito로 테스트 커버리지 30%를 목표로 했고, GitHub로 체계적인 코드 리뷰와 협업을 진행했습니다.
<br><br>

## 🛠️ 사용 기술 스택
- **🖥️ Back-End**: Java 17, Spring Boot 3, Spring Security, Spring Data JPA
- **🛢️ Database**: MySQL (AWS RDS)
- **⚙️ Build Tool**: Gradle
- **🌿 Version Control**: Git, GitHub
- **📮 API Development Tool**: Postman
<br><br>

## 🌟 팀원 소개
| 이름 | gitHub | 역할 |
|:---|:---|:---|
|김하경 |　[❤️‍🔥](https://github.com/khg9900)|팀장, API 설계, 인증/인가, 고객CRUD, Spring Security, 문서정리&PPT 초안|
|고승표 |　[🐾](https://github.com/KSP0321)|DB설계, 주문 CRUD, 문서정리, AOP 로깅|
|이윤승 |　[💫](https://github.com/younseung-Lee)|API 설계, 리뷰&리뷰 댓글 CRUD, 문서 정리, 시연 영상|
|이은지 |　[🎀](https://github.com/222eunji)|DB설계, 가게 CRUD, 문서 정리, ReadMe 작성|
|임민지 |　[🐣](https://github.com/minjee2758)|와이어프레임 설계, 메뉴 CRUD, 장바구니 CRUD, PPT&발표|

<br>

## 🗺️ ERD
- 🔗[ERD-Wiki로 연결됩니다.](https://github.com/khg9900/ToAnyone/wiki/ERD)
 <br><br> 

## 🖍️ 와이어 프레임
- 🔗 [와이어 프레임-figma로 연결됩니다.](https://embed.figma.com/design/w6r9x9ai97SADdr1Kb9B4k/%EB%82%B4%EB%B0%B0%EC%BA%A0-%EC%8B%AC%ED%99%94-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%99%80%EC%9D%B4%EC%96%B4%ED%94%84%EB%A0%88%EC%9E%84?node-id=0-17&t=9T9CGuRW2T7ecSUa-1&embed-host=notion&footer=false&theme=system)
<br><br>

## 📄 API 문서
- 🔗 [API 문서-Postman으로 연결됩니다.](https://documenter.getpostman.com/view/43159053/2sB2j1hsQt)
<br><br>

## 📂 서비스 상세 기능
### 👤 회원 가입/로그인
- **회원 가입**
  - 권한(일반 유저/사장님), 이름, 닉네임, 이메일, 비밀번호, 전화번호, 주소, 성별, 생년월일을 입력 필요
  - 비밀번호 조건은 대소문자, 숫자, 특수문자를 포함하고 최소 8자리 이상
  - 중복된 닉네임, 이메일, 전화번호 입력 시 예외 처리
   
- **로그인**
  - 이메일과 비밀번호로 로그인하며, 로그인 성공 시 AccessToken과 RefreshToken을 발급
  - Spring Security 기반 JWT 인증/인가 방식을 사용
  - 토큰 재발급(Reissue): 만료된 AccessToken을 RefreshToken으로 검증 후 재발급
    
- **로그아웃**: 저장된 RefreshToken을 삭제하여 로그아웃 처리
    
- **회원 정보 수정**: 닉네임과 주소 수정 가능

- **비밀번호 수정**: 현재 비밀번호 검증 후 새 비밀번호로 변경 가능

- **회원 탈퇴**: 비밀번호 검증 후 Soft Delete 방식으로 회원을 탈퇴 처리 (운영 중인 가게가 있으면 탈퇴 불가)

- **공통 예외 처리**: 유효성 검증 실패, 존재하지 않는 사용자, 권한 문제 등 다양한 상황에 대해 예외 코드를 일관되게 관리
***

### 🏪 가게
- **가게 생성**: 사장님만 생성 가능, 최대 3개까지 운영 가능, 시간/상태/전화번호 포맷 검증 및 다양한 예외 처리

- **가게 조회**
  - **본인 가게 조회**: 사장님만 본인 가게 조회 가능, 폐업된 가게는 제외
  - **키워드 검색**: 키워드 포함 가게명 검색, 사장님과 일반 유저 모두 조회 가능
  - **ID로 단건 조회**: 가게 상세 정보 + 메뉴 리스트까지 조회 가능, 폐업된 가게는 예외 발생

- **가게 수정**: 오픈/마감시간, 배달비, 최소 주문 금액, 공지 수정 가능, 일부만 수정 가능, 폐업된 가게는 수정 불가

- **가게 폐업**: 본인 가게만 폐업 가능, 비밀번호 검증 후 Soft Delete 처리 (deleted = true)
***

### 🍽️ 메뉴
- **메뉴 생성**: 사장님이 본인 가게에 메뉴를 등록 가능

- **메뉴 수정**: 사장님이 본인 가게의 메뉴를 수정 가능

- **메뉴 삭제**: 메뉴를 Soft Delete 방식으로 삭제 가능

- **메뉴 조회**: 메뉴는 가게 조회 시 함께 조회 (단독 조회 불가)

- **공통 예외**
  - 가게가 폐업한 경우 *STORE_SHUT_DOWN*
  - 본인 가게가 아닐 경우 *NOT_STORE_OWNER*
  - 메뉴가 이미 존재할 경우 *MENU_ALREADY_EXISTS*
  - 메뉴가 존재하지 않거나 삭제 상태일 경우 *MENU_NOT_FOUND, *MENU_ALREADY_DELETED*
  - 해당 가게에 속하지 않은 메뉴일 경우 *MENU_IS_NOT_IN_STORE*


***

### 🛒 장바구니
- **장바구니 메뉴 담기**: 한 가게의 여러 메뉴를 장바구니에 추가 가능

- **장바구니 조회**: 로그인한 사용자의 장바구니 조회 가능

- **장바구니 메뉴 빼기**: 장바구니에 담긴 메뉴 수량 조절 가능

- **장바구니 메뉴 비우기**: 장바구니의 모든 메뉴를 한 번에 삭제 가능 (hard delete)

- **공통 예외**
  - 가게가 존재하지 않을 경우 *STORE_NOT_FOUND*
  - 메뉴가 존재하지 않을 경우 *CART_ITEMS_NOT_FOUND*
  - 선택한 가게에 메뉴가 없을 경우 *MENU_IS_NOT_IN_STORE*
  - 메뉴 수량 조정 시 수량 부족할 경우 *CART_ITEM_QUANTITY_UNDERFLOW*
***

### 📱 주문
- **공통 기능**: 주문 생성/상태 변경 시 AOP로 요청 시각, 가게 ID, 주문 ID를 로그로 기

- **주문 생성**: 장바구니 메뉴로 최소 주문 금액 이상 주문을 생성 (상태: WAITING)

- **주문 상태 변경 (사장님)**
  - 사장님은 주문을 수락하고 배달 완료까지 순서대로 상태 변경 가능
  - 주문 상태는 WAITING → COOKING → DONE_COOKING → DELIVERING → DELIVERED 순서

- **가게 주문 목록 조회 (사장님)**: 사장님이 본인 가게의 주문 목록을 조회

- **내 주문 내역 조회 (고객)**: 고객이 본인의 전체 주문 내역을 조회

- **공통 예외**
  - 장바구니가 없거나 가게/메뉴가 존재하지 않는 경우 *CART_NOT_FOUND*, *STORE_NOT_FOUND*, *MENU_NOT_FOUND*
  - 영업 중이 아닌 가게에 주문할 경우 *ORDER_STORE_CLOSED*
  - 최소 주문 금액 미달 시 *ORDER_MIN_PRICE_NOT_MET*
  - 주문 번호가 없거나 상태 변경이 올바르지 않은 경우 *ORDER_NOT_FOUND*, *ORDER_INVALID_STATUS_CHANGE*
  - 사장님 권한이 없거나 가게 주인이 아닐 경우 *ORDER_ACCESS_DENIED_BY_NON_OWNER*
***

### 📃 리뷰
- **리뷰 생성**
  - 고객은 배달 완료(DELIVERED)된 주문에 대해 하나의 리뷰 작성 가능
  - 별점(1~5점)과 10자 이상의 내용을 입력해야 하며, 기본 공개 상태로 작성

- **리뷰 조회**
  - 특정 가게에 작성된 리뷰를 별점 필터링 조건으로 조회 가능
  - soft delete된 리뷰는 조회되지 않고, 결과는 페이징 처리

- **리뷰 수정**: 사용자는 자신이 작성한 리뷰만 수정 가능

- **리뷰 삭제**
  - 사용자는 자신이 작성한 리뷰만 soft delete 방식으로 삭제 가능 
   연결된 사장님 댓글(reply)도 함께 soft delete 처
  
- **공통 예외**
  - 작성한 리뷰가 없거나 본인이 아닌 경우 *REVIEW_ACCESS_DENIED*, *REVIEW_NOT_FOUND*
  - 가게 ID가 일치하지 않을 경우 *REVIEW_STORE_MISMATCH*
  - 이미 삭제된 리뷰를 다시 삭제하려 할 경우 *REVIEW_ALREADY_DELETED*
***

### 💬 리뷰 댓글
- **리뷰 댓글 생성**: 가게 사장님만 리뷰에 댓글 작성 가능 (리뷰당 댓글 하나만 작성 가능)

- **리뷰 댓글 수정**: 사장님이 본인 댓글만 수정 가능

- **리뷰 댓글 삭제**: Soft Delete 방식으로 댓글 삭제 (deleted = true)

- **공통 예외**
  - 리뷰가 존재하지 않을 경우 *REVIEW_NOT_FOUND*
  - 해당 가게의 리뷰가 아닐 경우 *REVIEW_STORE_MISMATCH*
  - 권한이 사장님이 아닐 경우 *REVIEW_ACCESS_DENIED*
  -리뷰 댓글 작성자가 본인이 아닐 경우 *REVIEW_ACCESS_DENIED*
***

## 🔍 SA 문서 및 트러블 슈팅
- 🔗 [SA 문서 및 트러블 슈팅-notion으로 연결됩니다.](https://www.notion.so/Chapter-4-S-A-1dc1e5c01907809d8c88c72ac1592d85#1e31e5c0190780f5b3cacb9ff384a198)
