package com.kims.goblinsis.model.dto;

import lombok.Data;

@Data
public class FaqDTO {

    private int id;             // FAQ index
    private String subject;     // FAQ 제목
    private String content;     // FAQ 내용

}
