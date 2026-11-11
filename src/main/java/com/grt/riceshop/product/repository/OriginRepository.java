package com.grt.riceshop.product.repository;

import com.grt.riceshop.product.entity.Origin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OriginRepository extends JpaRepository<Origin, Long> {

    Optional<Origin> findByNameIgnoreCase(String name);

    Optional<Origin> findByNameContainingIgnoreCase(String name);
}
