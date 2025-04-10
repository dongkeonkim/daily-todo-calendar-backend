package com.dailytodocalendar.domain.memo.model;

import com.dailytodocalendar.domain.memo.exception.MemoDomainException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/** 메모 도메인 모델 */
@Getter
@Builder
public class Memo {
  private final Long id;
  private final Long memberId;
  private String title;
  private String content;
  private LocalDate scheduleDate;
  private final List<Todo> todos;
  private final LocalDateTime regDate;
  private LocalDateTime udtDate;

  /**
   * 메모 정보 업데이트
   *
   * @param title 제목
   * @param content 내용
   * @param scheduleDate 일정 날짜
   */
  public void updateInfo(String title, String content, LocalDate scheduleDate) {
    validateTitle(title);
    this.title = title;
    this.content = content;
    this.scheduleDate = scheduleDate;
    this.udtDate = LocalDateTime.now();
  }

  /**
   * 할일 목록 업데이트
   *
   * @param newTodos 새 할일 목록
   */
  public void updateTodos(List<Todo> newTodos) {
    if (newTodos == null) {
      return;
    }

    this.todos.clear();
    this.todos.addAll(newTodos);
    this.udtDate = LocalDateTime.now();
  }

  /**
   * 할일 추가
   *
   * @param todo 추가할 할일
   */
  public void addTodo(Todo todo) {
    if (todo == null) {
      throw new MemoDomainException("추가할 할일이 null입니다.");
    }

    this.todos.add(todo);
    this.udtDate = LocalDateTime.now();
  }

  /**
   * 제목 유효성 검증
   *
   * @param title 제목
   */
  private void validateTitle(String title) {
    if (title == null || title.trim().isEmpty()) {
      throw new MemoDomainException("메모 제목은 비어있을 수 없습니다.");
    }
  }

  /**
   * 할일 목록 조회 (방어적 복사) 방어적 복사를 통해 외부에서 직접 컨테이너 내용을 변경할 수 없도록 함
   *
   * @return 불변 할일 목록
   */
  public List<Todo> getTodos() {
    return Collections.unmodifiableList(todos);
  }

  /**
   * 새 메모 생성 (팩토리 메서드)
   *
   * @param memberId 회원 ID
   * @param title 제목
   * @param content 내용
   * @param scheduleDate 일정 날짜
   * @return 새 메모 객체
   */
  public static Memo createMemo(
      Long memberId, String title, String content, LocalDate scheduleDate) {
    if (title == null || title.trim().isEmpty()) {
      throw new MemoDomainException("메모 제목은 비어있을 수 없습니다.");
    }

    LocalDateTime now = LocalDateTime.now();

    return Memo.builder()
        .memberId(memberId)
        .title(title)
        .content(content)
        .scheduleDate(scheduleDate)
        .todos(new ArrayList<>())
        .regDate(now)
        .udtDate(now)
        .build();
  }
}
