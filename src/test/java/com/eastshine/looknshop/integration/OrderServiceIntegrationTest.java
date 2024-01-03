package com.eastshine.looknshop.integration;

import com.eastshine.looknshop.domain.Order;
import com.eastshine.looknshop.domain.OrderItem;
import com.eastshine.looknshop.domain.Product.Product;
import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.OrderCreateRequest;
import com.eastshine.looknshop.dto.request.UserCreateRequest;
import com.eastshine.looknshop.repository.OrderRepository;
import com.eastshine.looknshop.repository.ProductRepository;
import com.eastshine.looknshop.service.OrderService;
import com.eastshine.looknshop.service.ProductService;
import com.eastshine.looknshop.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
public class OrderServiceIntegrationTest {

    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;



    @Test
    @DisplayName("정상적인 상품 주문에 성공한다. ")
    public void 상품_주문() throws Exception {
        // given

        /* 주문 계정 생성 */
        Long userId = userService.join(new UserCreateRequest("testId", "1234", "이름", "닉네임", "a@a.com", "010-1234-5678"));
        User user = userService.findUserById(userId);

        /* 주문 상품 생성 */
        Long productId = productRepository.save(Product.builder()
                .user(user)
                .title("상품명1")
                .content("내용")
                .price(1000)
                .discountRate(10)
                .totalStock(10)
                .build()).getId();

        List<OrderCreateRequest> list = Collections.singletonList(new OrderCreateRequest(productId, 1));

        // when
        Long orderId = orderService.createOrder(user, list);

        // then
        Order order = orderRepository.findById(orderId).orElseThrow();
        assertThat(orderId).isNotNull();
        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getUser()).isEqualTo(user);
        assertThat(order.getOrderItems().get(0).getProduct().getTitle()).isEqualTo("상품명1");
        Product product = productRepository.findById(productId).get();
        assertThat(product.getTotalStock()).isEqualTo(9);

    }


    @Test
    @DisplayName("재고 100개의 상품을 동시 주문하여 재고가 0개 남는다.")
    public void 재고_동시성_테스트() throws InterruptedException, IOException {
        // given
        Long userId = userService.join(new UserCreateRequest("testId", "1234", "이름", "닉네임", "a@a.com", "010-1234-5678"));
        User user = userService.findUserById(userId);

        Product product = productRepository.save(Product.builder()
                        .user(user)
                        .title("상품명1")
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
        Product productResult = productRepository.findById(product.getId()).get();
        assertThat(productResult.getTotalStock()).isEqualTo(0);
    }


    public static MultipartFile createMockImageMultipartFile(String prefix) throws IOException {
        // 임시 파일 생성
        Path tempFile = Files.createTempFile(prefix, ".jpg");

        Files.write(tempFile, "Mock image data".getBytes());

        return new MockMultipartFile(
                "file",
                tempFile.getFileName().toString(),
                "image/jpeg",
                Files.readAllBytes(tempFile)
        );
    }

}
