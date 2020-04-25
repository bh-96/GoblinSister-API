package com.kims.goblinsis.controller;

import com.kims.goblinsis.model.domain.Purchase;
import com.kims.goblinsis.model.dto.PurchaseDTO;
import com.kims.goblinsis.security.AccountChecker;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.purchase.PurchaseService;
import com.kims.goblinsis.service.redis.RedisService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = Constants.API_PURCHASE)
public class PurchaseController extends CommonService {

    @Autowired
    private AccountChecker accountChecker;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private RedisService redisService;

    /**
     * 구매 내역 저장
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> purchase(@Valid @RequestBody PurchaseDTO purchaseDTO, HttpServletRequest request) {
        int userId = accountChecker.getUserId(request);

        if (userId == 0) {
            return new ResponseEntity<>(returnErrJsonObj("사용자 조회 실패"), HttpStatus.BAD_REQUEST);
        }

        // 재고량 없으면 구매 불가
        if (redisService.getStock(purchaseDTO.getPostId()) == 0) {
            return new ResponseEntity<>(returnErrJsonObj("재고 부족"), HttpStatus.OK);
        }

        // 특수문자 공백 제거
        String phone = purchaseDTO.getPhone().replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", "").replaceAll(" ", "").trim();;

        // 휴대폰 번호가 10자리 또는 11자리가 아니거나 '-' 를 제외한 번호가 숫자가 아닐 때
        if (!(phone.length() == 10 || phone.length() == 11) || stringToInt(phone) == 0) {
            return new ResponseEntity<>(returnErrJsonObj("휴대폰 번호 유효성 오류"), HttpStatus.BAD_REQUEST);
        }

        purchaseDTO.setPhone(phone);
        Purchase purchase = purchaseService.purchase(purchaseDTO, userId);

        if (purchase != null) {
            // 재고량 감소 -> redis
            redisService.changeStock(purchase.getPostId(), -purchase.getAmount());
            return new ResponseEntity<>(purchase, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("구매 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 구매 내역 확인
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getPurchase(Pageable pageable, HttpServletRequest request) {
        int userId = accountChecker.getUserId(request);

        if (userId == 0) {
            return new ResponseEntity<>(returnErrJsonObj("사용자 조회 실패"), HttpStatus.BAD_REQUEST);
        }

        Page<Purchase> purchases = purchaseService.findAllByUserId(userId, pageable);

        if (purchases != null) {
            return new ResponseEntity<>(purchases, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("구매 내역 조회 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 구매 상태 변경
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public ResponseEntity<?> changePurchaseStatus(@RequestParam(value = "id") int purchaseId, @RequestParam(value = "status") int status) {
        Purchase purchase = purchaseService.updateStatus(purchaseId, status);

        if (purchase != null) {
            return new ResponseEntity<>(purchase, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("인덱스 오류"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 구매 내역 변경
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updatePurchase(@RequestParam int id, @RequestParam int amount) {
        Purchase purchase = purchaseService.update(id, amount);

        if (purchase != null) {
            return new ResponseEntity<>(purchase, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("구매 내역 변경 오류"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 구매 내역 전체 조회
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> getPurchaseList(@RequestParam(value = "status", defaultValue = "5") int status, Pageable pageable) {
        Page<Purchase> purchases = purchaseService.findAllByStatus(status, pageable);

        if (purchases != null) {
            return new ResponseEntity<>(purchases, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("조회 오류"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 주문 취소
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> cancel(@RequestParam int id, HttpServletRequest request) {
        int userId = accountChecker.getUserId(request);
        return new ResponseEntity<>(purchaseService.cancel(id, userId), HttpStatus.OK);
    }

}
