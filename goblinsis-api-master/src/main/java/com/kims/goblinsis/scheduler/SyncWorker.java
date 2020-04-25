package com.kims.goblinsis.scheduler;

import com.kims.goblinsis.model.domain.Post;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.post.PostService;
import com.kims.goblinsis.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SyncWorker extends CommonService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private PostService postService;

    @Scheduled(cron = "0 59 11,23 * * ?")
    private void redisAndDBSync() {
        try {
            List<Post> posts = postService.findAllByRegDateOrModDate();

            if (posts != null) {
                for (Post post : posts) {
                    post.setStock(redisService.getStock(post.getId()));
                    postService.save(post);
                }
            }
        } catch (Exception e) {
            logger.error("SyncWorker : " + e.getMessage());
        }
    }

}
