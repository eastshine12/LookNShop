package com.eastshine.looknshop.repository;

import com.eastshine.looknshop.domain.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdWithPessimisticLock(@Param("id") Long id);

    @Query("SELECT p FROM Product p JOIN ProductCategory pc ON p.category.id = pc.id WHERE p.id = :id")
    Optional<Product> findByIdWithCategory(@Param("id") Long id);


}

