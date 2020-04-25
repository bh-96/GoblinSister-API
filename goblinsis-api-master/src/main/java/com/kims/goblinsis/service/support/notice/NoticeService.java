package com.kims.goblinsis.service.support.notice;

import com.kims.goblinsis.model.domain.support.Notice;
import com.kims.goblinsis.model.dto.NoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {

    Page<Notice> findAll(Pageable pageable);

    Notice save(Notice notice);

    Notice update(NoticeDTO noticeDTO);

    Notice findById(int id);

    boolean delete(int id);

}
