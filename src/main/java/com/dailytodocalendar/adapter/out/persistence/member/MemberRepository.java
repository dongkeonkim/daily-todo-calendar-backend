package com.dailytodocalendar.adapter.out.persistence.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** 회원 JPA 리포지토리 */
@Repository
public interface MemberRepository extends JpaRepository<MemberJpaEntity, Long> {

  /**
   * 이메일로 회원 조회
   *
   * @param email 이메일
   * @return 회원 JPA 엔티티 옵셔널
   */
  Optional<MemberJpaEntity> findByEmail(String email);

  /**
   * 이메일과 삭제 여부로 회원 조회
   *
   * @param email 이메일
   * @param delYn 삭제 여부
   * @return 회원 JPA 엔티티 옵셔널
   */
  Optional<MemberJpaEntity> findByEmailAndDelYn(String email, boolean delYn);

  /**
   * 이메일 존재 여부 확인
   *
   * @param email 이메일
   * @return 존재 여부
   */
  boolean existsByEmail(String email);
}
