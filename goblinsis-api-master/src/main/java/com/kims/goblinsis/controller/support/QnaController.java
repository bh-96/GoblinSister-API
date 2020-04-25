package com.kims.goblinsis.controller.support;

import com.kims.goblinsis.model.domain.support.Qna;
import com.kims.goblinsis.model.dto.QnaDTO;
import com.kims.goblinsis.security.AccountChecker;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.support.qna.QnaService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping(value = Constants.API_QNA)
public class QnaController extends CommonService {

    @Autowired
    private AccountChecker accountChecker;

    @Autowired
    private QnaService qnaService;

    /**
     * QNA 전체 조회
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> getQnaList(Pageable pageable) {
        Page<Qna> qnas = qnaService.findAll(pageable);

        if (qnas != null) {
            return new ResponseEntity<>(qnas, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("QNA 조회 실패 혹은 null"), HttpStatus.BAD_REQUEST);
    }

    /**
     * QNA 조회
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> select(Pageable pageable, HttpServletRequest request) {
        int userId = accountChecker.getUserId(request);
        Page<Qna> qnas = qnaService.findAllByUserId(userId, pageable);

        if (qnas != null) {
            return new ResponseEntity<>(qnas, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("QNA 조회 실패 혹은 null"), HttpStatus.BAD_REQUEST);
    }

    /**
     * QNA 등록
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Qna qna) {
        qna.setRegDate(new Date());
        qna = qnaService.save(qna);

        if (qna != null) {
            return new ResponseEntity<>(qna, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("QNA 등록 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * QNA 수정
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody QnaDTO qnaDTO, HttpServletRequest request) {
        int userId = accountChecker.getUserId(request);
        Qna qna = qnaService.update(qnaDTO, userId);

        if (qna != null) {
            return new ResponseEntity<>(qna, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("QNA 수정 실패 혹은 null"), HttpStatus.BAD_REQUEST);
    }

    /**
     * QNA 댓글
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/comment", method = RequestMethod.PUT)
    public ResponseEntity<?> comment(@RequestBody QnaDTO qnaDTO) {
        Qna qna = qnaService.comment(qnaDTO);

        if (qna != null) {
            return new ResponseEntity<>(qna, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("QNA 댓글 수정 실패 혹은 null"), HttpStatus.BAD_REQUEST);
    }

    /**
     * QNA 삭제
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam int id) {
        return new ResponseEntity<>(qnaService.delete(id), HttpStatus.OK);
    }
}
