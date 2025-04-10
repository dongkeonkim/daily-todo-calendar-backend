package com.dailytodocalendar.adapter.out.persistence.memo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** 메모 JPA 리포지토리 */
@Repository
public interface MemoRepository extends JpaRepository<MemoJpaEntity, Long>, MemoRepositoryCustom {

  /**
   * 메모 ID와 회원 ID로 메모 조회
   *
   * @param id 메모 ID
   * @param memberId 회원 ID
   * @return 메모 JPA 엔티티 옵셔널
   */
  Optional<MemoJpaEntity> findByIdAndMemberId(Long id, Long memberId);

  /**
   * 회원 ID로 메모 목록 조회 (연도별)
   *
   * @param memberId 회원 ID
   * @param year 연도
   * @return 메모 JPA 엔티티 목록
   */
  @Query(
      "SELECT m FROM MemoJpaEntity m WHERE m.memberId = :memberId AND EXTRACT(YEAR FROM m.scheduleDate) = :year ORDER BY m.scheduleDate DESC")
  List<MemoJpaEntity> findByMemberIdAndYear(
      @Param("memberId") Long memberId, @Param("year") int year);

  /**
   * 회원 ID로 메모 목록 조회 (연도, 월별)
   *
   * @param memberId 회원 ID
   * @param year 연도
   * @param month 월
   * @return 메모 JPA 엔티티 목록
   */
  @Query(
      "SELECT m FROM MemoJpaEntity m WHERE m.memberId = :memberId AND EXTRACT(YEAR FROM m.scheduleDate) = :year AND EXTRACT(MONTH FROM m.scheduleDate) = :month ORDER BY m.scheduleDate DESC")
  List<MemoJpaEntity> findByMemberIdAndYearAndMonth(
      @Param("memberId") Long memberId, @Param("year") int year, @Param("month") int month);

  /**
   * 회원 ID로 메모 목록 조회 (날짜별)
   *
   * @param memberId 회원 ID
   * @param date 날짜
   * @return 메모 JPA 엔티티 목록
   */
  List<MemoJpaEntity> findByMemberIdAndScheduleDateOrderByRegDateDesc(
      Long memberId, LocalDate date);

  /**
   * 회원 ID로 메모 목록 조회 (전체)
   *
   * @param memberId 회원 ID
   * @return 메모 JPA 엔티티 목록
   */
  List<MemoJpaEntity> findByMemberIdOrderByScheduleDateDesc(Long memberId);
}
