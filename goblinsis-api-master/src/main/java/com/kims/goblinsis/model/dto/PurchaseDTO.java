package com.kims.goblinsis.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PurchaseDTO {

    @NotNull
    private String productName;     // 제품명

    @NotNull
    private int price;              // 가격

    @NotNull
    private int amount;             // 구매 수량

    @NotNull
    private String payer;           // 구매자 이름 (입금할 이름)

    @NotNull
    private String address;         // 구매자 주소

    @NotNull
    private String phone;           // 구매자 연락처

    @NotNull
    private int postId;             // 게시글 index

}
