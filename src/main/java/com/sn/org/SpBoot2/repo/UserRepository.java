package com.sn.org.SpBoot2.repo;

import com.sn.org.SpBoot2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {
    User getByEmail(String email);
}
