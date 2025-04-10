package com.dailytodocalendar.adapter.out.persistence.memo;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

/** 할일 JPA 엔티티 */
@Entity
@Table(name = "todo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TodoJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "todo_id")
  private Long id;

  @Column(name = "member_id")
  private Long memberId;

  @Column(name = "todo_content")
  private String content;

  @Column(name = "todo_completed")
  private boolean completed;

  @ManyToOne
  @JoinColumn(name = "memo_id")
  @Setter
  private MemoJpaEntity memo;

  @Column(name = "todo_reg_date")
  private LocalDateTime todoRegDate;

  @Column(name = "todo_udt_date")
  private LocalDateTime todoUdtDate;
}
