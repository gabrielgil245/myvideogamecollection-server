package com.gabrielgil.myvideogamecollection.repository;

import com.gabrielgil.myvideogamecollection.model.Game;
import com.gabrielgil.myvideogamecollection.model.Platform;
import com.gabrielgil.myvideogamecollection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("gameDao")
@Transactional
public interface GameDao extends JpaRepository <Game, Integer> {
    List<Game> findGamesByUser(User user);
    List<Game> findGamesByPlatform(Platform platform);
    @Modifying
    @Query("Delete from Game where gameId = :gameId")
    void deleteGame(@Param("gameId") Integer gameId);

}
