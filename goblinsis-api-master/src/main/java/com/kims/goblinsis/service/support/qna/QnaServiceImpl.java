package com.kims.goblinsis.service.support.qna;

import com.kims.goblinsis.model.domain.support.Qna;
import com.kims.goblinsis.model.dto.QnaDTO;
import com.kims.goblinsis.repository.support.QnaRepository;
import com.kims.goblinsis.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class QnaServiceImpl extends CommonService implements QnaService {

    @Autowired
    private QnaRepository qnaRepository;

    @Override
    public Page<Qna> findAll(Pageable pageable) {
        try {
            return qnaRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("QnaService -> findAll : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Page<Qna> findAllByUserId(int userId, Pageable pageable) {
        try {
            return qnaRepository.findAllByUserId(userId, pageable);
        } catch (Exception e) {
            logger.error("QnaService -> findAllByUserId : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Qna save(Qna qna) {
        try {
            qna.setModDate(new Date());
            return qnaRepository.saveAndFlush(qna);
        } catch (Exception e) {
            logger.error("QnaService -> save : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Qna update(QnaDTO qnaDTO, int userId) {
        Qna qna = findByIdAndUserId(qnaDTO.getId(), userId);

        if (qna != null) {
            String subject = qnaDTO.getSubject() != null ? qnaDTO.getSubject() : "";
            String content = qnaDTO.getContent() != null ? qnaDTO.getContent() : "";

            if (!subject.equals("")) {
                qna.setSubject(subject);
            }

            if (!content.equals("")) {
                qna.setContent(content);
            }

            return save(qna);
        }

        return null;
    }

    @Override
    public Qna comment(QnaDTO qnaDTO) {
        Qna qna = findById(qnaDTO.getId());

        if (qna != null) {
            String comment = qnaDTO.getComment() != null ? qnaDTO.getComment() : "";

            if (!comment.equals("")) {
                qna.setContent(comment);
            }

            return save(qna);
        }

        return null;
    }

    @Override
    public Qna findById(int id) {
        try {
            return qnaRepository.findById(id);
        } catch (Exception e) {
            logger.error("QnaService -> findById : " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean delete(int id) {
        try {
            qnaRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error("QnaService -> delete : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Qna findByIdAndUserId(int id, int userId) {
        try {
            return qnaRepository.findByIdAndUserId(id, userId);
        } catch (Exception e) {
            logger.error("QnaService -> findByIdAndUserId : " + e.getMessage());
        }

        return null;
    }
}
