package com.kims.goblinsis.repository;

import com.kims.goblinsis.model.domain.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    Purchase findById(int id);

    Page<Purchase> findAllByStatus(int status, Pageable pageable);

    Page<Purchase> findAllByUserId(int userId, Pageable pageable);

    Purchase findByIdAndUserId(int id, int userId);
}
