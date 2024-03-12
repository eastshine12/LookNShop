package com.eastshine.looknshop.repository;

import com.eastshine.looknshop.domain.Product.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

}

