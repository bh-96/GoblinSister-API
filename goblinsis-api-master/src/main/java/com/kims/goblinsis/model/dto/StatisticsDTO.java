package com.kims.goblinsis.model.dto;

import lombok.Data;

@Data
public class StatisticsDTO {

    private String startDate;   // 통계 조회 시작일
    private String endDate;     // 통계 조회 완료일
    private int price;          // 금액
    private int status;         // 5 : 구매, 6 : 환불
    private int post_id;        // 게시글 index
    private int user_id;        // 사용자 index

}
