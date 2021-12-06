package com.gabrielgil.myvideogamecollection.service;

import com.gabrielgil.myvideogamecollection.model.User;
import com.gabrielgil.myvideogamecollection.repository.UserDao;
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

    // NEEDS PASSWORD DECRYPTION
    public User getUserByUsernameAndPassword(String username, String password) {
        return this.userDao.findUserByUsernameAndPassword(username, password);
    }

    public User getUserByEmail(String email) {
        return this.userDao.findUserByEmail(email);
    }

    public User createUser(User user) {
        User existingUser = this.userDao.findUserByUsername(user.getUsername());
        if(existingUser != null)
            return null;
        existingUser = this.userDao.findUserByEmail(user.getEmail());
        if(existingUser != null)
            return null;
        return this.userDao.save(user);
    }

    public User editUser(User user) {
        User currentUser = this.userDao.findById(user.getUserId()).orElse(null);
        if(currentUser == null)
            return null;
        else { // User exists
            if(user.getUsername() != null)
                currentUser.setUsername(user.getUsername());
            if(user.getPassword() != null && user.getPassword().length() >= 8)
                currentUser.setPassword(user.getPassword());
            if(user.getFirstName() != null)
                currentUser.setFirstName(user.getFirstName());
            if(user.getLastName() != null)
                currentUser.setLastName(user.getLastName());
            if(user.getEmail() != null)
                currentUser.setEmail(user.getEmail());
            if(user.getBirthday() != null)
                currentUser.setBirthday(user.getBirthday());
            if(user.getAboutMe() != null)
                currentUser.setAboutMe(user.getAboutMe());
            if(user.getProfilePic() != null)
                currentUser.setProfilePic(user.getProfilePic());
        }
        return this.userDao.save(currentUser);
    }

    public User findUserById(Integer userId) {
        return this.userDao.findById(userId).orElse(null);
    }

    public Boolean deleteUser(User user) {
        Boolean success;
        try {
            this.userDao.delete(user);
            success = true;
        } catch(Exception exception) {
            exception.printStackTrace();
            success = false;
        }
        return success;
    }
}
