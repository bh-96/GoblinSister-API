package com.kims.goblinsis.model.domain.statistics;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class DailyStatistics {

    @Id
    private int id;

    private int price;

    @Column(name = "reg_date")
    private Date regDate;

    @Column(name = "user_id")
    private int userId;
}
