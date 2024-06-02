package com.dailytodocalendar.memo.entity;

import com.dailytodocalendar.memo.dto.MemoDto;
import com.dailytodocalendar.memo.dto.TodoDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "memo_title")
    private String title;

    @Column(name = "memo_content")
    private String content;

    @Column(name = "memo_schedule_date")
    private LocalDateTime scheduleDate;

    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Todo> todos = new ArrayList<>();

    @Column(name = "memo_reg_date")
    private LocalDateTime regDate;

    @Column(name = "memo_udt_date")
    private LocalDateTime udtDate;

    public MemoDto toDto() {
        return MemoDto.builder()
                .id(id)
                .memberId(memberId)
                .title(title)
                .content(content)
                .todos(todos != null ? todos.stream()
                        .map(Todo::toDto)
                        .collect(Collectors.toList()) : List.of())
                .scheduleDate(scheduleDate)
                .regDate(regDate)
                .udtDate(udtDate)
                .build();
    }

    public void update(MemoDto memoDto) {
        List<Todo> todos = memoDto.getTodos() != null ? memoDto.getTodos().stream()
                .map(TodoDto::toEntity)
                .toList() : List.of();

        this.title = memoDto.getTitle();
        this.content = memoDto.getContent();
        this.todos.clear();
        this.todos.addAll(todos);
        todos.forEach(todo -> todo.assignMemo(this));
        this.scheduleDate = memoDto.getScheduleDate();
        this.udtDate = LocalDateTime.now();
    }
}