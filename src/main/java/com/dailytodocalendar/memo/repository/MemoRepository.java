package com.dailytodocalendar.memo.repository;

import com.dailytodocalendar.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long>, MemoRepositoryCustom {

    Optional<Memo> findByIdAndMemberId(Long id, Long memberId);

    @Query("SELECT DISTINCT YEAR(m.scheduleDate) as years FROM Memo m" +
            " WHERE m.memberId = :memberId AND m.scheduleDate IS NOT NULL" +
            " ORDER BY YEAR(m.scheduleDate)")
    List<String> getTodoCompleteYears(@Param("memberId") Long memberId);

}
