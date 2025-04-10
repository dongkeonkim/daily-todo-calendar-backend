package com.dailytodocalendar.application.port.in.memo;

/** 메모 삭제 유스케이스 */
public interface DeleteMemoUseCase {

  /**
   * 메모 삭제
   *
   * @param memoId 메모 ID
   * @param memberId 회원 ID (인증된 사용자)
   */
  void deleteMemo(Long memoId, Long memberId);
}
