package com.dailytodocalendar.application.port.out.member;

import com.dailytodocalendar.domain.member.model.Email;
import com.dailytodocalendar.domain.member.model.Member;
import java.util.Optional;

/** 회원 조회 포트 - 아웃바운드 포트 */
public interface LoadMemberPort {

  /**
   * ID로 회원 조회
   *
   * @param id 회원 ID
   * @return 회원 엔티티 옵셔널
   */
  Optional<Member> loadById(Long id);

  /**
   * 이메일로 회원 조회
   *
   * @param email 이메일
   * @return 회원 엔티티 옵셔널
   */
  Optional<Member> loadByEmail(Email email);

  /**
   * 이메일과 삭제 여부로 회원 조회
   *
   * @param email 이메일
   * @param delYn 삭제 여부
   * @return 회원 엔티티 옵셔널
   */
  Optional<Member> loadByEmailAndDelYn(Email email, boolean delYn);
}
