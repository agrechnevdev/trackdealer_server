package com.startserver.Repository;

import com.startserver.Entity.Cookie;
import com.startserver.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by anton on 07.11.2017.
 */
public interface CookieRepository extends JpaRepository<Cookie, Integer> {

    @Query("SELECT coo.userId FROM Cookie coo where coo.sessionId like :session_id")
    User findUserIdBySessionId(@Param("session_id") String session_id);

    Cookie findBySessionId(String session_id);

}
