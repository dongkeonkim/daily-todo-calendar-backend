package com.postitbackend.memo.entity;

import com.postitbackend.memo.dto.MemoDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "memo_title")
    private String title;

    @Column(name = "memo_reg_date")
    private LocalDateTime regDate;

    @Column(name = "memo_udt_date")
    private LocalDateTime udtDate;

    @Builder
    public Memo(Long id, Long memberId, String title, LocalDateTime regDate, LocalDateTime udtDate) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.regDate = regDate;
        this.udtDate = udtDate;
    }

    public MemoDto toDto() {
        return new MemoDto(this.id, this.memberId, this.title, this.regDate, this.udtDate);
    }
}