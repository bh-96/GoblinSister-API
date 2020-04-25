package com.kims.goblinsis.utils;

public final class Constants {

    public static final String AUTH_HEADER_TOKEN = "auth-token";
    public static final String AUTH_HEADER_ROLE = "auth-role";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";   // 관리자
    public static final String ROLE_USER = "ROLE_USER";     // 일반 회원
    public static final String ROLE_OUT = "ROLE_OUT";       // 강퇴 당한 회원

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String OUT = "OUT";

    // api
    public static final String API_ADMIN = "/api/admin";            // 관리자
    public static final String API_USER = "/api/users";             // 일반 사용자
    public static final String API_POST = "/api/post";              // 게시글
    public static final String API_COMMENT = "/api/comment";        // 댓글
    public static final String API_FILE = "/api/file";              // 파일 업로드
    public static final String API_STATISTICS = "/api/statistics";  // 통계
    public static final String API_PURCHASE = "/api/purchase";      // 구매
    public static final String API_REFUND = "/api/refund";          // 환불
    public static final String API_REVIEW = "/api/review";          // 리뷰
    public static final String API_FAQ = "/api/faq";                // FAQ
    public static final String API_NOTICE = "/api/notice";          // 공지사항
    public static final String API_QNA = "/api/qna";                // 1대1 문의

    // valid
    public static final int AVAILABLE_PHONE = 1;
    public static final int OVERLAP_PHONE = 2;
    public static final int KICK_OUT_PHONE = 3;

    // file type
    public static final int TYPE_POST = 0;      // 게시글 파일
    public static final int TYPE_REVIEW = 1;    // 리뷰 파일
    public static final int TYPE_REFUND = 2;    // 환불 파일

    // status
    public static final int PURCHASE_STATUS_PURCHASE = 1;   // 주문완료
    public static final int PURCHASE_STATUS_CANCEL = 2;     // 주문취소
    public static final int PURCHASE_STATUS_CHECK = 3;      // 입금확인
    public static final int PURCHASE_STATUS_TRACKING = 4;   // 배송중
    public static final int PURCHASE_STATUS_END = 5;        // 배송완료
    public static final int PURCHASE_STATUS_REFUND = 6;     // 환불신청

    public static final int REFUND_STATUS_REFUND = 1;       // 환불신청
    public static final int REFUND_STATUS_TRACKING = 2;     // 수거중
    public static final int REFUND_STATUS_END = 3;          // 환불완료
    public static final int REFUND_STATUS_CANCEL = 4;       // 환불취소
}
