package com.kims.goblinsis.repository;

import com.kims.goblinsis.model.domain.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Integer> {

    Refund findById(int id);

    Page<Refund> findAllByUserId(int userId, Pageable pageable);
}
