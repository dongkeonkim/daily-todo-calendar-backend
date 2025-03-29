package com.dailytodocalendar.api.memo.entity;

import com.dailytodocalendar.api.memo.dto.TodoDto;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Todo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "todo_id")
  private Long id;

  @Column(name = "member_id")
  private Long memberId;

  @Column(name = "todo_content")
  private String content;

  @Column(name = "todo_completed")
  private boolean completed;

  @ManyToOne
  @JoinColumn(name = "memo_id")
  private Memo memo;

  @Column(name = "todo_reg_date")
  private LocalDateTime todoRegDate;

  @Column(name = "todo_udt_date")
  private LocalDateTime todoUdtDate;

  public void assignMemo(Memo memo) {
    this.memo = memo;
  }

  public TodoDto toDto() {
    return TodoDto.builder()
        .id(id)
        .memberId(memberId)
        .content(content)
        .completed(completed)
        .memoId(memo.getId())
        .todoRegDate(todoRegDate)
        .todoUdtDate(todoUdtDate)
        .build();
  }
}
