package com.kims.goblinsis.service.statistics;

import com.kims.goblinsis.model.domain.statistics.DailyStatistics;
import com.kims.goblinsis.model.domain.statistics.Statistics;

import java.util.List;

public interface StatisticsService {

    Statistics save(Statistics statistics);

    Statistics update(int id, int status);

    Statistics findById(int id);

    Statistics findByPurchaseId(int purchaseId);

    // 일일 매출 통계
    List<DailyStatistics> dailyStatistics(String startDate, String endDate);

    // 일일 환불 금액 통계
    List<DailyStatistics> dailyRefundStatistics(String startDate, String endDate);

}
