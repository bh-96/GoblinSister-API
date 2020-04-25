package com.kims.goblinsis.controller;

import com.kims.goblinsis.model.domain.Review;
import com.kims.goblinsis.model.dto.ReviewDTO;
import com.kims.goblinsis.security.AccountChecker;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.file.FileService;
import com.kims.goblinsis.service.review.ReviewService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = Constants.API_REVIEW)
public class ReviewController extends CommonService {

    @Autowired
    private AccountChecker accountChecker;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private FileService fileService;

    /**
     * 리뷰 등록
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@Valid @RequestBody ReviewDTO reviewDTO, HttpServletRequest request) {
        int userId = accountChecker.getUserId(request);

        Review review = reviewService.setReview(reviewDTO, userId);

        if (review != null) {
            int[] fileIds = findFileIdByContent(review.getContent());
            if (fileIds != null) {
                // 첨부한 파일이 있으면 해당 파일 type 지정
                fileService.update(fileIds, review.getId(), Constants.TYPE_REVIEW);
            }

            return new ResponseEntity<>(review, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("리뷰 등록 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 리뷰 수정
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> update(@Valid @RequestBody ReviewDTO reviewDTO, HttpServletRequest request) {
        int userId = accountChecker.getUserId(request);

        Review review = reviewService.update(reviewDTO, userId);

        if (review != null) {
            int[] fileIds = findFileIdByContent(review.getContent());
            if (fileIds != null) {
                // 첨부한 파일이 있으면 해당 파일 type 지정
                fileService.update(fileIds, review.getId(), Constants.TYPE_REVIEW);
            }

            return new ResponseEntity<>(review, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("리뷰 수정 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 리뷰 삭제
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam int id) {
        return new ResponseEntity<>(reviewService.delete(id), HttpStatus.OK);
    }

    /**
     * 리뷰 조회
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getReview(@RequestParam int postId, Pageable pageable) {
        Page<Review> reviews = reviewService.findAllByPostIdOrderByRegDateDesc(postId, pageable);

        if (reviews != null) {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("리뷰 조회 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 전체 리뷰 조회
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> getReviewList(Pageable pageable) {
        Page<Review> reviews = reviewService.findAll(pageable);

        if (reviews != null) {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("리뷰 전체 조회 실패"), HttpStatus.BAD_REQUEST);
    }

}
