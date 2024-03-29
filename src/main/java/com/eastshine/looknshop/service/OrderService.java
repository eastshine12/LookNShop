package com.eastshine.looknshop.service;

import com.eastshine.looknshop.annotation.DistributedLock;
import com.eastshine.looknshop.domain.Order;
import com.eastshine.looknshop.domain.OrderItem;
import com.eastshine.looknshop.domain.Product.Product;
import com.eastshine.looknshop.domain.Product.ProductOption;
import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.OrderCreateRequest;
import com.eastshine.looknshop.enums.OrderStatus;
import com.eastshine.looknshop.exception.custom.OutOfStockException;
import com.eastshine.looknshop.exception.custom.ProductNotFoundException;
import com.eastshine.looknshop.repository.OrderRepository;
import com.eastshine.looknshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @DistributedLock(key = "#createOrder")
    public Long createOrder(User user, List<OrderCreateRequest> request) {

        List<OrderItem> orderItems = createOrderItems(request);
        Order order = saveOrder(user, orderItems);
        decreaseProductStock(orderItems);

        return order.getId();
    }

    private List<OrderItem> createOrderItems(List<OrderCreateRequest> orderCreateRequests) {
        return orderCreateRequests.stream().map(this::createOrderItem).collect(Collectors.toList());
    }

    private OrderItem createOrderItem(OrderCreateRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + request.getProductId()));

        ProductOption productOption = product.getProductOptions().stream()
                .filter(option -> Objects.equals(option.getId(), request.getProductOptionId()))
                .findFirst()
                .orElse(null);

        if(productOption == null) {
            throw new ProductNotFoundException("Product Option not found with productOptionId: " + request.getProductOptionId());
        } else if (productOption.getStockQuantity() < request.getQuantity()) {
            throw new OutOfStockException("Not enough stock available for product: " + product.getTitle() + " option : " + productOption.getValue());
        }

        return OrderItem.builder()
                .product(product)
                .productOption(productOption)
                .quantity(request.getQuantity())
                .orderPrice(getDiscountPrice(product, productOption))
                .build();
    }

    private Order saveOrder(User user, List<OrderItem> orderItems) {
        Order order = Order.builder()
                .user(user)
                .orderItems(new ArrayList<>())
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.ORDERED)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        return orderRepository.save(order);
    }

    public void decreaseProductStock(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            ProductOption productOption = orderItem.getProductOption();
            int quantity = orderItem.getQuantity();
            product.removeStock(quantity);
            productOption.removeStock(quantity);
        }
    }

    private int getDiscountPrice(Product product, ProductOption productOption) {
        return (((product.getPrice() + productOption.getPrice()) * (100 - product.getDiscountRate())) / 1000) * 10;
    }

    @Transactional
    public void decreaseProductStockWithPessimisticLock(long productId, int quantity) {
        Product product = productRepository.findByIdWithPessimisticLock(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        product.removeStock(quantity);
    }

    @DistributedLock(key = "#lockName")
    public void decreaseProductStockWithRedissonLock(long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        product.removeStock(quantity);
        log.info("Total Stock : {}", product.getTotalStock());
    }
}
