package com.startserver.Controllers;

import com.startserver.Entity.Period;
import com.startserver.Entity.User;
import com.startserver.Repository.PeriodRepository;
import com.startserver.Repository.TrackRepository;
import com.startserver.Repository.UserRepository;
import com.startserver.responseUtils.MessageExeption;
import com.startserver.utils.TStatus;
import com.startserver.utils.Texts;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/period")
public class PeriodController {

    final String KEY = "0552c62a-edf6-40c9-a25b-621cac4d931e";
    int SIZE_VERY_SMALL = 1;
    int SIZE_SMALL = 2;
    int SIZE_MEDIUM = 10;
    int SIZE_BIG = 20;

    @Inject
    private PeriodRepository periodRepository;

    @Inject
    private TrackRepository trackRepository;

    @Inject
    private UserRepository userRepository;

    @Transactional
    @GetMapping(value = "/end", produces = "application/json")
    public ResponseEntity<?> getFavTrack(@RequestParam("key") String key, HttpServletRequest request) throws MessageExeption {
        if (key.equals(KEY)) {
            Period lastPeriod = periodRepository.getLastPeriod();
            Calendar calendar = Calendar.getInstance();
            if (lastPeriod == null || (calendar.getTime().getTime() - lastPeriod.getDateEnd().getTime()) / (1000 * 60 * 60 * 24) > 6) {

                Period period = new Period();
                period.setDateEnd(calendar.getTime());
                Period periodSave = periodRepository.save(period);
                Integer countAllTracks = trackRepository.countAllTracksInChart();
                if (countAllTracks != 0) {
                    int size;
                    if (countAllTracks > 0 && countAllTracks < 20)
                        size = SIZE_VERY_SMALL;
                    else if (countAllTracks >= 20 && countAllTracks < 100)
                        size = SIZE_SMALL;
                    else if (countAllTracks >= 100 && countAllTracks < 400)
                        size = SIZE_MEDIUM;
                    else
                        size = SIZE_BIG;
                    trackRepository.finishPeriod(period.getDateEnd(), size);
                }
                List<User> list = userRepository.getUsersForPeriodDate(period.getDateEnd());
                for (User user : list) {
                    userRepository.updateStatus(user.getId(), TStatus.getUpgradedStatus(user.getStatus(), false));
                }
                return ResponseEntity.ok().build();
            } else {
                throw new MessageExeption(Texts.WRONG_TIME.getText("ru"));
            }

        } else {
            throw new MessageExeption(Texts.TRY_HARDER.getText("ru"));
        }
    }
}
