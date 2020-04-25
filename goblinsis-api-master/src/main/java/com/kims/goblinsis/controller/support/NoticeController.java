package com.kims.goblinsis.controller.support;

import com.kims.goblinsis.model.domain.support.Notice;
import com.kims.goblinsis.model.dto.NoticeDTO;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.support.notice.NoticeService;
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
@RequestMapping(value = Constants.API_NOTICE)
public class NoticeController extends CommonService {

    @Autowired
    private NoticeService noticeService;

    /**
     * 공지사항 조회
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> select(Pageable pageable) {
        Page<Notice> notices = noticeService.findAll(pageable);

        if (notices != null) {
            return new ResponseEntity<>(notices, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("공지사항 조회 실패 혹은 null"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 공지사항 등록
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Notice notice) {
        notice.setRegDate(new Date());
        notice = noticeService.save(notice);

        if (notice != null) {
            return new ResponseEntity<>(notice, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("공지사항 등록 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 공지사항 수정
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody NoticeDTO noticeDTO) {
        Notice notice = noticeService.update(noticeDTO);

        if (notice != null) {
            return new ResponseEntity<>(notice, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("공지사항 수정 실패 혹은 null"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 공지사항 삭제
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam int id) {
        return new ResponseEntity<>(noticeService.delete(id), HttpStatus.OK);
    }

}
