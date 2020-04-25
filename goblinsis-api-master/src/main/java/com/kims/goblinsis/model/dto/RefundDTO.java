package com.kims.goblinsis.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RefundDTO {

    private int id;         // 환불 index

    @NotNull
    private String bank;    // 환불 은행

    @NotNull
    private String account; // 환불 계좌

    private String payer;   // 통장 주인 이름

    @NotNull
    private int amount;     // 환불 수량

    @NotNull
    private int purchaseId; // 구매내역 index

    private String q;       // 관리자가 환불 목록 조회 할 때, 검색키 (아이디로 검색)

}
