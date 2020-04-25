package com.kims.goblinsis.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "REVIEW")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String subject;

    private String content;

    @Column(name = "product_name")
    private String productName;

    private int amount;

    @Column(name = "reg_date")
    private Date regDate;

    @Column(name = "mod_date")
    private Date modDate;

    @OneToOne(fetch = FetchType.EAGER)
    private Purchase purchase;

    @Column(name = "post_id")
    private int postId;

}
