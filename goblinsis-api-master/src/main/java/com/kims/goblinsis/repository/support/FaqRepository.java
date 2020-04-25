package com.kims.goblinsis.repository.support;

import com.kims.goblinsis.model.domain.support.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Integer> {

    Faq findById(int id);
}
