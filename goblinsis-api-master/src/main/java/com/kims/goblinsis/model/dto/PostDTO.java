package com.kims.goblinsis.model.dto;

import lombok.Data;

@Data
public class PostDTO {

    private int id;             // index
    private String subject;     // 게시글 제목
    private String productName; // 제품명
    private int price;          // 가격
    private String content;     // 게시글 내용
    private int category;       // 제품 분류
    private int stock;          // 재고량

}
