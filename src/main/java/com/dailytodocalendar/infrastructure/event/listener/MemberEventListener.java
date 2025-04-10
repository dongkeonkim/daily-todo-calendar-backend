package com.dailytodocalendar.infrastructure.event.listener;

import com.dailytodocalendar.domain.member.event.MemberDeletedEvent;
import com.dailytodocalendar.domain.member.event.MemberUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/** 회원 도메인 이벤트 리스너 - 회원 관련 이벤트 처리 */
@Component
@Slf4j
public class MemberEventListener {

  /**
   * 회원 정보 업데이트 이벤트 처리
   *
   * @param event 회원 정보 업데이트 이벤트
   */
  @EventListener
  @Async
  public void handleMemberUpdatedEvent(MemberUpdatedEvent event) {
    log.info(
        "회원 정보 업데이트 이벤트 수신: memberId={}, email={}, time={}",
        event.getMemberId(),
        event.getEmail(),
        event.getOccurredAt());

    // TODO: 필요한 후속 처리 구현
    // 예: 회원 정보 변경 알림, 통계 수집 등
  }

  /**
   * 회원 탈퇴 이벤트 처리
   *
   * @param event 회원 탈퇴 이벤트
   */
  @EventListener
  @Async
  public void handleMemberDeletedEvent(MemberDeletedEvent event) {
    log.info(
        "회원 탈퇴 이벤트 수신: memberId={}, email={}, time={}",
        event.getMemberId(),
        event.getEmail(),
        event.getOccurredAt());

    // TODO: 필요한 후속 처리 구현
    // 예: 연관 데이터 삭제, 탈퇴 통계 수집, 탈퇴 설문 발송 등
  }
}
