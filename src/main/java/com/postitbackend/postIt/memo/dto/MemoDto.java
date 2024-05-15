package com.postitbackend.postIt.memo.dto;

import com.postitbackend.postIt.memo.entity.Memo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemoDto {

    private Long id;
    private Long memberId;
    private String title;
    private LocalDateTime regDate;
    private LocalDateTime udtDate;

    @Builder
    public MemoDto(Long id, Long memberId, String title, LocalDateTime regDate, LocalDateTime udtDate) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.regDate = regDate;
        this.udtDate = udtDate;
    }

    public Memo toEntity() {
        return Memo.builder()
                .id(id)
                .memberId(memberId)
                .title(title)
                .regDate(regDate)
                .udtDate(udtDate)
                .build();
    }
}
