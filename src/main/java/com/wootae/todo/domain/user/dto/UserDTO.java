package com.wootae.todo.domain.user.dto;
import com.wootae.todo.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDTO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JoinRequest{

        @NotBlank(message = "아이디를 입력해주세요.")
        private String username;
        @NotBlank(message = "비밀번호를 입력해주세요")
        private String password;
        @NotBlank(message = "닉네임을 입력해주세요.")
        private String nickname;

        public User toEntity(String encodedPassword) {
            return User.builder()
                    .username(this.username)
                    .password(encodedPassword)
                    .nickname(this.nickname)
                    .role(Role.USER)
                    .build();
        }
    }

}
