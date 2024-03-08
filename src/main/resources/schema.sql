SET foreign_key_checks = 0;

DROP TABLE IF EXISTS users;
CREATE TABLE `users` (
                     id bigint(20) NOT NULL AUTO_INCREMENT,
                     login_id varchar(50) NOT NULL COMMENT '로그인ID',
                     password varchar(100) NOT NULL COMMENT '비밀번호',
                     name varchar(100) DEFAULT NULL COMMENT '이름',
                     nickname varchar(100) DEFAULT NULL COMMENT '닉네임',
                     email varchar(100) NOT NULL COMMENT '이메일',
                     phone varchar(20) DEFAULT NULL COMMENT '휴대폰번호',
                     auth_provider varchar(50) DEFAULT NULL COMMENT '인증공급자',
                     grade varchar(10) NOT NULL COMMENT '등급',
                     oauth2id varchar(50) DEFAULT NULL COMMENT 'OAuth2 ID',
                     `role` varchar(20) NOT NULL COMMENT '권한',
                     created_at datetime(6) DEFAULT NULL COMMENT '생성일',
                     updated_at datetime(6) DEFAULT NULL COMMENT '수정일',
                     is_deleted tinyint(1) NOT NULL COMMENT '삭제여부',
                     deleted_at datetime(6) DEFAULT NULL COMMENT '삭제일',
                     PRIMARY KEY (id),
                     UNIQUE (login_id),
                     UNIQUE (email)
);


DROP TABLE IF EXISTS address;
CREATE TABLE `address` (
                       address_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '주소 ID',
                       user_id BIGINT(20) NOT NULL COMMENT '회원 ID',
                       name VARCHAR(255) COMMENT '주소 이름',
                       postal_code VARCHAR(20) COMMENT '우편번호',
                       main_address VARCHAR(255) COMMENT '기본 주소',
                       detail_address VARCHAR(255) COMMENT '상세주소',
                       phone VARCHAR(20) COMMENT '받는분 전화번호',
                       delivery_request VARCHAR(255) COMMENT '배송요청',
                       is_default tinyint(1) COMMENT '기본주소지 여부',
                       is_deleted tinyint(1) COMMENT '삭제여부',
                       deleted_at DATETIME COMMENT '삭제일',
                       PRIMARY KEY (address_id),
                       FOREIGN KEY (user_id) REFERENCES users(id)
);


DROP TABLE IF EXISTS product_category;
CREATE TABLE product_category (
                      category_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '카테고리 ID',
                      name VARCHAR(255) COMMENT '카테고리명',
                      PRIMARY KEY (category_id)
);


DROP TABLE IF EXISTS product;
CREATE TABLE product (
                     product_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '상품 ID',
                     partner_id BIGINT(20) NOT NULL COMMENT '회원 ID',
                     category_id BIGINT(20) NOT NULL COMMENT '카테고리 ID',
                     title VARCHAR(255) COMMENT '제목',
                     content VARCHAR(255) COMMENT '내용',
                     thumbnail1 VARCHAR(255) COMMENT '썸네일1',
                     thumbnail2 VARCHAR(255) COMMENT '썸네일2',
                     price INT COMMENT '가격',
                     discount_rate INT COMMENT '할인율',
                     total_stock INT COMMENT '총 재고',
                     created_at DATETIME COMMENT '생성일',
                     updated_at DATETIME COMMENT '수정일',
                     deleted_at DATETIME COMMENT '삭제일',
                     is_deleted TINYINT(1) COMMENT '삭제여부',
                     PRIMARY KEY (product_id),
                     FOREIGN KEY (partner_id) REFERENCES users(id),
                     FOREIGN KEY (category_id) REFERENCES product_category(category_id)
);


DROP TABLE IF EXISTS product_option;
CREATE TABLE product_option (
                    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '상품옵션 ID',
                    product_id BIGINT(20) NOT NULL COMMENT '상품 ID',
                    name VARCHAR(255) COMMENT '옵션명',
                    value VARCHAR(255) COMMENT '옵션값',
                    price INT COMMENT '옵션가격',
                    stock_quantity INT COMMENT '재고',
                    PRIMARY KEY (id),
                    FOREIGN KEY (product_id) REFERENCES product(product_id)
);


DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
                    order_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '주문 ID',
                    user_id BIGINT(20) NOT NULL COMMENT '회원 ID',
                    order_date DATETIME COMMENT '주문시간',
                    order_status VARCHAR(255) COMMENT '주문상태',
                    order_cancel_reason VARCHAR(255) COMMENT '주문취소사유',
                    cancelled_time DATETIME COMMENT '주문취소시간',
                    created_at DATETIME COMMENT '생성일',
                    updated_at DATETIME COMMENT '수정일',
                    PRIMARY KEY (order_id),
                    FOREIGN KEY (user_id) REFERENCES users(id)
);


DROP TABLE IF EXISTS order_item;
CREATE TABLE order_item (
                    order_item_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '주문상품 ID',
                    product_id BIGINT(20) NOT NULL COMMENT '상품 ID',
                    order_id BIGINT(20) NOT NULL COMMENT '주문 ID',
                    quantity INT COMMENT '개수',
                    order_price INT COMMENT '구매가격',
                    PRIMARY KEY (order_item_id),
                    FOREIGN KEY (product_id) REFERENCES product(product_id),
                    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);


DROP TABLE IF EXISTS payment;
CREATE TABLE payment (
                     payment_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '결제 ID',
                     order_id BIGINT(20) NOT NULL COMMENT '주문 ID',
                     payment_method VARCHAR(255) COMMENT '결제 방식',
                     amount INT COMMENT '결제 금액',
                     payment_time DATETIME COMMENT '결제 시간',
                     PRIMARY KEY (payment_id),
                     FOREIGN KEY (order_id) REFERENCES orders(order_id)
);


DROP TABLE IF EXISTS delivery;
CREATE TABLE delivery (
                      delivery_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '배송 ID',
                      order_id BIGINT(20) NOT NULL COMMENT '주문 ID',
                      address_id BIGINT(20) NOT NULL COMMENT '주소 ID',
                      delivery_start_time DATETIME COMMENT '배송 시작 시간',
                      delivery_completed_time DATETIME COMMENT '배송 완료 시간',
                      delivery_status VARCHAR(255) COMMENT '배송상태',
                      PRIMARY KEY (delivery_id),
                      FOREIGN KEY (order_id) REFERENCES orders(order_id),
                      FOREIGN KEY (address_id) REFERENCES address(address_id)
);


DROP TABLE IF EXISTS cart;
CREATE TABLE cart (
                      cart_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '장바구니 ID',
                      user_id BIGINT(20) NOT NULL COMMENT '회원 ID',
                      product_id BIGINT(20) NOT NULL COMMENT '상품 ID',
                      product_option_id BIGINT(20) NOT NULL COMMENT '상품옵션 ID',
                      quantity INT COMMENT '수량',
                      PRIMARY KEY (cart_id),
                      FOREIGN KEY (user_id) REFERENCES users(id),
                      FOREIGN KEY (product_id) REFERENCES product(product_id),
                      FOREIGN KEY (product_option_id) REFERENCES product_option(id)
);


DROP TABLE IF EXISTS wishlist;
CREATE TABLE wishlist (
                      wishlist_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '위시리스트 ID',
                      user_id BIGINT(20) NOT NULL COMMENT '회원 ID',
                      product_id BIGINT(20) NOT NULL COMMENT '상품 ID',
                      created_at DATETIME COMMENT '생성일',
                      updated_at DATETIME COMMENT '수정일',
                      is_deleted BOOLEAN COMMENT '삭제여부',
                      deleted_at DATETIME COMMENT '삭제일',
                      PRIMARY KEY (wishlist_id),
                      FOREIGN KEY (user_id) REFERENCES users(id),
                      FOREIGN KEY (product_id) REFERENCES product(product_id)
);


DROP TABLE IF EXISTS likes;
CREATE TABLE likes (
                       like_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '라이크 ID',
                       user_id BIGINT(20) NOT NULL COMMENT '회원 ID',
                       post_type VARCHAR(255) COMMENT '대상 타입',
                       target_id BIGINT COMMENT '대상 ID',
                       liked_at DATETIME COMMENT '생성일',
                       PRIMARY KEY (like_id),
                       FOREIGN KEY (user_id) REFERENCES users(id)
);


DROP TABLE IF EXISTS refresh_token;
CREATE TABLE refresh_token (
                       id BIGINT auto_increment NOT NULL,
                       user_id BIGINT NOT NULL,
                       refresh_token varchar(255) NOT NULL,
                       CONSTRAINT refresh_token_pk PRIMARY KEY (id),
                       CONSTRAINT refresh_token_FK FOREIGN KEY (user_id) REFERENCES users(id)
);


SET foreign_key_checks = 1;