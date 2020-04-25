package com.kims.goblinsis.service.support.qna;

import com.kims.goblinsis.model.domain.support.Qna;
import com.kims.goblinsis.model.dto.QnaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaService {

    Page<Qna> findAll(Pageable pageable);

    Page<Qna> findAllByUserId(int userId, Pageable pageable);

    Qna save(Qna qna);

    // 일반 사용자 QNA 수정
    Qna update(QnaDTO qnaDTO, int userId);

    // 관리자 댓글
    Qna comment(QnaDTO qnaDTO);

    Qna findById(int id);

    boolean delete(int id);

    Qna findByIdAndUserId(int id, int userId);

}
