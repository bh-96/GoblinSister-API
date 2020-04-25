package com.kims.goblinsis.service.support.notice;

import com.kims.goblinsis.model.domain.support.Notice;
import com.kims.goblinsis.model.dto.NoticeDTO;
import com.kims.goblinsis.repository.support.NoticeRepository;
import com.kims.goblinsis.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NoticeServiceImpl extends CommonService implements NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Override
    public Page<Notice> findAll(Pageable pageable) {
        try {
            return noticeRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("NoticeService -> findAll : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Notice save(Notice notice) {
        try {
            notice.setModDate(new Date());
            return noticeRepository.saveAndFlush(notice);
        } catch (Exception e) {
            logger.error("NoticeService -> save : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Notice update(NoticeDTO noticeDTO) {
        Notice notice = findById(noticeDTO.getId());

        if (notice != null) {
            String subject = noticeDTO.getSubject() != null ? noticeDTO.getSubject() : "";
            String content = noticeDTO.getContent() != null ? noticeDTO.getContent() : "";

            if (!subject.equals("")) {
                notice.setSubject(subject);
            }

            if (!content.equals("")) {
                notice.setContent(content);
            }

            return save(notice);
        }

        return null;
    }

    @Override
    public Notice findById(int id) {
        try {
            return noticeRepository.findById(id);
        } catch (Exception e) {
            logger.error("NoticeService -> findById : " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean delete(int id) {
        try {
            noticeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error("NoticeService -> delete : " + e.getMessage());
            return false;
        }
    }

}
