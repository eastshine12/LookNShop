INSERT INTO users (login_id, password, name, email,  grade, role, is_deleted, deleted_at, created_at, updated_at)
VALUES ('test1', '$2a$12$rdtc5AkEtv1iIXVS9Mg/1e7YkvAWyJ8EWRk5kDng96BdRjmbPk49S', '홍길동', 'test@example.com', 'NEWBIE', 'ADMIN', false, null, now(), now());

insert
into
    product
(created_at, updated_at, content, deleted_at, discount_rate, is_deleted, partner_id, price, thumbnail1, thumbnail2, title, total_stock)
values
    ('2023-12-31', '2023-12-31', '내용', null, 10, false, 1, 89000, '4747302d-d5ff-48d6-b80b-dfac4f7e837b.jpg', null, '스트레치 기모 조거팬츠 블랙', 50);

insert
into
    product
(created_at, updated_at, content, deleted_at, discount_rate, is_deleted, partner_id, price, thumbnail1, thumbnail2, title, total_stock)
values
    ('2023-12-31', '2023-12-31', '내용2', null, 15, false, 1, 199000, '1c05aa3f-2d09-433d-8f95-87c68471f2ed.jpg', '5bc9b74a-2280-4edd-90d7-5db26cb3793d.jpg', '커뮤터 스트레치 라이트 롱 패딩 그레이', 100);