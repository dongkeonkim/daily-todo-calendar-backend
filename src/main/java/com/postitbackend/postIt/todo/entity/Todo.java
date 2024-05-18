package com.postitbackend.postIt.todo.entity;

import com.postitbackend.postIt.todo.dto.TodoDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    private Long memoId;

    private Long memberId;

    private String content;

    private boolean isCompleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public TodoDto toDto() {
        return TodoDto.builder()
                .todoId(todoId)
                .memoId(memoId)
                .memberId(memberId)
                .content(content)
                .isCompleted(isCompleted)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public void update(TodoDto todoDto) {
        this.content = todoDto.getContent();
        this.isCompleted = todoDto.isCompleted();
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public Todo(Long todoId, Long memoId, Long memberId, String content, boolean isCompleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.todoId = todoId;
        this.memoId = memoId;
        this.memberId = memberId;
        this.content = content;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
