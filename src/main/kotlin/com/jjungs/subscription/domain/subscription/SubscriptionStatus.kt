package com.jjungs.subscription.domain.subscription

enum class SubscriptionStatus(val description: String) {
    ACTIVE("구독중"),
    TRIAL("시행단계"),
    PENDING("결제하기 전 상태"),
    PAUSED("고객이 다음 청구를 일시정지함. 다음 청구일까지 서비스 사용 가능하고 구독을 재개하지 않으면 구독이 만료됨."),
    SUSPENDED("정책 문제로 서비스 이용이 일시정지됨 (부정 사용, 등)"),
    PAST_DUE("고객이 비용을 지불하지 않아 서비스 이용이 일시정지됨 (카드 거절, 잔액 부족, 등)"),
    CANCELLED("고객이 구독을 취소함"),
    EXPIRED("구독 만료"),
}
