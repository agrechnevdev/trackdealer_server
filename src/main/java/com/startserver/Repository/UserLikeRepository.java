package com.startserver.Repository;

import com.startserver.Entity.Track;
import com.startserver.Entity.User;
import com.startserver.Entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLikeRepository extends JpaRepository<UserLike, Integer> {

    UserLike findByUserIdAndTrackId(User userId, Track trackId);
}
