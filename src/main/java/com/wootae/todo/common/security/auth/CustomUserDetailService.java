package com.wootae.todo.common.security.auth;

import com.wootae.todo.domain.user.entity.User;
import com.wootae.todo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. DB에서 유저 조회
        User user = userRepository.findByUsername(username);

        // 2. 유저가 없으면 예외 발생 (중요!)
        if (user == null) {
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다: " + username);
        }

        return new CustomUserDetails(userRepository.findByUsername(username));
    }

}
