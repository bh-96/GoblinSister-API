package com.kims.goblinsis.service.purchase;

import com.kims.goblinsis.model.domain.Purchase;
import com.kims.goblinsis.model.dto.PurchaseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseService {

    Purchase findById(int id);

    // 구매
    Purchase purchase(PurchaseDTO purchaseDTO, int userId);

    Purchase save(Purchase purchase);

    Purchase updateStatus(int id, int status);

    Page<Purchase> findAllByUserId(int userId, Pageable pageable);

    Page<Purchase> findAllByStatus(int status, Pageable pageable);

    boolean cancel(int id, int userId);

    boolean delete(int id);

    Purchase findByIdAndUserId(int id, int userId);

    Purchase update(int id, int amount);

}
