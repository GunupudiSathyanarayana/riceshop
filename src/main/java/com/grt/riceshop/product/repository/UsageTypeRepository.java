package com.grt.riceshop.product.repository;

import com.grt.riceshop.product.entity.UsageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsageTypeRepository extends JpaRepository<UsageType, Long> {

    List<UsageType> findByNameInIgnoreCase(Collection<String> names);

    Optional<UsageType> findByNameContainingIgnoreCase(String name);
}
