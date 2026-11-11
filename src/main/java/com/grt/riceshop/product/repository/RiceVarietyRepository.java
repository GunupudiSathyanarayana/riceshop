package com.grt.riceshop.product.repository;

import com.grt.riceshop.product.entity.RiceVariety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiceVarietyRepository extends JpaRepository<RiceVariety, Long> {

    Optional<RiceVariety> findByNameIgnoreCase(String name);

    Optional<RiceVariety> findByNameContainingIgnoreCase(String name);
}
