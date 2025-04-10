package com.dailytodocalendar.adapter.out.persistence.memo;

import com.dailytodocalendar.domain.memo.model.Memo;
import com.dailytodocalendar.domain.memo.model.Todo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/** 메모 매퍼 - JPA 엔티티와 도메인 모델 간 변환 */
@Component
public class MemoMapper {

  /**
   * 도메인 모델 -> JPA 엔티티 변환
   *
   * @param memo 메모 도메인 모델
   * @return 메모 JPA 엔티티
   */
  public MemoJpaEntity toJpaEntity(Memo memo) {
    // 메모 엔티티 생성
    MemoJpaEntity memoEntity =
        MemoJpaEntity.builder()
            .id(memo.getId())
            .memberId(memo.getMemberId())
            .title(memo.getTitle())
            .content(memo.getContent())
            .scheduleDate(memo.getScheduleDate())
            .regDate(memo.getRegDate())
            .udtDate(memo.getUdtDate())
            .todos(new ArrayList<>())
            .build();

    // 할일 엔티티 변환 및 추가
    List<Todo> todos = memo.getTodos();
    if (todos != null && !todos.isEmpty()) {
      for (Todo todo : todos) {
        TodoJpaEntity todoEntity = toTodoJpaEntity(todo);
        memoEntity.addTodo(todoEntity);
      }
    }

    return memoEntity;
  }

  /**
   * JPA 엔티티 -> 도메인 모델 변환
   *
   * @param memoEntity 메모 JPA 엔티티
   * @return 메모 도메인 모델
   */
  public Memo toDomain(MemoJpaEntity memoEntity) {
    // 할일 목록 변환
    List<Todo> todos =
        memoEntity.getTodos().stream().map(this::toTodoDomain).collect(Collectors.toList());

    // 메모 도메인 모델 생성
    return Memo.builder()
        .id(memoEntity.getId())
        .memberId(memoEntity.getMemberId())
        .title(memoEntity.getTitle())
        .content(memoEntity.getContent())
        .scheduleDate(memoEntity.getScheduleDate())
        .todos(todos)
        .regDate(memoEntity.getRegDate())
        .udtDate(memoEntity.getUdtDate())
        .build();
  }

  /**
   * 할일 도메인 모델 -> JPA 엔티티 변환
   *
   * @param todo 할일 도메인 모델
   * @return 할일 JPA 엔티티
   */
  private TodoJpaEntity toTodoJpaEntity(Todo todo) {
    return TodoJpaEntity.builder()
        .id(todo.getId())
        .memberId(todo.getMemberId())
        .content(todo.getContent())
        .completed(todo.isCompleted())
        .todoRegDate(todo.getRegDate())
        .todoUdtDate(todo.getUdtDate())
        .build();
  }

  /**
   * 할일 JPA 엔티티 -> 도메인 모델 변환
   *
   * @param todoEntity 할일 JPA 엔티티
   * @return 할일 도메인 모델
   */
  private Todo toTodoDomain(TodoJpaEntity todoEntity) {
    return Todo.builder()
        .id(todoEntity.getId())
        .memberId(todoEntity.getMemberId())
        .content(todoEntity.getContent())
        .completed(todoEntity.isCompleted())
        .memoId(todoEntity.getMemo().getId())
        .regDate(todoEntity.getTodoRegDate())
        .udtDate(todoEntity.getTodoUdtDate())
        .build();
  }
}
