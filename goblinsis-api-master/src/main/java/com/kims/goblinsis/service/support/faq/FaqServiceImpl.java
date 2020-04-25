package com.kims.goblinsis.service.support.faq;

import com.kims.goblinsis.model.domain.support.Faq;
import com.kims.goblinsis.model.dto.FaqDTO;
import com.kims.goblinsis.repository.support.FaqRepository;
import com.kims.goblinsis.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FaqServiceImpl extends CommonService implements FaqService {

    @Autowired
    private FaqRepository faqRepository;

    @Override
    public Page<Faq> findAll(Pageable pageable) {
        try {
            return faqRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("FaqService -> findAll : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Faq save(Faq faq) {
        try {
            faq.setModDate(new Date());
            return faqRepository.saveAndFlush(faq);
        } catch (Exception e) {
            logger.error("FaqService -> save : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Faq update(FaqDTO faqDTO) {
        Faq faq = findById(faqDTO.getId());

        if (faq != null) {
            String subject = faqDTO.getSubject() != null ? faqDTO.getSubject() : "";
            String content = faqDTO.getContent() != null ? faqDTO.getContent() : "";

            if (!subject.equals("")) {
                faq.setSubject(subject);
            }

            if (!content.equals("")) {
                faq.setContent(content);
            }

            return save(faq);
        }

        return null;
    }

    @Override
    public Faq findById(int id) {
        try {
            return faqRepository.findById(id);
        } catch (Exception e) {
            logger.error("FaqService -> findById : " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean delete(int id) {
        try {
            faqRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error("FaqService -> delete : " + e.getMessage());
            return false;
        }
    }

}
