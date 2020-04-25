package com.kims.goblinsis.service.redis;

import com.kims.goblinsis.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl extends CommonService implements RedisService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean saveStock(int postId, int stock) {
        try {
            redisTemplate.opsForValue().set(String.valueOf(postId), String.valueOf(stock));
            return true;
        } catch (Exception e){
            logger.error("RedisService -> saveStock : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean changeStock(int postId, int num) {
        try {
            redisTemplate.opsForValue().increment(String.valueOf(postId), num);
            return true;
        } catch (Exception e) {
            logger.error("RedisService -> changeStock : " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getStock(int postId) {
        try {
            int stock = Integer.parseInt(redisTemplate.opsForValue().get(String.valueOf(postId)));
            return stock;
        } catch (Exception e) {
            logger.error("RedisService -> getStock : " + e.getMessage());
            return 0;
        }
    }

}
