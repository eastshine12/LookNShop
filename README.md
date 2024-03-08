# LookNShop

- 패션 의류를 구매, 판매 할 수 있는 패션 이커머스 플랫폼 프로젝트
- 커머스 플랫폼의 단순 기능 구현 외에도 동시성 관리, 단위 · 통합 테스트, 인증 및 권한 관리, 클린 아키텍처 등을 고려하며 개발하도록 방향성을 잡음


<br />


## ✅ 사용 기술 및 개발 환경

Java 11, Spring Boot 2.7, IntelliJ, Gradle, MariaDB, Redis, Kafka, Docker

<br />


## ✅ Use Case

### 구매자 유스케이스

- 회원 가입
    - 일반 회원가입, 간편 회원가입으로 플랫폼에 회원으로 등록할 수 있다.
- 회원 등급
    - 총 구매 금액에 따라 등급이 정해진다.
    - `NEWBIE, BRONZE, SILVER, GOLD, PLATINUM, DIAMOND`
- 상품 검색
    - 원하는 상품을 검색하고, 카테고리를 탐색할 수 있다.
- 상품 상세 정보 확인
    - 상품 목록에서 특정 상품을 선택하여 상세 정보와 이미지, 리뷰 등을 확인할 수 있다.
- 위시리스트, Like 기능
    - 마음에 드는 상품을 위시리스트에 저장할 수 있다.
    - 상품이나 리뷰에 좋아요(Like)를 남길 수 있다.
- 장바구니 관리
    - 상품을 장바구니에 추가, 제거하고 수량을 조절할 수 있다.
- 주문 및 결제
    - 장바구니에 담긴 상품을 주문하고 결제할 수 있다.
    - 특정 주문 건을 취소할 수 있다.
- 주문 상태 확인
    - 주문한 상품의 배송 상태를 확인할 수 있다.

### 판매자 유스케이스

- 상품 등록 및 관리
    - 판매자는 쇼핑몰에서 판매할 새로운 상품의 정보를 등록할 수 있다.
    - 상품에 대한 카테고리를 지정, 상품 등록 시 상품명의 중복은 허용하지 않는다.
    - 등록할 상품의 정보(상품명, 상품내용, 가격, 할인율, 썸네일 등)를 입력하고 등록한다.
    - 상품 추가, 수정, 삭제가 가능하다.
- 주문 처리
    - 주문이 들어오면 주문을 확인하고 상품을 준비하여 배송할 수 있다.
    - 배송 단계는 배송준비, 배송중, 배송완료로 구분짓는다.
- 재고 관리
    - 재고 수량을 확인하고 관리할 수 있다.
- 판매 통계 확인
    - 판매 통계 및 성과를 확인할 수 있다.

<br />

## ✅ 프로젝트 구조

