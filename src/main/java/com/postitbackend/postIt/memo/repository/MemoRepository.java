package com.postitbackend.postIt.memo.repository;

import com.postitbackend.postIt.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {

//    List<Memo> findAllByMemberId(String email);
//
//    List<Memo> findFixedMemos(String email);

}
