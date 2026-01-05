package com.wootae.todo.domain.user.repository;

import com.wootae.todo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    //[회원가입] 같은 아이디가 존재하는 가?
    boolean existsByUsername(String username);

    //[로그인]
    User findByUsername(String username);
}
