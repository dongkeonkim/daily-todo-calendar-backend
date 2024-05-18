package com.postitbackend.postIt.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.postitbackend.postIt.todo.entity.Todo;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoDto {

    @NotNull
    private Long todoId;

    @NotNull
    private Long memoId;

    @NotNull
    private Long memberId;

    private String content;

    @JsonProperty("isCompleted")
    private boolean isCompleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Todo toEntity() {
        return Todo.builder()
                .todoId(todoId)
                .memoId(memoId)
                .memberId(memberId)
                .content(content)
                .isCompleted(isCompleted)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    @Builder
    public TodoDto(Long todoId, Long memoId, Long memberId, String content, boolean isCompleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.todoId = todoId;
        this.memoId = memoId;
        this.memberId = memberId;
        this.content = content;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
