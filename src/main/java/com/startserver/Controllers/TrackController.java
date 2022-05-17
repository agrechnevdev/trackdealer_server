package com.startserver.Controllers;

import com.startserver.DTO.RConverter;
import com.startserver.DTO.RTrack;
import com.startserver.Entity.Track;
import com.startserver.Entity.User;
import com.startserver.Entity.UserLike;
import com.startserver.Repository.CookieRepository;
import com.startserver.Repository.PeriodRepository;
import com.startserver.Repository.TrackRepository;
import com.startserver.Repository.UserLikeRepository;
import com.startserver.responseUtils.MessageExeption;
import com.startserver.utils.StaticUtils;
import com.startserver.utils.Texts;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    final Integer PAGE_SIZE = 20;
    final Integer RANDOM_LIMIT = 20;
    @Inject
    private TrackRepository trackRepository;

    @Inject
    private CookieRepository cookieRepository;

    @Inject
    private UserLikeRepository userLikeRepository;

    @Inject
    private PeriodRepository periodRepository;

    @GetMapping(value = "/favtrack", produces = "application/json")
    public ResponseEntity<?> getFavTrack(HttpServletRequest request) throws MessageExeption {
        User userId = StaticUtils.getUserIdBySessionId(cookieRepository, request.getCookies());
        Track favTrack = trackRepository.findByUserLoadId(userId);
        RTrack rTrack = RConverter.convert(favTrack, userId.getId());
        return ResponseEntity.ok().body(rTrack);
    }

    @Transactional
    @PostMapping(value = "/change", produces = "application/json")
    public ResponseEntity<?> changeFavTrack(HttpServletRequest request, @Valid @RequestBody Track newTrack) throws MessageExeption {
        User userId = StaticUtils.getUserIdBySessionId(cookieRepository, request.getCookies());

        Track trackByDeezerId = trackRepository.findByDeezerId(newTrack.getDeezerId());
        if (trackByDeezerId != null && trackByDeezerId.getUserLoadId() != null) {
            throw new MessageExeption(Texts.TRACK_ALREADY_USED.getText(StaticUtils.getLang(request.getCookies())));
        }

        // ищем предыдущую выбранную песню этим пользователем, и если нашли - стираем userId и стату по лайкам
        Track oldFavTrack = trackRepository.findByUserLoadId(userId);
        if (oldFavTrack != null) {
            if (oldFavTrack.getFinishDate() == null) {
                oldFavTrack.setCountLike(0);
                oldFavTrack.setCountDislike(0);
            }
            oldFavTrack.setUserLoadId(null);
            trackRepository.save(oldFavTrack);
        }

        // ищем песню без пользователя по deezerId или сохраняем новую выбранную песню этим пользователем
        Track trackSave;
        if (trackByDeezerId != null) {
            trackByDeezerId.setCountLike(0);
            trackByDeezerId.setCountDislike(0);
            trackByDeezerId.setUserLoadId(userId);
            trackSave = trackRepository.save(trackByDeezerId);
        } else {
            newTrack.setCountLike(0);
            newTrack.setCountDislike(0);
            newTrack.setUserLoadId(userId);
            trackSave = trackRepository.save(newTrack);
        }
        RTrack rTrack = RConverter.convert(trackSave, userId.getId());
        return ResponseEntity.ok().body(rTrack);
    }

    @GetMapping(value = "/list", produces = "application/json")
    public ResponseEntity<?> getTrackList(HttpServletRequest request, @RequestParam("lastNum") int lastNum, @RequestParam("genre") String genre) throws MessageExeption {
        User user = StaticUtils.getUserIdBySessionId(cookieRepository, request.getCookies());
        List<Track> list;
        int page = lastNum / PAGE_SIZE;
        int size = lastNum % PAGE_SIZE == 0 ? PAGE_SIZE : lastNum % PAGE_SIZE;
        if (genre != null && !genre.equals("Все")) {
            list = trackRepository.getTopTracksByGenreAndLikes(lastNum, size, genre);
        } else {
            list = trackRepository.getTopTracksByLikes(lastNum, size);
        }
        List<RTrack> trackResponseList = RConverter.convert(list, user.getId());
        if (size == PAGE_SIZE)
            return ResponseEntity.ok().body(trackResponseList);
        else
            return ResponseEntity.ok().body(new ArrayList<>());
    }

    @GetMapping(value = "/randomlist", produces = "application/json")
    public ResponseEntity<?> getRandomTrackList(HttpServletRequest request, @RequestParam("genre") String genre) throws MessageExeption {
        User user = StaticUtils.getUserIdBySessionId(cookieRepository, request.getCookies());
        List<Track> list;
        int page = 0;
        int size = RANDOM_LIMIT;
        if (genre != null && !genre.equals("Все")) {
            list = trackRepository.getRandomTracksByGenre(genre, RANDOM_LIMIT);
        } else {
            list = trackRepository.getRandomTracks(RANDOM_LIMIT);
        }
        List<RTrack> trackResponseList = RConverter.convert(list, user.getId());
        return ResponseEntity.ok().body(trackResponseList);
    }

    @GetMapping(value = "/userlist", produces = "application/json")
    public ResponseEntity<?> getUserTrackList(HttpServletRequest request, @RequestParam("username") String username) throws MessageExeption {
        User user = StaticUtils.getUserIdBySessionId(cookieRepository, request.getCookies());
        List<Track> list = trackRepository.getUserTracks(username);
        List<RTrack> trackResponseList = RConverter.convert(list, user.getId());
        return ResponseEntity.ok().body(trackResponseList);
    }

    @GetMapping(value = "/periodsList", produces = "application/json")
    public ResponseEntity<?> getPeriodsList(HttpServletRequest request, @RequestParam("date") String date) throws MessageExeption, ParseException {
        Date dateParse = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(date);
        List<Track> list = trackRepository.getPeriodTracksByDate(dateParse);
        list = StaticUtils.formatListForPeriod(list);
        List<RTrack> trackResponseList;
        if(list != null && !list.isEmpty())
            trackResponseList = RConverter.convert(list, null);
        else
            trackResponseList = new ArrayList<>();
        return ResponseEntity.ok().body(trackResponseList);
    }


    @Transactional
    @GetMapping(value = "/like", produces = "application/json")
    public ResponseEntity<?> likeTrack(HttpServletRequest request, @RequestParam("deezerId") Integer deezerId, @RequestParam("like") Boolean like) throws MessageExeption {
        User userId = StaticUtils.getUserIdBySessionId(cookieRepository, request.getCookies());
        Track track = trackRepository.findByDeezerId(deezerId);
        UserLike userLike = userLikeRepository.findByUserIdAndTrackId(userId, track);
        if (userLike == null) {
            if (like) {
                track.setCountLike(track.getCountLike() + 1);
            } else {
                track.setCountDislike(track.getCountDislike() + 1);
            }
            trackRepository.save(track);

            UserLike newUserLike = new UserLike();
            newUserLike.setUserId(userId);
            newUserLike.setTrackId(track);
            newUserLike.setUserike(like);
            userLikeRepository.save(newUserLike);
        }
        return ResponseEntity.ok().build();
    }

    @Transactional
    @GetMapping(value = "/updatelike", produces = "application/json")
    public ResponseEntity<?> updateeLikeTrack(HttpServletRequest request, @RequestParam("deezerId") Integer deezerId, @RequestParam("like") Boolean like) throws MessageExeption {
        User userId = StaticUtils.getUserIdBySessionId(cookieRepository, request.getCookies());
        Track track = trackRepository.findByDeezerId(deezerId);
        UserLike userLike = userLikeRepository.findByUserIdAndTrackId(userId, track);
        if (userLike != null) {
            if (like) {
                track.setCountDislike(track.getCountDislike() - 1);
                track.setCountLike(track.getCountLike() + 1);
            } else {
                track.setCountLike(track.getCountLike() - 1);
                track.setCountDislike(track.getCountDislike() + 1);
            }
            trackRepository.save(track);

            userLike.setUserike(like);
            userLikeRepository.save(userLike);
        }
        return ResponseEntity.ok().build();
    }


    @Transactional
    @GetMapping(value = "/deletelike", produces = "application/json")
    public ResponseEntity<?> deleteLikeTrack(HttpServletRequest request, @RequestParam("deezerId") Integer deezerId, @RequestParam("like") Boolean like) throws MessageExeption {
        User userId = StaticUtils.getUserIdBySessionId(cookieRepository, request.getCookies());
        Track track = trackRepository.findByDeezerId(deezerId);
        UserLike userLike = userLikeRepository.findByUserIdAndTrackId(userId, track);
        if (userLike != null) {
            if (like) {
                track.setCountLike(track.getCountLike() - 1);
            } else {
                track.setCountDislike(track.getCountDislike() - 1);
            }
            trackRepository.save(track);
            userLikeRepository.delete(userLike);
        }
        return ResponseEntity.ok().build();
    }

}
