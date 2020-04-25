package com.kims.goblinsis.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "REFUND")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String bank;

    private String account;

    private String payer;

    private int amount;

    private int status;     // 1 -> 환불신청, 2 -> 수거중, 3 -> 환불완료, 4 -> 환불취소

    @Column(name = "reg_date")
    private Date regDate;

    @Column(name = "mod_date")
    private Date modDate;

    @OneToOne(fetch = FetchType.EAGER)
    private Purchase purchase;

    @Column(name = "user_id")
    private int userId;

}
