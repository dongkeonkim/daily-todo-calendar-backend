package com.dailytodocalendar.memo.repository;

import com.dailytodocalendar.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long>, MemoRepositoryCustom {

    @Query("select m from Memo m left join fetch m.todos")
    List<Memo> findAllByMemberId(@Param("memberId") Long memberId);

    Optional<Memo> findByIdAndMemberId(Long id, Long memberId);

}
