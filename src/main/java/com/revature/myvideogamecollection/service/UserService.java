package com.revature.myvideogamecollection.service;

import com.revature.myvideogamecollection.model.User;
import com.revature.myvideogamecollection.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserService {
    UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return this.userDao.findAll();
    }

    public User getUserByUsername(String username) {
        return this.userDao.findUserByUsername(username);
    }

    public User getUserByEmail(String email) {
        return this.userDao.findUserByEmail(email);
    }

    public User createUser(User user) {
        User existingUser = this.userDao.findUserByUsername(user.getUsername());
        if(existingUser != null) return null;
        existingUser = this.userDao.findUserByEmail(user.getEmail());
        if(existingUser != null) return null;
        return this.userDao.save(user);
    }

    public User editUser(User user) {
        return this.userDao.save(user);
    }

    public Boolean deleteUser(User user) {
        Boolean success;
        try {
            this.userDao.delete(user);
            success = true;
        } catch(Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

}
