package com.kims.goblinsis.controller.support;

import com.kims.goblinsis.model.domain.support.Faq;
import com.kims.goblinsis.model.dto.FaqDTO;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.support.faq.FaqService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = Constants.API_FAQ)
public class FaqController extends CommonService {

    @Autowired
    private FaqService faqService;

    /**
     * FAQ 조회
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> select(Pageable pageable) {
        Page<Faq> faqs = faqService.findAll(pageable);

        if (faqs != null) {
            return new ResponseEntity<>(faqs, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("FAQ 조회 실패 혹은 null"), HttpStatus.BAD_REQUEST);
    }

    /**
     * FAQ 등록
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Faq faq) {
        faq.setRegDate(new Date());
        faq = faqService.save(faq);

        if (faq != null) {
            return new ResponseEntity<>(faq, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("FAQ 등록 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * FAQ 수정
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody FaqDTO faqDTO) {
        Faq faq = faqService.update(faqDTO);

        if (faq != null) {
            return new ResponseEntity<>(faq, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("FAQ 수정 실패 혹은 null"), HttpStatus.BAD_REQUEST);
    }

    /**
     * FAQ 삭제
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam int id) {
        return new ResponseEntity<>(faqService.delete(id), HttpStatus.OK);
    }

}
