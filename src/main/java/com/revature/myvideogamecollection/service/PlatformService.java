package com.revature.myvideogamecollection.service;

import com.revature.myvideogamecollection.model.Platform;
import com.revature.myvideogamecollection.model.User;
import com.revature.myvideogamecollection.repository.PlatformDao;
import com.revature.myvideogamecollection.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("platformService")
public class PlatformService {

    UserDao userDao;
    PlatformDao platformDao;

    @Autowired
    public PlatformService(UserDao userDao, PlatformDao platformDao) {
        this.userDao = userDao;
        this.platformDao = platformDao;
    }

    public List<Platform> getAllPlatforms() {
        try {
            return this.platformDao.findAll();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Platform> getPlatformsByUserId(Integer userId) {
        User user = this.userDao.findById(userId).orElse(null);
        try {
            if(user.getUserId() != null)
                return this.platformDao.findPlatformsByUserOrderByPlatformNameAsc(user);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Platform createPlatform(Platform platform) {
        User user = this.userDao.findById(platform.getUser().getUserId()).orElse(null);
        if(user.getUserId() != null) {
            try {
                platform.setUser(user);
                return this.platformDao.save(platform);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        } else
            return null;
    }

    public Platform editPlatform(Platform platform) {
        Platform target = this.platformDao.findById(platform.getPlatformId()).orElse(null);
        if(target != null) {
            try {
                target.setPlatformName(platform.getPlatformName());
                target.setPlatformUsername(platform.getPlatformUsername());
                return this.platformDao.save(target);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        } else
            return null;
    }

    public Boolean deletePlatform(Integer platformId) {
        Boolean success;
        Platform target = this.platformDao.findById(platformId).orElse(null);
        if(target != null) {
            try {
                this.platformDao.deletePlatform(target.getPlatformId());
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
            }
            return success;
        } else
            return false;
    }

}
