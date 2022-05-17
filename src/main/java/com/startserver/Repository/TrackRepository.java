package com.startserver.Repository;

import com.startserver.Entity.Track;
import com.startserver.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Integer> {

    Track findByDeezerId(Integer deezerId);

    Track findByUserLoadId(User userId);

    @Query(value =  "SELECT * FROM (SELECT DISTINCT tr.* " +
                    "FROM track AS tr " +
            "LEFT JOIN profile u ON tr.user_load_id = u.id " +
            "LEFT JOIN userlike ul ON tr.id = ul.track_id " +
            "WHERE finish_date IS NULL AND user_load_id IS NOT NULL " +
            "ORDER BY tr.id) as u " +
            "order by (count_like-count_dislike) DESC, count_like DESC, id  OFFSET :offset LIMIT :limit ",
//            "SELECT tr.* FROM track as tr " +
//            "LEFT JOIN profile u on tr.user_load_id = u.id " +
//            "LEFT JOIN userlike ul on tr.id = ul.track_id " +
//            "WHERE tr.finish_date is null " +
//            "AND tr.user_load_id is not null " +
//            "order by (tr.count_like-tr.count_dislike) DESC OFFSET :offset LIMIT :limit ",
            nativeQuery = true)
    List<Track> getTopTracksByLikes(@Param("offset") Integer offset, @Param("limit") Integer limit);

    @Query(value = "SELECT tr.* FROM track as tr " +
            "LEFT JOIN profile u on tr.user_load_id = u.id " +
            "LEFT JOIN userlike ul on tr.id = ul.track_id " +
            "WHERE tr.finish_date is NULL AND tr.genre LIKE :genre " +
            "AND tr.user_load_id is not null " +
            "order by (tr.count_like-tr.count_dislike) DESC OFFSET :offset LIMIT :limit",
            nativeQuery = true)
    List<Track> getTopTracksByGenreAndLikes(@Param("offset") Integer offset, @Param("limit") Integer limit, @Param("genre") String genre);

    @Query(value = "SELECT * FROM (SELECT DISTINCT tr.* " +
                    "FROM track AS tr " +
            "LEFT JOIN profile u ON tr.user_load_id = u.id " +
            "LEFT JOIN userlike ul ON tr.id = ul.track_id " +
            "WHERE finish_date IS NULL AND user_load_id IS NOT NULL " +
            "ORDER BY tr.id) as u " +
            "ORDER BY random() " +
            "LIMIT :limit ",
            nativeQuery = true)
    List<Track> getRandomTracks(@Param("limit") Integer limit);

    @Query(value = "SELECT * FROM (SELECT DISTINCT tr.* " +
            "FROM track AS tr " +
            "LEFT JOIN profile u ON tr.user_load_id = u.id " +
            "LEFT JOIN userlike ul ON tr.id = ul.track_id " +
            "WHERE finish_date IS NULL AND user_load_id IS NOT NULL AND tr.genre LIKE :genre " +
            "ORDER BY tr.id) as u " +
            "ORDER BY random() " +
            "LIMIT :limit ",
            nativeQuery = true)
    List<Track> getRandomTracksByGenre(@Param("genre") String genre, @Param("limit") Integer limit);

    @Modifying
    @Query(value = " UPDATE track " +
            "SET finish_date = :times, user_finish_id = user_load_id " +
            "WHERE " +
            "id IN ( " +
            "SELECT newtr.id " +
            "FROM track AS newtr " +
            "WHERE " +
            "newtr.finish_date IS NULL " +
            "AND newtr.user_load_id IS NOT NULL " +
            "ORDER BY (newtr.count_like - newtr.count_dislike) DESC " +
            "LIMIT :size)",
            nativeQuery = true)
    void finishPeriod(@Param("times") Date times, @Param("size") Integer size);

    @Query(value = "SELECT count(tr.id) FROM track as tr " +
            "WHERE tr.finish_date is null " +
            "AND tr.user_load_id is not null ",
            nativeQuery = true)
    Integer countAllTracksInChart();

//    @Query(value = "SELECT DISTINCT tr.* FROM track as tr " +
//            "LEFT JOIN user u on tr.user_load_id = u.id " +
//            "LEFT JOIN user us on tr.user_finish_id = us.id " +
//            "LEFT JOIN userlike ul on tr.id = ul.track_id " +
//            "WHERE tr.finish_date = :finish_date "+
//            "order by (tr.count_like-tr.count_dislike) DESC ",
//            nativeQuery = true)
//    List<Track> getPeriodTracksByDate(@Param("finish_date") Date finishDate);

    @Query(value = "SELECT t.* FROM track as t " +
            "WHERE t.finish_date = " +
            "(SELECT max(tr.finish_date) FROM track as tr " +
            "INNER JOIN period as p on p.date_end = tr.finish_date " +
            "where tr.finish_date < :finish_date)",
            nativeQuery = true)
    List<Track> getPeriodTracksByDate(@Param("finish_date") Date finishDate);

    @Query(value = "SELECT DISTINCT tr.* FROM track as tr " +
            "LEFT JOIN profile u on tr.user_load_id = u.id " +
            "LEFT JOIN profile ul on tr.user_finish_id = ul.id " +
            "LEFT JOIN userlike uslike on tr.id = uslike.track_id " +
            "WHERE u.username LIKE :username " +
            "OR ul.username LIKE :username " +
            "ORDER BY tr.id ",
            nativeQuery = true)
    List<Track> getUserTracks(@Param("username") String username);


}
