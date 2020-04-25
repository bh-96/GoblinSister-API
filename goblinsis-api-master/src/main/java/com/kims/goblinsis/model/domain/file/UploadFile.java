package com.kims.goblinsis.model.domain.file;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "FILE")
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "reg_date")
    private Date regDate;

    @Column(name = "save_file_name")
    private String saveFileName;

    private Long size;

    private int type;   // 0 : 게시글 파일, 1 : 리뷰 파일, 2 : 환불 파일

    @Column(name = "post_id")
    private int postId;

}
