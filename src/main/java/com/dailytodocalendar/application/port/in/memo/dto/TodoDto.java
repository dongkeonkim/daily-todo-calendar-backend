package com.dailytodocalendar.application.port.in.memo.dto;

import com.dailytodocalendar.domain.memo.model.Todo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

/** 할일 DTO */
public record TodoDto(
    Long id,
    @JsonIgnore // 클라이언트 응답에서 제외
        Long memberId,
    String content,
    boolean completed,
    Long memoId,
    LocalDateTime todoRegDate,
    LocalDateTime todoUdtDate) {

  /**
   * 도메인 모델에서 DTO 생성
   *
   * @param todo 할일 도메인 모델
   * @return 할일 DTO
   */
  public static TodoDto fromDomain(Todo todo) {
    return new TodoDto(
        todo.getId(),
        todo.getMemberId(),
        todo.getContent(),
        todo.isCompleted(),
        todo.getMemoId(),
        todo.getRegDate(),
        todo.getUdtDate());
  }

  /**
   * DTO에서 도메인 모델 생성
   *
   * @param memberId 회원 ID
   * @param memoId 메모 ID
   * @return 할일 도메인 모델
   */
  public Todo toDomain(Long memberId, Long memoId) {
    return Todo.builder()
        .id(this.id)
        .memberId(memberId)
        .content(this.content)
        .completed(this.completed)
        .memoId(memoId)
        .regDate(this.todoRegDate != null ? this.todoRegDate : LocalDateTime.now())
        .udtDate(this.todoUdtDate != null ? this.todoUdtDate : LocalDateTime.now())
        .build();
  }
}
