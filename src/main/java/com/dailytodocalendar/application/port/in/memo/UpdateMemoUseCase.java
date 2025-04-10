package com.dailytodocalendar.application.port.in.memo;

import com.dailytodocalendar.application.port.in.memo.dto.MemoDto;

/** 메모 수정 유스케이스 */
public interface UpdateMemoUseCase {

  /**
   * 메모 수정
   *
   * @param memoDto 수정할 메모 정보
   * @param memberId 회원 ID (인증된 사용자)
   * @return 수정된 메모 정보
   */
  MemoDto updateMemo(MemoDto memoDto, Long memberId);
}
