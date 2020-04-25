package com.kims.goblinsis.service.review;

import com.kims.goblinsis.model.domain.Review;
import com.kims.goblinsis.model.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    Review save(Review review);

    // 리뷰 등록
    Review setReview(ReviewDTO reviewDTO, int userId);

    Review update(ReviewDTO reviewDTO, int userId);

    Review findById(int id);

    boolean delete(int id);

    Page<Review> findAllByPostIdOrderByRegDateDesc(int postId, Pageable pageable);

    Page<Review> findAll(Pageable pageable);

}
