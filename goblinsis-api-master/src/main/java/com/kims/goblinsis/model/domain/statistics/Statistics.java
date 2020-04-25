package com.kims.goblinsis.model.domain.statistics;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "STATISTICS")
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int price;

    private int status;

    @Column(name = "reg_date")
    private Date regDate;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "purchase_id")
    private int purchaseId;

}
