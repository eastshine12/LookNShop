package com.eastshine.looknshop.repository;

import com.eastshine.looknshop.domain.Order;
import com.eastshine.looknshop.domain.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}

