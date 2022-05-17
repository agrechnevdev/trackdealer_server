package com.startserver.Repository;

import com.startserver.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u where u.username like :username")
    User findUserByUsername(@Param("username") String username);

    @Modifying
    @Query(value = "UPDATE profile SET status = :newStatus " +
            "WHERE id IN (SELECT tr.user_finish_id FROM track AS tr WHERE tr.finish_date = :times) ",
            nativeQuery = true)
//    @Query("update Track tr set tr.finishDate = :times, tr")
    void updateStatuses(@Param("times") Date times, @Param("newStatus") String newStatus);

    @Modifying
    @Query(value = "UPDATE profile SET status = :newStatus WHERE id IN (:userid) ",
            nativeQuery = true)
    void updateStatus(@Param("userid") Integer userid, @Param("newStatus") String newStatus);

    @Query(value = "SELECT * FROM profile WHERE id IN (SELECT user_finish_id FROM track WHERE finish_date = :times)",
            nativeQuery = true)
    List<User> getUsersForPeriodDate(@Param("times") Date times);

}

