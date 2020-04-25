package com.kims.goblinsis.service.refund;

import com.kims.goblinsis.model.domain.Refund;
import com.kims.goblinsis.model.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RefundService {

    Refund save(Refund refund);

    Refund findById(int id);

    Page<Refund> findAllByUser(String q, User user, Pageable pageable);

    Refund changeStatus(int id, int refundStatus);

    Refund delete(int id, int refundStatus, int purchaseStatus);

    Page<Refund> findAll(Pageable pageable);

}
