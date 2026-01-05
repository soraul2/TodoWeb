package com.wootae.todo.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "USER"),
    ADMIN("ROLE_ADMIN", "ADMIN");

    private final String key;
    private final String title;

    // ★ 이 메서드를 추가해 주세요!
    public static Role findByKey(String key) {
        return Arrays.stream(Role.values())
                .filter(r -> r.getKey().equals(key))
                .findAny()
                .orElse(Role.USER); // 만약 일치하는 게 없으면 기본값으로 USER를 줌 (혹은 예외 처리)
    }

}
