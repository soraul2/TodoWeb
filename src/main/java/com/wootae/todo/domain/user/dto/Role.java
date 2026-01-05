package com.wootae.todo.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "USER"),
    ADMIN("ROLE_ADMIN", "ADMIN");

    private final String key;
    private final String title;

}
