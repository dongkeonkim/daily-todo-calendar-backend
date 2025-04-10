package com.dailytodocalendar.application.port.out.memo;

import com.dailytodocalendar.domain.memo.model.Memo;

/** 메모 저장 포트 */
public interface SaveMemoPort {

  /**
   * 메모 저장
   *
   * @param memo 메모 엔티티
   * @return 저장된 메모 엔티티
   */
  Memo save(Memo memo);
}
