package com.kims.goblinsis.repository.statistics;

import com.kims.goblinsis.model.domain.statistics.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {

    Statistics findById(int id);

    Statistics findByPurchaseId(int id);

}
