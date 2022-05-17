package com.startserver.utils;

import com.startserver.DTO.RTrack;
import com.startserver.Entity.Track;
import com.startserver.Entity.User;
import com.startserver.Repository.CookieRepository;
import com.startserver.responseUtils.MessageExeption;

import javax.servlet.http.Cookie;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by anton on 07.11.2017.
 */
public class StaticUtils {

    public final static String sessionName = "TD_SESSION_ID";
    public final static String lang = "TD_LANG";

    public static String createCookie() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static String getSessionId(Cookie[] cookies) {
        String stringCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(StaticUtils.sessionName)) {
                stringCookie = cookie.getValue();
                break;
            }
        }
        return stringCookie;
    }

    public static String getLang(Cookie[] cookies) {
        String stringCookie = "ru";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(StaticUtils.lang)) {
                stringCookie = cookie.getValue();
                break;
            }
        }
        return stringCookie;
    }

    public static User getUserIdBySessionId(CookieRepository cookieRepository, Cookie[] cookies) throws MessageExeption {

        String stringCookie = getSessionId(cookies);
        String lang = getLang(cookies);

        if (stringCookie == null)
            throw new MessageExeption(Texts.NOT_LOGIN.getText(lang));

        User userId = cookieRepository.findUserIdBySessionId(stringCookie);
        if (userId == null)
            throw new MessageExeption(Texts.NOT_LOGIN.getText(lang));
        return userId;
    }

    public static List<Track> formatListForPeriod(List<Track> list) {
        list.sort((o1, o2) -> {
            if (o1.getFinishDate().after(o2.getFinishDate()))
                return -1;
            else if (o2.getFinishDate().after(o1.getFinishDate()))
                return 1;
            else
                return 0;
        });
        HashSet<Timestamp> dates = new HashSet<>();
        for (Track track : list) {
            if (!dates.contains(track.getFinishDate())) {
                dates.add(track.getFinishDate());
                track.setFirst(true);
            }
        }
        return list;
    }

}
