package com.kims.goblinsis.service.review;

import com.kims.goblinsis.model.domain.Purchase;
import com.kims.goblinsis.model.domain.Review;
import com.kims.goblinsis.model.dto.ReviewDTO;
import com.kims.goblinsis.repository.ReviewRepository;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.file.FileService;
import com.kims.goblinsis.service.purchase.PurchaseService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReviewServiceImpl extends CommonService implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private FileService fileService;

    @Override
    public Review save(Review review) {
        try {
            review.setModDate(new Date());
            return reviewRepository.saveAndFlush(review);
        } catch (Exception e) {
            logger.error("ReviewService -> save : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Review setReview(ReviewDTO reviewDTO, int userId) {
        Review review = new Review();
        review.setRegDate(new Date());
        review.setSubject(reviewDTO.getSubject() != null ? reviewDTO.getSubject() : "");
        review.setContent(reviewDTO.getContent() != null ? reviewDTO.getContent() : "");

        Purchase purchase = purchaseService.findById(reviewDTO.getPurchaseId());
        if (purchase == null) {
            return null;
        }

        review.setPurchase(purchase);
        review.setPostId(purchase.getPostId());

        return save(review);
    }

    @Override
    public Review update(ReviewDTO reviewDTO, int userId) {
        Review review = findById(reviewDTO.getId());

        String subject = reviewDTO.getSubject() != null ? reviewDTO.getSubject() : "";
        String content = reviewDTO.getContent() != null ? reviewDTO.getContent() : "";

        if (!subject.equals("")) {
            review.setSubject(subject);
        }

        if (!content.equals("")) {
            review.setContent(content);
        }

        return save(review);
    }

    @Override
    public Review findById(int id) {
        try {
            return findById(id);
        } catch (Exception e) {
            logger.error("ReviewService -> findById : " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean delete(int id) {
        try {
            fileService.delete(id, Constants.TYPE_REVIEW);
            reviewRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error("ReviewService -> delete : " + e.getMessage());
        }

        return false;
    }

    @Override
    public Page<Review> findAllByPostIdOrderByRegDateDesc(int postId, Pageable pageable) {
        try {
            return reviewRepository.findAllByPostIdOrderByRegDateDesc(postId, pageable);
        } catch (Exception e) {
            logger.error("ReviewService -> findAllByPostIdOrderByRegDateDesc : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Page<Review> findAll(Pageable pageable) {
        try {
            return reviewRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("ReviewService -> findAll : " + e.getMessage());
        }

        return null;
    }

}
