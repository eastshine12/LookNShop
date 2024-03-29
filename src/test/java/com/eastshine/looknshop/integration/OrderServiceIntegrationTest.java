package com.eastshine.looknshop.integration;

import com.eastshine.looknshop.domain.Order;
import com.eastshine.looknshop.domain.OrderItem;
import com.eastshine.looknshop.domain.Product.Product;
import com.eastshine.looknshop.domain.Product.ProductCategory;
import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.OrderCreateRequest;
import com.eastshine.looknshop.dto.request.UserCreateRequest;
import com.eastshine.looknshop.repository.OrderRepository;
import com.eastshine.looknshop.repository.ProductCategoryRepository;
import com.eastshine.looknshop.repository.ProductRepository;
import com.eastshine.looknshop.service.OrderService;
import com.eastshine.looknshop.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class OrderServiceIntegrationTest {

    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductCategoryRepository productCategoryRepository;


    @Transactional
    @Test
    @DisplayName("정상적인 상품 주문에 성공한다. ")
    public void 상품_주문() throws Exception {
        // given

        /* 주문 계정 생성 */
        Long userId = userService.join(new UserCreateRequest("testId", "1234", "이름", "닉네임", "a@a.com", "010-1234-5678"));
        User user = userService.findUserById(userId);
        ProductCategory productCategory = productCategoryRepository.save(ProductCategory.builder().id(1L).name("하의").build());

        /* 주문 상품 생성 */
        Long productId = productRepository.save(Product.builder()
                .user(user)
                .category(productCategory)
                .title("상품명1")
                .content("내용")
                .price(1000)
                .discountRate(10)
                .totalStock(10)
                .build()).getId();

        List<OrderCreateRequest> list = Collections.singletonList(new OrderCreateRequest(productId, null, 1));

        // when
        Long orderId = orderService.createOrder(user, list);

        // then
        Order order = orderRepository.findById(orderId).orElseThrow();
        assertThat(orderId).isNotNull();
        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getOrderItems().get(0).getProduct().getTitle()).isEqualTo("상품명1");
        Product findProduct = productRepository.findById(productId).get();
        assertThat(findProduct.getTotalStock()).isEqualTo(9);

    }


    @Test
    @Transactional
    @DisplayName("재고 100개의 상품을 동시 주문하여 재고가 0개 남는다.")
    public void 재고_동시성_테스트() throws InterruptedException {
        // given
        Long userId = userService.join(new UserCreateRequest("testId2", "1234", "이름", "닉네임", "a@a.com", "010-1234-5678"));
        User user = userService.findUserById(userId);
        ProductCategory productCategory = productCategoryRepository.save(ProductCategory.builder().id(1L).name("하의").build());

        Product product = productRepository.save(Product.builder()
                .user(user)
                .category(productCategory)
                .title("상품명2")
                .content("내용")
                .price(1000)
                .discountRate(10)
                .totalStock(100)
                .build());

        final int THREAD_COUNT = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // when
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    List<OrderItem> list = Collections.singletonList(OrderItem.builder().product(product).quantity(1).build());
                    orderService.decreaseProductStock(list);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Product productResult = productRepository.findById(product.getId()).orElseThrow();
        assertThat(productResult.getTotalStock()).isEqualTo(0);
    }

    @Test
    @DisplayName("(PessimisticLock)재고 100개의 상품을 동시 주문하여 재고가 0개 남는다.")
    public void 비관적락_테스트() throws InterruptedException {
        // given
        Long userId = userService.join(new UserCreateRequest("testId3", "1234", "이름", "닉네임", "a@a.com", "010-1234-5678"));
        User user = userService.findUserById(userId);
        ProductCategory productCategory = productCategoryRepository.save(ProductCategory.builder().id(1L).name("하의").build());

        Product product = productRepository.save(Product.builder()
                .user(user)
                .category(productCategory)
                .title("상품명3")
                .content("내용")
                .price(1000)
                .discountRate(10)
                .totalStock(100)
                .build());

        final int THREAD_COUNT = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // when
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    orderService.decreaseProductStockWithPessimisticLock(product.getId(), 1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Product productResult = productRepository.findById(product.getId()).orElseThrow();
        assertThat(productResult.getTotalStock()).isEqualTo(0);


    }

    @Test
    @DisplayName("(RedissonLock)재고 100개의 상품을 동시 주문하여 재고가 0개 남는다.")
    public void redisson_테스트() throws InterruptedException {
        // given
        Long userId = userService.join(new UserCreateRequest("testId4", "1234", "이름", "닉네임", "a@a.com", "010-1234-5678"));
        User user = userService.findUserById(userId);
        ProductCategory productCategory = productCategoryRepository.save(ProductCategory.builder().id(1L).name("하의").build());

        Product product = productRepository.save(Product.builder()
                .user(user)
                .category(productCategory)
                .title("상품명4")
                .content("내용")
                .price(1000)
                .discountRate(10)
                .totalStock(100)
                .build());

        final int THREAD_COUNT = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // when
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    orderService.decreaseProductStockWithRedissonLock(product.getId(), 1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Product productResult = productRepository.findById(product.getId()).orElseThrow();
        assertThat(productResult.getTotalStock()).isZero();


    }

}
