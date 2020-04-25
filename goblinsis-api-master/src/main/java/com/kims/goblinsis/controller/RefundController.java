package com.kims.goblinsis.controller;

import com.kims.goblinsis.model.domain.Purchase;
import com.kims.goblinsis.model.domain.Refund;
import com.kims.goblinsis.model.domain.file.UploadFile;
import com.kims.goblinsis.model.domain.user.User;
import com.kims.goblinsis.model.dto.RefundDTO;
import com.kims.goblinsis.security.AccountChecker;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.file.FileService;
import com.kims.goblinsis.service.purchase.PurchaseService;
import com.kims.goblinsis.service.redis.RedisService;
import com.kims.goblinsis.service.refund.RefundService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = Constants.API_REFUND)
public class RefundController extends CommonService {

    @Autowired
    private AccountChecker accountChecker;

    @Autowired
    private RefundService refundService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private FileService fileService;

    @Autowired
    private RedisService redisService;

    /**
     * 환불 신청
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> refund(@Valid @RequestBody RefundDTO refundDTO, HttpServletRequest request) {
        int userId = accountChecker.getUserId(request);

        Refund refund = toRefund(refundDTO, userId);
        if (refund != null) {
            refund = refundService.save(refund);

            if (refund != null) {
                List<UploadFile> files = fileService.findAllByTypeIsNull();
                if (files != null) {
                    int[] fileIds = findFileId(files);
                    if (fileIds != null) {
                        // 첨부한 파일이 있으면 해당 파일 type 지정
                        fileService.update(fileIds, refund.getId(), Constants.TYPE_REFUND);
                    }
                }

                return new ResponseEntity<>(refund, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(returnErrJsonObj("환불 등록 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 환불 신청 취소
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public ResponseEntity<?> cancel(@RequestParam int id) {
        Refund refund = refundService.delete(id, Constants.REFUND_STATUS_CANCEL, Constants.PURCHASE_STATUS_END);

        if (refund != null) {
            return new ResponseEntity<>(refund, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("환불 취소 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 환불 신청 목록 조회
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getRefundList(@RequestParam(value = "q", defaultValue = "") String q, Pageable pageable, HttpServletRequest request) {
        try {
            User user = accountChecker.getUser(request);
            return new ResponseEntity<>(refundService.findAllByUser(q, user, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(returnErrJsonObj("환불 신청 목록 조회 실패"), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 환불 상태 변경
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public ResponseEntity<?> changeRefundStatus(@RequestParam(value = "id") int refundId, @RequestParam(value = "status") int refundStatus) {
        Refund refund = refundService.changeStatus(refundId, refundStatus);

        if (refund != null) {
            if (refund.getStatus() == Constants.REFUND_STATUS_END) {
                // 환불완료 시 재고량 증가..?
                int postId = refund.getPurchase().getPostId();
                redisService.changeStock(postId, refund.getAmount());
            }
            return new ResponseEntity<>(refund, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("환불 상태 변경 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 환불 목록 전체 조회
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll(Pageable pageable) {
        Page<Refund> refunds = refundService.findAll(pageable);

        if (refunds != null) {
            return new ResponseEntity<>(refunds, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("환불 목록 조회 실패 혹은 null"), HttpStatus.BAD_REQUEST);
    }

    private Refund toRefund(RefundDTO refundDTO, int userId) {
        Refund refund = new Refund();
        refund.setBank(refundDTO.getBank());
        refund.setAccount(refundDTO.getAccount());
        refund.setAmount(refundDTO.getAmount());
        refund.setStatus(Constants.REFUND_STATUS_REFUND);
        refund.setRegDate(new Date());
        refund.setModDate(new Date());
        refund.setUserId(userId);

        Purchase purchase = setPurchaseStatus(refundDTO.getPurchaseId(), userId);
        if (purchase != null) {
            refund.setPurchase(purchase);
            return refund;
        }

        return null;
    }

    private Purchase setPurchaseStatus(int purchaseId, int userId) {
        Purchase purchase = purchaseService.findByIdAndUserId(purchaseId, userId);

        // 배송완료 이후 상태만 환불 가능
        if (purchase != null && purchase.getStatus() >= Constants.PURCHASE_STATUS_END) {
            purchase.setStatus(Constants.PURCHASE_STATUS_REFUND);
            purchase = purchaseService.save(purchase);

            if (purchase != null) {
                return purchase;
            }
        }

        return null;
    }
}
