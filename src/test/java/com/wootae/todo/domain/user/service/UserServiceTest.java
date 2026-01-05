package com.wootae.todo.domain.user.service;

import com.wootae.todo.domain.user.dto.UserDTO;
import com.wootae.todo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class) // 1. Mockito 프레임워크를 쓰겠다고 선언
class UserServiceTest {

    @Mock // 가짜 리포지토리 (DB 연결 안 함)
    private UserRepository userRepository;

    @Mock // 가짜 암호화기
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks // 가짜들을 주입받을 진짜 서비스 객체
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void join_success() {
        // given (준비)
        UserDTO.JoinRequest request = UserDTO.JoinRequest.builder()
                .username("testUser")
                .password("1234")
                .nickname("코딩왕")
                .build();

        // "DB에 testUser가 있니?"라고 물어보면 "없어(false)"라고 대답해! 라고 가짜 설정
        given(userRepository.existsByUsername("testUser")).willReturn(false);
        // "암호화 해줘"라고 하면 "encoded_pw"를 줘! 라고 가짜 설정
        given(bCryptPasswordEncoder.encode("1234")).willReturn("encoded_pw");

        // when (실행)
        userService.join(request);

        // then (검증)
        // 1. save 메서드가 딱 1번 실행되었는지 확인
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("중복 회원가입 실패 테스트")
    void join_fail_duplicate() {
        // given (준비)
        UserDTO.JoinRequest request = UserDTO.JoinRequest.builder()
                .username("duplicateUser")
                .password("1234")
                .nickname("중복왕")
                .build();

        // "DB에 duplicateUser가 있니?"라고 물어보면 "응 있어(true)"라고 대답해!
        given(userRepository.existsByUsername("duplicateUser")).willReturn(true);

        // when & then (실행 및 에러 검증)
        // 이 코드를 실행했을 때 IllegalArgumentException이 터져야 성공!
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.join(request);
        });

        // 에러 메시지가 우리가 설정한 것과 같은지 확인
        assertEquals("동일 아이디가 존재합니다.", exception.getMessage());

        // 중요: 실패했으니까 save(저장)는 절대 호출되면 안 됨!
        verify(userRepository, times(0)).save(any());
    }

}
