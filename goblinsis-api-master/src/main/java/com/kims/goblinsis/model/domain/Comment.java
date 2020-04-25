package com.kims.goblinsis.model.domain;

import com.kims.goblinsis.model.domain.user.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "COMMENT")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String comment;

    @Column(name = "reg_date")
    private Date regDate;

    @Column(name = "mod_date")
    private Date modDate;

    @Column(name = "re_comment")
    private String reComment;

    @OneToOne(fetch = FetchType.EAGER)
    private Post post;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

}
