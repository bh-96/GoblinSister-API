package com.kims.goblinsis.repository;

import com.kims.goblinsis.model.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Review findById(int id);

    Page<Review> findAllByPostIdOrderByRegDateDesc(int postId, Pageable pageable);
}
