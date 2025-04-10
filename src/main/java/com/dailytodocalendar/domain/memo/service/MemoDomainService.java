package com.dailytodocalendar.domain.memo.service;

import com.dailytodocalendar.domain.memo.exception.MemoDomainException;
import com.dailytodocalendar.domain.memo.model.Memo;
import com.dailytodocalendar.domain.memo.model.Todo;
import java.time.LocalDate;
import java.util.List;

/** 메모 도메인 서비스 - 도메인 로직 중 단일 애그리거트로 처리할 수 없거나 여러 애그리거트가 필요한 작업 처리 - 도메인 개념을 저해하지 않도록 업무 규칙만 구현 */
public class MemoDomainService {

  /**
   * 새 메모 생성
   *
   * @param memberId 회원 ID
   * @param title 제목
   * @param content 내용
   * @param scheduleDate 일정 날짜
   * @param todos 할일 목록
   * @return 생성된 메모
   */
  public Memo createMemo(
      Long memberId, String title, String content, LocalDate scheduleDate, List<Todo> todos) {
    Memo memo = Memo.createMemo(memberId, title, content, scheduleDate);

    if (todos != null && !todos.isEmpty()) {
      for (Todo todo : todos) {
        if (todo.getMemberId() == null || !todo.getMemberId().equals(memberId)) {
          throw new MemoDomainException("할일의 회원 ID가 메모 작성자와 일치하지 않습니다.");
        }
        memo.addTodo(todo);
      }
    }

    return memo;
  }

  /**
   * 메모 업데이트
   *
   * @param memo 메모 엔티티
   * @param title 새 제목
   * @param content 새 내용
   * @param scheduleDate 새 일정 날짜
   * @param todos 새 할일 목록
   */
  public void updateMemo(
      Memo memo, String title, String content, LocalDate scheduleDate, List<Todo> todos) {
    if (memo == null) {
      throw new MemoDomainException("업데이트할 메모가 null입니다.");
    }

    memo.updateInfo(title, content, scheduleDate);

    if (todos != null) {
      for (Todo todo : todos) {
        if (todo.getMemberId() == null || !todo.getMemberId().equals(memo.getMemberId())) {
          throw new MemoDomainException("할일의 회원 ID가 메모 작성자와 일치하지 않습니다.");
        }
      }
      memo.updateTodos(todos);
    }
  }

  /**
   * 메모 소유권 확인
   *
   * @param memo 메모 엔티티
   * @param memberId 회원 ID
   * @return 소유 여부
   */
  public boolean isOwner(Memo memo, Long memberId) {
    return memo != null && memo.getMemberId().equals(memberId);
  }
}
