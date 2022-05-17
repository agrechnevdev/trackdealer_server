package com.startserver.Repository;

import com.startserver.Entity.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface PeriodRepository extends JpaRepository<Period, Integer> {

    @Query(value = "SELECT per.* FROM period as per " +
            "order by per.id DESC LIMIT 1",
            nativeQuery = true)
    Period getLastPeriod();


    @Query(value = "SELECT max(per.date_end) FROM period as per " +
            "LEFT JOIN track tr on tr.finish_date = per.date_end " +
            "where per.date_end < :finish_date AND " +
            "(SELECT count (tr.id) FROM track as tr WHERE tr.finish_date = :finish_date) > 0",
            nativeQuery = true)
    Date getPrevPeriodDateWithTracks(@Param("finish_date") Date finishDate);
}
