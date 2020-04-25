package com.kims.goblinsis.service.refund;

import com.kims.goblinsis.model.domain.Purchase;
import com.kims.goblinsis.model.domain.Refund;
import com.kims.goblinsis.model.domain.user.User;
import com.kims.goblinsis.repository.RefundRepository;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.file.FileService;
import com.kims.goblinsis.service.purchase.PurchaseService;
import com.kims.goblinsis.service.user.UserService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RefundServiceImpl extends CommonService implements RefundService {

    @Autowired
    private RefundRepository refundRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private FileService fileService;

    @Override
    public Refund save(Refund refund) {
        try {
            return refundRepository.saveAndFlush(refund);
        } catch (Exception e) {
            logger.error("RefundService -> save : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Refund findById(int id) {
        try {
            return refundRepository.findById(id);
        } catch (Exception e) {
            logger.error("RefundService -> findById : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Page<Refund> findAllByUser(String q, User user, Pageable pageable) {
        try {
            // 관리자는 사용자 아이디로 검색 가능
            if (user.getRole().getRolename().equalsIgnoreCase(Constants.API_ADMIN)) {
                if (!q.equals("")) {
                    User qUser = userService.findByAccount(q);

                    if (qUser != null) {
                        return refundRepository.findAllByUserId(qUser.getId(), pageable);
                    }
                }

                // 검색 조건 없으면 전체 조회
                return refundRepository.findAll(pageable);
            }
        } catch (Exception e) {
            logger.error("RefundService -> findAllByUser : " + e.getMessage());
        }

        // 일반 사용자는 자기 정보만 조회 가능
        return refundRepository.findAllByUserId(user.getId(), pageable);
    }

    @Override
    public Refund changeStatus(int id, int refundStatus) {
        try {
            Refund refund = findById(id);

            if (refund != null) {
                refund.setModDate(new Date());
                refund.setStatus(refundStatus);
                return save(refund);
            }
        } catch (Exception e) {
            logger.error("RefundService -> update : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Refund delete(int id, int refundStatus, int purchaseStatus) {
        try {
            Refund refund = findById(id);

            if (refund != null && refund.getStatus() < Constants.REFUND_STATUS_TRACKING) {
                // 구매내역 상태 변경
                Purchase purchase = refund.getPurchase();
                purchase.setStatus(purchaseStatus);
                purchaseService.save(purchase);

                // 파일 삭제
                fileService.delete(id, Constants.TYPE_REFUND);

                // 환불 상태 변경
                refund.setModDate(new Date());
                refund.setStatus(refundStatus);
                return save(refund);
            }
        } catch (Exception e) {
            logger.error("RefundService -> update : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Page<Refund> findAll(Pageable pageable) {
        try {
            return refundRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("RefundService -> findAll : " + e.getMessage());
        }

        return null;
    }

}
