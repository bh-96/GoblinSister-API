package com.kims.goblinsis.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {

    private int id;             // index
    private String comment;     // 댓글
    private String reComment;   // 대댓글 (관리자)
    private Date regDate;       // 등록일 또는 수정일
    private int postId;         // 게시글 index
    private int userId;         // 사용자 index

}
