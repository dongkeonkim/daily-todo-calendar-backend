package com.dailytodocalendar.adapter.out.persistence.member;

import com.dailytodocalendar.application.port.out.member.LoadMemberPort;
import com.dailytodocalendar.application.port.out.member.SaveMemberPort;
import com.dailytodocalendar.domain.member.model.Email;
import com.dailytodocalendar.domain.member.model.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** 회원 영속성 어댑터 - 아웃바운드 포트 구현 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MemberPersistenceAdapter implements LoadMemberPort, SaveMemberPort {

  private final MemberRepository memberRepository;
  private final MemberMapper memberMapper;

  /**
   * ID로 회원 조회
   *
   * @param id 회원 ID
   * @return 회원 엔티티 옵셔널
   */
  @Override
  public Optional<Member> loadById(Long id) {
    log.debug("ID로 회원 조회: {}", id);
    return memberRepository.findById(id).map(memberMapper::toDomain);
  }

  /**
   * 이메일로 회원 조회
   *
   * @param email 이메일
   * @return 회원 엔티티 옵셔널
   */
  @Override
  public Optional<Member> loadByEmail(Email email) {
    log.debug("이메일로 회원 조회: {}", email.getValue());
    return memberRepository.findByEmail(email.getValue()).map(memberMapper::toDomain);
  }

  /**
   * 이메일과 삭제 여부로 회원 조회
   *
   * @param email 이메일
   * @param delYn 삭제 여부
   * @return 회원 엔티티 옵셔널
   */
  @Override
  public Optional<Member> loadByEmailAndDelYn(Email email, boolean delYn) {
    log.debug("이메일과 삭제 여부로 회원 조회: {}, {}", email.getValue(), delYn);
    return memberRepository
        .findByEmailAndDelYn(email.getValue(), delYn)
        .map(memberMapper::toDomain);
  }

  /**
   * 회원 저장
   *
   * @param member 회원 엔티티
   * @return 저장된 회원 엔티티
   */
  @Override
  public Member save(Member member) {
    log.debug("회원 저장/수정: ID={}, 이메일={}", member.getId(), member.getEmail().getValue());
    MemberJpaEntity entity = memberMapper.toJpaEntity(member);
    MemberJpaEntity savedEntity = memberRepository.save(entity);
    return memberMapper.toDomain(savedEntity);
  }
}