```
├─main
│  ├─generated
│  ├─java
│  │  └─com
│  │      └─eastshine
│  │          └─looknshop
│  │              │  LookNShopApplication.java
│  │              │  
│  │              ├─annotation
│  │              │      CurrentUser.java
│  │              │      DistributedLock.java
│  │              │      
│  │              ├─aop
│  │              │      AopForTransaction.java
│  │              │      CustomSpringELParser.java
│  │              │      DistributedLockAop.java
│  │              │      
│  │              ├─config
│  │              │  │  RedissonConfig.java
│  │              │  │  SwaggerConfig.java
│  │              │  │  WebConfig.java
│  │              │  │  WebSecurityConfig.java
│  │              │  │
│  │              │  ├─jwt
│  │              │  │      JwtAccessDeniedHandler.java
│  │              │  │      JwtAuthenticationEntryPoint.java
│  │              │  │      JwtTokenFilter.java
│  │              │  │      JwtTokenProvider.java
│  │              │  │      RefreshTokenRepository.java
│  │              │  │      RefreshTokenService.java
│  │              │  │      TokenService.java
│  │              │  │
│  │              │  └─oauth2
│  │              │          CookieAuthorizationRequestRepository.java
│  │              │          CustomOAuth2UserService.java
│  │              │          GoogleOAuth2User.java
│  │              │          KakaoOAuth2User.java
│  │              │          NaverOAuth2User.java
│  │              │          OAuth2AuthenticationFailureHandler.java
│  │              │          OAuth2AuthenticationSuccessHandler.java
│  │              │          OAuth2UserInfo.java
│  │              │          OAuth2UserInfoFactory.java
│  │              │          UserPrincipal.java
│  │              │
│  │              ├─controller
│  │              │      OrderController.java
│  │              │      ProductController.java
│  │              │      UserController.java
│  │              │
│  │              ├─domain
│  │              │  │  Address.java
│  │              │  │  BaseEntity.java
│  │              │  │  Cart.java
│  │              │  │  Delivery.java
│  │              │  │  Likes.java
│  │              │  │  Order.java
│  │              │  │  OrderItem.java
│  │              │  │  Payment.java
│  │              │  │  RefreshToken.java
│  │              │  │  User.java
│  │              │  │  Wishlist.java
│  │              │  │
│  │              │  └─Product
│  │              │          Product.java
│  │              │          ProductCategory.java
│  │              │          ProductOption.java
│  │              │          Review.java
│  │              │          UploadFile.java
│  │              │
│  │              ├─dto
│  │              │  ├─request
│  │              │  │      OrderCreateRequest.java
│  │              │  │      ProductCreateRequest.java
│  │              │  │      UserCreateRequest.java
│  │              │  │      UserLoginRequest.java
│  │              │  │
│  │              │  └─response
│  │              │          ProductResponse.java
│  │              │          UserLoginResponse.java
│  │              │
│  │              ├─enums
│  │              │      AuthProvider.java
│  │              │      DeliveryStatus.java
│  │              │      Grade.java
│  │              │      OrderStatus.java
│  │              │      PaymentMethod.java
│  │              │      PostType.java
│  │              │      Role.java
│  │              │
│  │              ├─exception
│  │              │  │  GlobalExceptionHandler.java
│  │              │  │
│  │              │  └─custom
│  │              │          DuplicateLoginIdException.java
│  │              │          FileStorageException.java
│  │              │          OutOfStockException.java
│  │              │          PasswordNotMatchedException.java
│  │              │          ProductNotFoundException.java
│  │              │          SoftDeleteFailureException.java
│  │              │          UserNotFoundException.java
│  │              │
│  │              ├─repository
│  │              │      OrderItemRepository.java
│  │              │      OrderRepository.java
│  │              │      ProductRepository.java
│  │              │      UserRepository.java
│  │              │
│  │              ├─resolver
│  │              │      CurrentUserResolver.java
│  │              │
│  │              ├─service
│  │              │      FileStorageService.java
│  │              │      OrderService.java
│  │              │      ProductService.java
│  │              │      UserService.java
│  │              │
│  │              └─util
│  │                      CookieUtils.java
│  │
│  └─resources
│      │  application-oauth.yml
│      │  application.yml
│      │  data.sql
│      │
│      ├─static
│      ├─templates
│      └─upload
└─test
    └─java
        └─com
            └─eastshine
                └─looknshop
                    │  LookNShopApplicationTests.java
                    │
                    ├─controller
                    │      UserControllerTest.java
                    │
                    ├─integration
                    │      OrderServiceIntegrationTest.java
                    │
                    ├─repository
                    │      UserRepositoryTest.java
                    │
                    └─service
                            UserServiceTest.java


```

<br />


## ✅ Architecture


<br />


## ✅ ERD

![LookNShop-ERD](https://github.com/eastshine12/LookNShop/assets/75498839/1a281bc8-0e60-4b2f-bd3d-c738498976e5)

<br />



## ✅ 기술적 이슈와 해결 과정

<br />


## ✅ 화면 구성

<br />

