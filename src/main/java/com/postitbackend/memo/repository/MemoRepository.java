package com.postitbackend.memo.repository;

import com.postitbackend.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long>, MemoRepositoryCustom {

    List<Memo> findAllByMemberId(Long memberId);
}
