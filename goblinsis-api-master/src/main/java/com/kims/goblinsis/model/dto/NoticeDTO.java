package com.kims.goblinsis.model.dto;

import lombok.Data;

@Data
public class NoticeDTO {

    private int id;             // 공지사항 index
    private String subject;     // 공지사항 제목
    private String content;     // 공지사항 내용

}
