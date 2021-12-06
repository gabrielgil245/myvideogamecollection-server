package com.gabrielgil.myvideogamecollection.repository;

import com.gabrielgil.myvideogamecollection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("userDao")
@Transactional
public interface UserDao extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);
    User findUserByUsernameAndPassword(String username, String password);
    User findUserByEmail(String email);
}
