package com.kims.goblinsis.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "PURCHASE")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "product_name")
    private String productName;

    private int price;

    private int amount;

    private int total;  // 구매 금액

    private String payer;   // 입금자 이름

    private String address;

    private String phone;

    private int status;     // 1 -> 주문완료, 2 -> 주문취소, 3 -> 입금확인, 4 -> 배송중, 5 -> 배송완료, 6 -> 환불신청

    @Column(name = "reg_date")
    private Date regDate;

    @Column(name = "mod_date")
    private Date modDate;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "post_id")
    private int postId;

}
