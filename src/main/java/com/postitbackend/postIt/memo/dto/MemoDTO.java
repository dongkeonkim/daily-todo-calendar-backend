package com.postitbackend.postIt.memo.dto;

import com.postitbackend.postIt.memo.entity.Memo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class MemoDTO {

    private Long id;

    private String memberId;
    private String title;
    private String delYn;
    private LocalDateTime regDate;
    private LocalDateTime udtDate;

    public Memo toEntity() {
        return Memo.builder()
                .id(this.id)
                .memberId(this.memberId)
                .title(this.title)
                .delYn(this.delYn)
                .regDate(this.regDate)
                .udtDate(this.udtDate)
                .build();
    }

}
