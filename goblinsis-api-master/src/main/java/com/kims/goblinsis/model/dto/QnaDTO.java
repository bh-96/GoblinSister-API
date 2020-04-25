package com.kims.goblinsis.model.dto;

import lombok.Data;

@Data
// 1대1 문의
public class QnaDTO {

    private int id;             // QnA index
    private String subject;     // QnA 제목
    private String content;     // QnA 내용
    private String comment;     // QnA 댓글 (관리자)

}
