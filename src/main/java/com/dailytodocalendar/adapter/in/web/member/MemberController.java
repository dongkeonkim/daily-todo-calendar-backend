package com.dailytodocalendar.adapter.in.web.member;

import com.dailytodocalendar.adapter.in.web.common.CurrentUser;
import com.dailytodocalendar.adapter.in.web.common.WebAdapter;
import com.dailytodocalendar.adapter.in.web.member.dto.MemberUpdateRequest;
import com.dailytodocalendar.adapter.in.web.member.dto.MemberWithdrawRequest;
import com.dailytodocalendar.application.port.in.auth.KakaoAuthUseCase;
import com.dailytodocalendar.application.port.in.member.GetMemberUseCase;
import com.dailytodocalendar.application.port.in.member.UpdateMemberUseCase;
import com.dailytodocalendar.application.port.in.member.WithdrawMemberUseCase;
import com.dailytodocalendar.application.port.in.member.dto.MemberInfoDto;
import com.dailytodocalendar.application.port.in.member.dto.UpdateMemberCommand;
import com.dailytodocalendar.application.port.in.member.dto.WithdrawMemberCommand;
import com.dailytodocalendar.common.codes.SuccessCode;
import com.dailytodocalendar.common.response.ResponseDto;
import com.dailytodocalendar.infrastructure.config.security.custom.CustomUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/** 회원 컨트롤러 - 인바운드 어댑터 */
@Slf4j
@WebAdapter
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

  private final GetMemberUseCase getMemberUseCase;
  private final UpdateMemberUseCase updateMemberUseCase;
  private final WithdrawMemberUseCase withdrawMemberUseCase;
  private final MemberDtoMapper memberDtoMapper;
  private final KakaoAuthUseCase kakaoAuthUseCase;

  /**
   * 회원 정보 조회
   *
   * @param customUser 인증된 사용자 정보
   * @return 회원 정보 응답
   */
  @GetMapping("/info")
  public ResponseDto<MemberInfoDto> userInfo(@CurrentUser CustomUser customUser) {
    log.debug("회원 정보 조회 요청 - 이메일: {}", customUser.getUsername());
    MemberInfoDto memberInfo = getMemberUseCase.getMemberByEmail(customUser.getUsername());
    return ResponseDto.success(SuccessCode.SUCCESS, memberInfo);
  }

  /**
   * 회원 정보 수정
   *
   * @param customUser 인증된 사용자 정보
   * @param request 회원 정보 수정 요청
   * @return 응답 객체
   */
  @PutMapping("/update")
  public ResponseDto<Void> update(
      @CurrentUser CustomUser customUser, @Valid @RequestBody MemberUpdateRequest request) {

    log.debug("회원 정보 수정 요청 - 이메일: {}", customUser.getUsername());

    UpdateMemberCommand command = memberDtoMapper.toUpdateMemberCommand(request);
    updateMemberUseCase.updateMember(customUser.getUsername(), command);

    return ResponseDto.success(SuccessCode.UPDATED);
  }

  /**
   * 회원 탈퇴
   *
   * @param request 회원 탈퇴 요청
   * @return 응답 객체
   */
  @PutMapping("/delete")
  public ResponseDto<Void> delete(@Valid @RequestBody MemberWithdrawRequest request) {
    log.debug("회원 탈퇴 요청 - 이메일: {}", request.email());

    WithdrawMemberCommand command = memberDtoMapper.toWithdrawMemberCommand(request);
    withdrawMemberUseCase.withdrawMember(command);

    return ResponseDto.success(SuccessCode.DELETED);
  }

  /**
   * 카카오 계정 연결 해제 및 회원 탈퇴
   *
   * @return 처리 결과
   */
  @PostMapping("/unlink")
  public ResponseDto<Void> unlinkKakao() {
    kakaoAuthUseCase.unlinkKakaoAccount();
    return ResponseDto.success(SuccessCode.DELETED);
  }
}
