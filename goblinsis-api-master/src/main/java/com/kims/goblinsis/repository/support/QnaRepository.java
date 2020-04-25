package com.kims.goblinsis.repository.support;

import com.kims.goblinsis.model.domain.support.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Integer> {

    Qna findById(int id);

    Qna findByIdAndUserId(int id, int userId);

    Page<Qna> findAllByUserId(int userId, Pageable pageable);
}
