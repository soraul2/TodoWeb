package com.wootae.todo.domain.user.service;

import com.wootae.todo.domain.user.dto.UserDTO;
import com.wootae.todo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void join(UserDTO.JoinRequest joinRequest) {

        //1. 동일 아이디 조회
        if (userRepository.existsByUsername(joinRequest.getUsername())) {
            throw new IllegalArgumentException("동일 아이디가 존재합니다.");
        }

        String encodePassword = bCryptPasswordEncoder.encode(joinRequest.getPassword());

        //2. 가입 진행
        userRepository.save(joinRequest.toEntity(encodePassword));
    }

}
