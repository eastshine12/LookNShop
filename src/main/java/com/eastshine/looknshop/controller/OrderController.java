package com.eastshine.looknshop.controller;

import com.eastshine.looknshop.annotation.CurrentUser;
import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.OrderCreateRequest;
import com.eastshine.looknshop.dto.request.ProductCreateRequest;
import com.eastshine.looknshop.dto.request.UserCreateRequest;
import com.eastshine.looknshop.dto.request.UserLoginRequest;
import com.eastshine.looknshop.dto.response.UserLoginResponse;
import com.eastshine.looknshop.service.OrderService;
import com.eastshine.looknshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Order", description = "Order API")
@RequestMapping("/api/order")
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@CurrentUser User user, @RequestBody List<OrderCreateRequest> request) {
        log.info("OrderController createOrder()");
        Long orderId = orderService.createOrder(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully. id = " + orderId);
    }


}
