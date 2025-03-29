package com.dailytodocalendar.api.memo.dto;

import com.dailytodocalendar.api.memo.entity.Memo;
import com.dailytodocalendar.api.memo.entity.Todo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoDto {
    private Long id;
    private Long memberId;

    @NotBlank(message = "할 일 내용은 필수 입력 항목입니다.")
    @Size(max = 500, message = "할 일 내용은 최대 500자까지 입력 가능합니다.")
    private String content;

    private boolean completed;
    private Long memoId;
    private LocalDateTime todoRegDate;
    private LocalDateTime todoUdtDate;

    public Todo toEntity() {
        return Todo.builder()
                .id(id)
                .memberId(memberId)
                .content(content)
                .completed(completed)
                .memo(Memo.builder().id(memoId).build())
                .todoRegDate(todoRegDate != null ? todoRegDate : LocalDateTime.now())
                .todoUdtDate(todoUdtDate != null ? todoUdtDate : LocalDateTime.now())
                .build();
    }

    public Todo toUpdateEntity() {
        return Todo.builder()
                .id(id)
                .memberId(memberId)
                .content(content)
                .completed(completed)
                .memo(Memo.builder().id(memoId).build())
                .todoRegDate(todoRegDate != null ? todoRegDate : LocalDateTime.now())
                .todoUdtDate(LocalDateTime.now())
                .build();
    }

    @Override
    public String toString() {
        return "TodoDto{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", content='" + content + '\'' +
                ", completed=" + completed +
                ", memoId=" + memoId +
                ", todoRegDate=" + todoRegDate +
                ", todoUdtDate=" + todoUdtDate +
                '}';
    }
}