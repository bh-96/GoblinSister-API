package com.kims.goblinsis.service.support.faq;

import com.kims.goblinsis.model.domain.support.Faq;
import com.kims.goblinsis.model.dto.FaqDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FaqService {

    Page<Faq> findAll(Pageable pageable);

    Faq save(Faq faq);

    Faq update(FaqDTO faqDTO);

    Faq findById(int id);

    boolean delete(int id);

}
