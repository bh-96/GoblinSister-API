package com.kims.goblinsis.service.statistics;

import com.kims.goblinsis.model.domain.statistics.DailyStatistics;
import com.kims.goblinsis.model.domain.statistics.Statistics;
import com.kims.goblinsis.repository.statistics.DailyStatisticsRepository;
import com.kims.goblinsis.repository.statistics.StatisticsRepository;
import com.kims.goblinsis.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsServiceImpl extends CommonService implements StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private DailyStatisticsRepository dailyStatisticsRepository;

    @Override
    public Statistics save(Statistics statistics) {
        try {
            return statisticsRepository.saveAndFlush(statistics);
        } catch (Exception e) {
            logger.error("StatisticsService -> save : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Statistics update(int id, int status) {
        Statistics statistics = findById(id);

        if (statistics == null) {
            return null;
        }

        statistics.setStatus(status);

        return save(statistics);
    }

    @Override
    public Statistics findById(int id) {
        try {
            return statisticsRepository.findById(id);
        } catch (Exception e) {
            logger.error("StatisticsService -> findById : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Statistics findByPurchaseId(int purchaseId) {
        try {
            return statisticsRepository.findByPurchaseId(purchaseId);
        } catch (Exception e) {
            logger.error("StatisticsService -> findByPurchaseId : " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<DailyStatistics> dailyStatistics(String startDate, String endDate) {
        try {
            return dailyStatisticsRepository.findDailyStatistics(startDate, endDate);
        } catch (Exception e) {
            logger.error("StatisticsService -> dailyStatistics : " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<DailyStatistics> dailyRefundStatistics(String startDate, String endDate) {
        try {
            return dailyStatisticsRepository.findDailyRefundStatistics(startDate, endDate);
        } catch (Exception e) {
            logger.error("StatisticsService -> dailyRefundStatistics : " + e.getMessage());
        }

        return null;
    }
}
