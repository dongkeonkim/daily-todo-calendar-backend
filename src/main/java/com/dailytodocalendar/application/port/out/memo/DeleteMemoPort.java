package com.dailytodocalendar.application.port.out.memo;

import com.dailytodocalendar.domain.memo.model.Memo;

/** 메모 삭제 포트 */
public interface DeleteMemoPort {

  /**
   * 메모 삭제
   *
   * @param memo 메모 엔티티
   */
  void delete(Memo memo);
}
