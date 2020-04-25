package com.kims.goblinsis.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReviewDTO {

    private int id;             // 리뷰 index
    private String subject;     // 리뷰 제목
    private String content;     // 리뷰 내용

    @NotNull
    private int purchaseId;     // 구매내역 index

}
