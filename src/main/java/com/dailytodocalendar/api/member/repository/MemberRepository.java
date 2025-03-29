package com.dailytodocalendar.api.member.repository;

import com.dailytodocalendar.api.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

  Optional<Member> findByEmail(String email);

  Optional<Member> findByEmailAndDelYn(String email, boolean delYn);
}
