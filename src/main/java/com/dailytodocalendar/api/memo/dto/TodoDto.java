package com.dailytodocalendar.api.memo.dto;

import com.dailytodocalendar.api.memo.entity.Memo;
import com.dailytodocalendar.api.memo.entity.Todo;
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
                .todoRegDate(todoRegDate)
                .todoUdtDate(LocalDateTime.now())
                .build();
    }

}
