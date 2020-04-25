package com.kims.goblinsis.repository.support;

import com.kims.goblinsis.model.domain.support.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    Notice findById(int id);
}
