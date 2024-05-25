package com.dailytodocalendar.memo.repository;

import com.dailytodocalendar.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findAllByMemberId(Long memberId);

    Optional<Memo> findByIdAndMemberId(Long id, Long memberId);

}
