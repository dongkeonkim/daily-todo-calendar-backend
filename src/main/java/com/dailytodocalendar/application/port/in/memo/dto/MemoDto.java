package com.dailytodocalendar.application.port.in.memo.dto;

import com.dailytodocalendar.domain.memo.model.Memo;
import com.dailytodocalendar.domain.memo.model.Todo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** 메모 DTO */
public record MemoDto(
    Long id,
    @JsonIgnore Long memberId,
    String title,
    String content,
    List<TodoDto> todos,
    LocalDate scheduleDate,
    LocalDateTime regDate,
    LocalDateTime udtDate) {

  /**
   * 도메인 모델에서 DTO 생성
   *
   * @param memo 메모 도메인 모델
   * @return 메모 DTO
   */
  public static MemoDto fromDomain(Memo memo) {
    List<TodoDto> todoDtos =
        memo.getTodos() != null
            ? memo.getTodos().stream().map(TodoDto::fromDomain).collect(Collectors.toList())
            : new ArrayList<>();

    return new MemoDto(
        memo.getId(),
        memo.getMemberId(),
        memo.getTitle(),
        memo.getContent(),
        todoDtos,
        memo.getScheduleDate(),
        memo.getRegDate(),
        memo.getUdtDate());
  }

  /**
   * DTO에서 도메인 모델 생성 (할일 목록 제외)
   *
   * @param memberId 회원 ID (인증된 사용자)
   * @return 메모 도메인 모델
   */
  public Memo toDomain(Long memberId) {
    return Memo.builder()
        .id(this.id)
        .memberId(memberId)
        .title(this.title)
        .content(this.content)
        .scheduleDate(this.scheduleDate)
        .todos(new ArrayList<>()) // 일단 빈 목록으로 생성, 후처리 필요
        .regDate(this.regDate != null ? this.regDate : LocalDateTime.now())
        .udtDate(this.udtDate != null ? this.udtDate : LocalDateTime.now())
        .build();
  }

  /**
   * 할일 도메인 모델 목록 생성
   *
   * @param memberId 회원 ID (인증된 사용자)
   * @param memoId 메모 ID
   * @return 할일 도메인 모델 목록
   */
  public List<Todo> toTodoDomains(Long memberId, Long memoId) {
    if (this.todos == null || this.todos.isEmpty()) {
      return new ArrayList<>();
    }

    return this.todos.stream()
        .map(todoDto -> todoDto.toDomain(memberId, memoId))
        .collect(Collectors.toList());
  }
}
