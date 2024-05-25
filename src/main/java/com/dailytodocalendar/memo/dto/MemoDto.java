package com.dailytodocalendar.memo.dto;

import com.dailytodocalendar.memo.entity.Memo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoDto {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private List<TodoDto> todos;
    private LocalDateTime regDate;
    private LocalDateTime udtDate;

    public Memo toEntity() {
        Memo memo = Memo.builder()
                .id(id)
                .memberId(memberId)
                .title(title)
                .content(content)
                .todos(todos != null ? todos.stream()
                        .map(TodoDto::toEntity)
                        .collect(Collectors.toList()) : List.of())
                .regDate(regDate != null ? regDate : LocalDateTime.now())
                .udtDate(udtDate != null ? udtDate : LocalDateTime.now())
                .build();

        if (memo.getTodos() != null) {
            memo.getTodos().forEach(todo -> todo.assignMemo(memo));
        }

        return memo;
    }

}




