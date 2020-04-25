package com.kims.goblinsis.service.redis;

public interface RedisService {

    // 재고량 저장
    boolean saveStock(int postId, int stock);

    // 재고량 증감
    boolean changeStock(int postId, int num);

    // 재고량 조회
    int getStock(int postId);

}
