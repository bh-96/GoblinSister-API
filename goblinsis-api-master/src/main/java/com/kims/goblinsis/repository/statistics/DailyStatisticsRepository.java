package com.kims.goblinsis.repository.statistics;

import com.kims.goblinsis.model.domain.statistics.DailyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface DailyStatisticsRepository extends JpaRepository<DailyStatistics, Integer> {

    @Transactional
    @Query(value = "SELECT id, SUM(s.price) AS price, s.reg_date AS regDate, s.user_id AS userId " +
            "FROM STATISTICS s " +
            "WHERE s.status = 5 AND s.reg_date BETWEEN :startDate AND :endDate " +
            "GROUP BY s.reg_date " +
            "ORDER BY s.reg_date", nativeQuery = true)
    List<DailyStatistics> findDailyStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Transactional
    @Query(value = "SELECT id, SUM(s.price) AS price, s.reg_date AS regDate, s.user_id AS userId " +
            "FROM STATISTICS s " +
            "WHERE s.status = 6 AND s.reg_date BETWEEN :startDate AND :endDate " +
            "GROUP BY s.reg_date " +
            "ORDER BY s.reg_date", nativeQuery = true)
    List<DailyStatistics> findDailyRefundStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
