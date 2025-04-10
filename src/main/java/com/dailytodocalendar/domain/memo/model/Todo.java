package com.dailytodocalendar.domain.memo.model;

import com.dailytodocalendar.domain.memo.exception.MemoDomainException;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/** 할일 도메인 모델 */
@Getter
@Builder
public class Todo {
  private final Long id;
  private final Long memberId;
  private String content;
  private boolean completed;
  private final Long memoId;
  private final LocalDateTime regDate;
  private LocalDateTime udtDate;

  /**
   * 할일 내용 수정
   *
   * @param content 새 내용
   * @throws MemoDomainException 유효하지 않은 내용인 경우
   */
  public void updateContent(String content) {
    validateContent(content);
    this.content = content;
    this.udtDate = LocalDateTime.now();
  }

  /**
   * 할일 완료 상태 변경
   *
   * @param completed 완료 여부
   */
  public void updateCompletionStatus(boolean completed) {
    this.completed = completed;
    this.udtDate = LocalDateTime.now();
  }

  /**
   * 내용 유효성 검증
   *
   * @param content 할일 내용
   */
  private void validateContent(String content) {
    if (content == null || content.trim().isEmpty()) {
      throw new MemoDomainException("할일 내용은 비어있을 수 없습니다.");
    }
  }

  /**
   * 새 할일 생성 (팩토리 메서드)
   *
   * @param memberId 회원 ID
   * @param content 내용
   * @param memoId 메모 ID
   * @return 새 할일 객체
   */
  public static Todo createTodo(Long memberId, String content, Long memoId) {
    if (content == null || content.trim().isEmpty()) {
      throw new MemoDomainException("할일 내용은 비어있을 수 없습니다.");
    }

    LocalDateTime now = LocalDateTime.now();

    return Todo.builder()
        .memberId(memberId)
        .content(content)
        .completed(false)
        .memoId(memoId)
        .regDate(now)
        .udtDate(now)
        .build();
  }
}
