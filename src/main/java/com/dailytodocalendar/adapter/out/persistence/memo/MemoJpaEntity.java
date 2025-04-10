package com.dailytodocalendar.adapter.out.persistence.memo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

/** 메모 JPA 엔티티 */
@Entity
@Table(name = "memo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemoJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "memo_id")
  private Long id;

  @Column(name = "member_id")
  private Long memberId;

  @Column(name = "memo_title")
  private String title;

  @Column(name = "memo_content", length = 4000)
  private String content;

  @Column(name = "memo_schedule_date")
  private LocalDate scheduleDate;

  @OneToMany(
      mappedBy = "memo",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  @Builder.Default
  private List<TodoJpaEntity> todos = new ArrayList<>();

  @Column(name = "memo_reg_date")
  private LocalDateTime regDate;

  @Column(name = "memo_udt_date")
  private LocalDateTime udtDate;

  /**
   * 할일 추가
   *
   * @param todo 할일 엔티티
   */
  public void addTodo(TodoJpaEntity todo) {
    todos.add(todo);
    todo.setMemo(this);
  }

  /**
   * 할일 목록 업데이트
   *
   * @param newTodos 새 할일 목록
   */
  public void updateTodos(List<TodoJpaEntity> newTodos) {
    todos.clear();
    for (TodoJpaEntity todo : newTodos) {
      addTodo(todo);
    }
  }

  /**
   * 메모 내용 업데이트
   *
   * @param title 새 제목
   * @param content 새 내용
   * @param scheduleDate 새 일정 날짜
   */
  public void update(String title, String content, LocalDate scheduleDate) {
    this.title = title;
    this.content = content;
    this.scheduleDate = scheduleDate;
    this.udtDate = LocalDateTime.now();
  }
}
