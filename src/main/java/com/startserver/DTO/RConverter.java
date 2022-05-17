package com.startserver.DTO;

import com.startserver.Entity.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RConverter {

    public static List<RTrack> convert(List<Track> trackList, Integer userId) {
        if (trackList != null) {
            List<RTrack> rTrackList = new ArrayList<>();
            for (Track track : trackList) {
                rTrackList.add(convert(track, userId));
            }
            return rTrackList;
        } else
            return new ArrayList<>();
    }

    public static RTrack convert(Track track, Integer userId) {
        RTrack rTrack = new RTrack();
        if (track != null) {
            rTrack.id = track.getId();
            rTrack.deezerId = track.getDeezerId();
            rTrack.title = track.getTitle();
            rTrack.artist = track.getArtist();
            rTrack.duration = track.getDuration();
            rTrack.genre = track.getGenre();
            rTrack.coverImage = track.getCoverImage();
            rTrack.countLike = track.getCountLike();
            rTrack.countDislike = track.getCountDislike();
            rTrack.userNameLoad = track.getUserNameLoad();
            rTrack.userStatusLoad = track.getUserStatusLoad();
            if (track.getFinishDate() != null)
                rTrack.finishDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(track.getFinishDate());
            if (userId != null)
                rTrack.userLike = track.currentUserLike(userId);
            rTrack.first = track.getFirst();
            return rTrack;
        } else {
            rTrack.id = -1;
        }
        return rTrack;
    }
}
