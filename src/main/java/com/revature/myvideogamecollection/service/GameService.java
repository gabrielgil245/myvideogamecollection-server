package com.revature.myvideogamecollection.service;

import com.revature.myvideogamecollection.model.Game;
import com.revature.myvideogamecollection.model.Platform;
import com.revature.myvideogamecollection.model.User;
import com.revature.myvideogamecollection.repository.GameDao;
import com.revature.myvideogamecollection.repository.PlatformDao;
import com.revature.myvideogamecollection.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("gameService")
public class GameService {
    UserDao userDao;
    PlatformDao platformDao;
    GameDao gameDao;

    @Autowired
    public GameService(UserDao userDao, PlatformDao platformDao, GameDao gameDao) {
        this.userDao = userDao;
        this.platformDao = platformDao;
        this.gameDao = gameDao;
    }

    public List<Game> getAllGames() {
        try {
            return this.gameDao.findAll();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Game> getGamesByUserId(Integer userId) {
        User user = this.userDao.findById(userId).orElse(null);
        try {
            if(user.getUserId() != null)
                return this.gameDao.findGamesByUser(user);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Game> getGamesByPlatformId(Integer userId, Integer platformId) {
        User user = this.userDao.findById(userId).orElse(null);
        Platform platform = this.platformDao.findById(platformId).orElse(null);
        try {
            if(platform.getUser().getUserId() == user.getUserId())
                return this.gameDao.findGamesByUserAndPlatform(user, platform);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Game> getGamesByUserIdAndPlatformId(Integer userId, Integer platformId) {
        User user = this.userDao.findById(userId).orElse(null);
        Platform platform = this.platformDao.findById(platformId).orElse(null);
        try {
            if(user.getUserId() != null && platform.getPlatformId() != null)
                return this.gameDao.findGamesByUserAndPlatform(user, platform);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Game createGame(Game game) {
        User user = this.userDao.findById(game.getUser().getUserId()).orElse(null);
        Platform platform = this.platformDao.findById(game.getPlatform().getPlatformId()).orElse(null);
        if(user.getUserId() != null && platform.getPlatformId() != null) {
            try {
                game.setUser(user);
                game.setPlatform(platform);
                return this.gameDao.save(game);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        } else
            return null;
    }

    public Game editGame(Game game) {
        Game target = this.gameDao.findById(game.getGameId()).orElse(null);
        if(target != null) {
            try {
                target.setGameName(game.getGameName());
                target.setGameStatus(game.getGameStatus());
                return this.gameDao.save(target);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        } else
            return null;
    }

    public Boolean deleteGame(Integer gameId) {
        Boolean success;
        Game target = this.gameDao.findById(gameId).orElse(null);
        if(target != null) {
            try {
                this.gameDao.deleteGame(target.getGameId());
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
