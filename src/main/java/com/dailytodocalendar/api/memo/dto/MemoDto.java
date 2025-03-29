package com.dailytodocalendar.api.memo.dto;

import com.dailytodocalendar.api.memo.entity.Memo;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoDto {
  private Long id;
  private Long memberId;

  @NotBlank(message = "제목은 필수 입력 항목입니다.")
  @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
  private String title;

  @Size(max = 1000, message = "내용은 최대 1000자까지 입력 가능합니다.")
  private String content;

  @Valid private List<TodoDto> todos;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate scheduleDate;

  private LocalDateTime regDate;
  private LocalDateTime udtDate;

  public Memo toEntity() {
    Memo memo =
        Memo.builder()
            .id(id)
            .memberId(memberId)
            .title(title)
            .content(content)
            .todos(
                todos != null
                    ? todos.stream().map(TodoDto::toEntity).collect(Collectors.toList())
                    : List.of())
            .scheduleDate(scheduleDate)
            .regDate(regDate != null ? regDate : LocalDateTime.now())
            .udtDate(udtDate != null ? udtDate : LocalDateTime.now())
            .build();

    // 양방향 관계 설정
    if (memo.getTodos() != null) {
      memo.getTodos().forEach(todo -> todo.assignMemo(memo));
    }

    return memo;
  }

  @Override
  public String toString() {
    return "MemoDto{"
        + "id="
        + id
        + ", memberId="
        + memberId
        + ", title='"
        + title
        + '\''
        + ", content='"
        + content
        + '\''
        + ", todos="
        + todos
        + ", scheduleDate="
        + scheduleDate
        + ", regDate="
        + regDate
        + ", udtDate="
        + udtDate
        + '}';
  }
}
