package com.kims.goblinsis.controller;

import com.kims.goblinsis.model.domain.statistics.DailyStatistics;
import com.kims.goblinsis.model.dto.StatisticsDTO;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.statistics.StatisticsService;
import com.kims.goblinsis.utils.Constants;
import com.kims.goblinsis.utils.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(Constants.API_STATISTICS)
public class StatisticsController extends CommonService {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 지정한 날짜 사이의 배송완료 통계 내역 가져오기
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getStatistics(@RequestBody StatisticsDTO statisticsDTO) {
        Date date = new Date();
        String startDate = statisticsDTO.getStartDate() != null ? statisticsDTO.getStartDate() : DateFormatUtil.beforeOrAfterDaysDate("yyyy-MM-dd", -7, date);
        String endDate = statisticsDTO.getEndDate() != null ? statisticsDTO.getEndDate() : DateFormatUtil.getFormatStringByDate("yyyy-MM-dd", date);

        List<DailyStatistics> dailyStatistics = statisticsService.dailyStatistics(startDate, endDate);

        if (dailyStatistics != null) {
            return new ResponseEntity<>(dailyStatistics, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("날짜 오류 또는 해당 기간 통계 내역 없음"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 지정한 날짜 사이의 환불 통계 내역 가져오기
     */
    @RequestMapping(value = "/refund", method = RequestMethod.GET)
    public ResponseEntity<?> getRefundStatistics(@RequestBody StatisticsDTO statisticsDTO) {
        Date date = new Date();
        String startDate = statisticsDTO.getStartDate() != null ? statisticsDTO.getStartDate() : DateFormatUtil.beforeOrAfterDaysDate("yyyy-MM-dd", -7, date);
        String endDate = statisticsDTO.getEndDate() != null ? statisticsDTO.getEndDate() : DateFormatUtil.getFormatStringByDate("yyyy-MM-dd", date);

        List<DailyStatistics> dailyStatistics = statisticsService.dailyRefundStatistics(startDate, endDate);

        if (dailyStatistics != null) {
            return new ResponseEntity<>(dailyStatistics, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("날짜 오류 또는 해당 기간 통계 내역 없음"), HttpStatus.BAD_REQUEST);
    }

}
