package moais.todolist.member.application;

import lombok.RequiredArgsConstructor;
import moais.todolist.global.auth.application.usecase.CreateTokenUseCase;
import moais.todolist.global.auth.domain.Token;
import moais.todolist.global.auth.presentation.dto.request.CreateUserAccountEvent;
import moais.todolist.global.auth.presentation.dto.request.DeleteUserAccountEvent;
import moais.todolist.member.application.dto.request.SignInRequestDto;
import moais.todolist.member.application.dto.request.SignUpRequestDto;
import moais.todolist.member.application.dto.request.WithdrawRequestDto;
import moais.todolist.member.application.dto.response.SignInResponseDto;
import moais.todolist.member.application.usecase.SignInUseCase;
import moais.todolist.member.application.usecase.SignUpUseCase;
import moais.todolist.member.application.usecase.WithdrawUseCase;
import moais.todolist.member.domain.Member;
import moais.todolist.member.exception.ErrorMessage;
import moais.todolist.member.persistence.MemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements SignUpUseCase, SignInUseCase, WithdrawUseCase {

    private final MemberRepository memberRepository;

    private final CreateTokenUseCase createTokenUseCase;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void signUp(SignUpRequestDto requestDto) {
        if (memberRepository.findMemberByLoginIdAndDeleteYnIsFalse(requestDto.loginId()).isPresent()) {
            throw new IllegalStateException(ErrorMessage.ALREADY_EXIST_LOGIN_ID.getMessage());
        }
        Member member = requestDto.toEntity();
        memberRepository.save(member);

        // 회원가입 이벤트 발행 -> UserAccount 생성
        eventPublisher.publishEvent(new CreateUserAccountEvent(member.getId()));
    }

    @Override
    public SignInResponseDto signIn(SignInRequestDto requestDto) {
        Member member = memberRepository.findMemberByLoginIdAndDeleteYnIsFalse(requestDto.loginId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER.getMessage()));
        String memberId = member.signIn(requestDto.password());
        Token token = createTokenUseCase.create(memberId);
        return new SignInResponseDto(memberId, token);
    }

    @Override
    @Transactional
    public void withdraw(WithdrawRequestDto requestDto, String payload) {
        if (ObjectUtils.isEmpty(payload)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_PAYLOAD.getMessage());
        }
        Member member = memberRepository.findMemberByLoginIdAndDeleteYnIsFalse(requestDto.loginId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER.getMessage()));

        // 토큰의 payload와 비교
        member.withdraw(payload, requestDto.loginId(), requestDto.password());

        // 회원 탈퇴시 UserAccount 삭제 이벤트 발행
        eventPublisher.publishEvent(new DeleteUserAccountEvent(member.getId()));
    }
}
