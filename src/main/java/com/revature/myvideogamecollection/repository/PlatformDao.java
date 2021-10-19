package com.revature.myvideogamecollection.repository;

import com.revature.myvideogamecollection.model.Platform;
import com.revature.myvideogamecollection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("platformDao")
@Transactional
public interface PlatformDao extends JpaRepository <Platform, Integer> {
    List<Platform> findPlatformsByUserOrderByPlatformNameAsc(User user);
    @Modifying
    @Query("Delete from Platform where platformId = :platformId")
    void deletePlatform(@Param("platformId") Integer platformId);
}
