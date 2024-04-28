package com.postitbackend.postIt.memo.entity;

import com.postitbackend.postIt.memo.dto.MemoDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class Memo {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String memberId;
    private String title;
    private String delYn;
    private LocalDateTime regDate;
    private LocalDateTime udtDate;

    public MemoDTO toDTO() {
        return MemoDTO.builder()
                .id(this.id)
                .memberId(this.memberId)
                .title(this.title)
                .delYn(this.delYn)
                .regDate(this.regDate)
                .udtDate(this.udtDate)
                .build();
    }

    @Builder
    public Memo(Long id, String memberId, String title, String delYn, LocalDateTime regDate, LocalDateTime udtDate) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.delYn = delYn;
        this.regDate = regDate;
        this.udtDate = udtDate;
    }
}
