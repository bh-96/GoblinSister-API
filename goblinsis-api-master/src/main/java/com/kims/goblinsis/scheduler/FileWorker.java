package com.kims.goblinsis.scheduler;

import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.utils.DateFormatUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

@Component
public class FileWorker extends CommonService {

    @Value("${file.path}")
    private String filePath;

    @Scheduled(cron = "0 59 23 */7 * ?")
    private void deleteEmptyDir() {
        // 비어있는 폴더 삭제
        try {
            String yyyyMMdd = File.separator + DateFormatUtil.getFormatStringByDate("yyyyMMdd", new Date());
            File yyyyMMddDir = new File(filePath + yyyyMMdd);

            if (yyyyMMddDir.exists()) {
                File[] HHmmDirs = yyyyMMddDir.listFiles();

                if (HHmmDirs.length > 0) {
                    for (File dir : HHmmDirs) {
                        File[] files = dir.listFiles();
                        if (files.length == 0) {
                            logger.info("Deleted Empty Dir (" + yyyyMMdd + ") : " + dir.getPath());
                            dir.delete();
                        }
                    }
                }

                File[] reDirs = yyyyMMddDir.listFiles();
                if (reDirs.length == 0) {
                    logger.info("Deleted Empty Dir (" + yyyyMMdd + ") : " + yyyyMMddDir.getPath());
                    yyyyMMddDir.delete();
                }
            }
        } catch (Exception e) {
            logger.error("Deleted Empty Dir Scheduler Fail : " + e.getMessage());
        }
    }
}
