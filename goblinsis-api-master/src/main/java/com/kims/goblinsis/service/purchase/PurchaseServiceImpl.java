package com.kims.goblinsis.service.purchase;

import com.kims.goblinsis.model.domain.Purchase;
import com.kims.goblinsis.model.domain.statistics.Statistics;
import com.kims.goblinsis.model.dto.PurchaseDTO;
import com.kims.goblinsis.repository.PurchaseRepository;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.statistics.StatisticsService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PurchaseServiceImpl extends CommonService implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private StatisticsService statisticsService;

    @Override
    public Purchase findById(int id) {
        try {
            return purchaseRepository.findById(id);
        } catch (Exception e) {
            logger.error("PurchaseService -> findById : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Purchase purchase(PurchaseDTO purchaseDTO, int userId) {
        Purchase purchase = new Purchase();
        purchase.setProductName(purchaseDTO.getProductName());
        purchase.setPrice(purchaseDTO.getPrice());
        purchase.setAmount(purchaseDTO.getAmount());
        purchase.setTotal(purchase.getPrice() * purchase.getAmount());
        purchase.setPayer(purchaseDTO.getPayer());
        purchase.setAddress(purchaseDTO.getAddress());
        purchase.setPhone(purchaseDTO.getPhone());
        purchase.setRegDate(new Date());
        purchase.setStatus(Constants.PURCHASE_STATUS_PURCHASE);
        purchase.setUserId(userId);
        purchase.setPostId(purchaseDTO.getPostId());

        return save(purchase);
    }

    @Override
    public Purchase save(Purchase purchase) {
        try {
            return purchaseRepository.saveAndFlush(purchase);
        } catch (Exception e) {
            logger.error("PurchaseService -> save : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Purchase updateStatus(int id, int status) {
        Purchase purchase = findById(id);

        if (purchase == null) {
            return null;
        }

        purchase.setStatus(status);
        purchase.setModDate(new Date());

        if (status == Constants.PURCHASE_STATUS_END) {
            // 배송완료
            Statistics statistics = new Statistics();
            statistics.setPrice(purchase.getPrice());
            statistics.setStatus(Constants.PURCHASE_STATUS_END);
            statistics.setRegDate(new Date());
            statistics.setUserId(purchase.getUserId());
            statistics.setPurchaseId(purchase.getId());
            statisticsService.save(statistics);
        } if (status == Constants.PURCHASE_STATUS_REFUND) {
            // 환불
            Statistics statistics = statisticsService.findByPurchaseId(purchase.getId());
            statistics.setStatus(Constants.PURCHASE_STATUS_REFUND);
            statistics.setRegDate(new Date());
            statisticsService.save(statistics);
        }

        return save(purchase);
    }

    @Override
    public Page<Purchase> findAllByUserId(int userId, Pageable pageable) {
        try {
            return purchaseRepository.findAllByUserId(userId, pageable);
        } catch (Exception e) {
            logger.error("PurchaseService -> findAllByStatusAndUserId : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Page<Purchase> findAllByStatus(int status, Pageable pageable) {
        try {
            return purchaseRepository.findAllByStatus(status, pageable);
        } catch (Exception e) {
            logger.error("PurchaseService -> findAllByStatus : " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean cancel(int id, int userId) {
        Purchase purchase = findByIdAndUserId(id, userId);

        // 배송중 상태 전일 때만 주문취소 가능
        if (purchase != null && purchase.getStatus() == Constants.PURCHASE_STATUS_PURCHASE) {
            purchase.setStatus(Constants.PURCHASE_STATUS_CANCEL);

            if (save(purchase) != null) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        try {
            purchaseRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error("PurchaseService -> delete : " + e.getMessage());
        }

        return false;
    }

    @Override
    public Purchase findByIdAndUserId(int id, int userId) {
        try {
            return purchaseRepository.findByIdAndUserId(id, userId);
        } catch (Exception e) {
            logger.error("PurchaseService -> findByIdAndUserId : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Purchase update(int id, int amount) {
        Purchase purchase = findById(id);
        if (purchase == null) {
            return null;
        }

        purchase.setAmount(amount);
        purchase.setModDate(new Date());

        return save(purchase);
    }
}
