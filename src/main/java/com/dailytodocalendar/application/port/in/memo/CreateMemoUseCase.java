package com.dailytodocalendar.application.port.in.memo;

import com.dailytodocalendar.application.port.in.memo.dto.MemoDto;

/** 메모 생성 유스케이스 */
public interface CreateMemoUseCase {

  /**
   * 메모 생성
   *
   * @param memoDto 메모 정보
   * @param memberId 회원 ID
   * @return 생성된 메모 정보
   */
  MemoDto createMemo(MemoDto memoDto, Long memberId);
}
