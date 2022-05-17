package com.startserver.Controllers;


import com.startserver.DTO.RUserSettings;
import com.startserver.Entity.Cookie;
import com.startserver.Entity.User;
import com.startserver.DTO.RMessage;
import com.startserver.Repository.CookieRepository;
import com.startserver.Repository.UserRepository;
import com.startserver.responseUtils.MessageExeption;
import com.startserver.utils.MyAppConfig;
import com.startserver.utils.StaticUtils;
import com.startserver.utils.TStatus;
import com.startserver.utils.Texts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    final String PROMO = "547512";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CookieRepository cookieRepository;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws MessageExeption {
        User userFind = userRepository.findUserByUsername(user.getUsername());
        if (userFind != null) {
            throw new MessageExeption(Texts.USER_NAME_BUSY.getText(StaticUtils.getLang(request.getCookies())));
        } else {
//            user.setStatus("TRACKDEALER");
            user.setStatus("TRACKLISTENER");
            user.setDateRegistration(new Timestamp(new Date().getTime()));
            User savingUser = userRepository.save(user);
            addCookie(savingUser, response);
            return ResponseEntity.ok().build();
        }
    }

    @Transactional
    @GetMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@RequestParam("username") String username, @RequestParam("password") String password,
                                   HttpServletRequest request, HttpServletResponse response) throws MessageExeption {
        User user = userRepository.findUserByUsername(username);
        if (user == null)
            throw new MessageExeption(Texts.USER_NOT_FOUND.getText(StaticUtils.getLang(request.getCookies())));

        if (!user.getPassword().equals(password))
            throw new MessageExeption(Texts.WRONG_PASSWORD.getText(StaticUtils.getLang(request.getCookies())));

        addCookie(user, response);
        return ResponseEntity.ok().build();
    }

    public void addCookie(User user, HttpServletResponse response) {
        Cookie cookie = cookieRepository.save(new Cookie(StaticUtils.createCookie(), null, user));
        javax.servlet.http.Cookie cookieHttp = new javax.servlet.http.Cookie(StaticUtils.sessionName, cookie.getSessionId());
        cookieHttp.setMaxAge(265 * 24 * 60 * 60);  // (s)
        cookieHttp.setPath("/");
        response.addCookie(cookieHttp);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<?> logout(HttpServletRequest request) throws MessageExeption {
        String sessionId = StaticUtils.getSessionId(request.getCookies());
        Cookie cookie = cookieRepository.findBySessionId(sessionId);
        cookieRepository.delete(cookie);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ResponseEntity<?> userSettings(@RequestParam("version") String version,
                                          HttpServletRequest request) throws MessageExeption {
        User user = StaticUtils.getUserIdBySessionId(cookieRepository, request.getCookies());
        if (user == null)
            throw new MessageExeption(Texts.USER_NOT_FOUND.getText(StaticUtils.getLang(request.getCookies())));

        RUserSettings rUserSettings = new RUserSettings(user.getUsername(), user.getName(), user.getStatus());
        if (version != null) {
            Integer versionBack = Integer.parseInt(MyAppConfig.versionBack.replace(".", ""));
            Integer versionFront = Integer.parseInt(version.replace(".", ""));
            if (versionBack.equals(versionFront)) {
                return ResponseEntity.ok().body(rUserSettings);
            } else if (versionBack > versionFront) {
                throw new MessageExeption(Texts.UPDATE_APP.getText(StaticUtils.getLang(request.getCookies())));
            } else {
                throw new MessageExeption(Texts.WORK_ON_SERVER.getText(StaticUtils.getLang(request.getCookies())));
            }
        } else {
            throw new MessageExeption(Texts.WRONG_VERSION.getText(StaticUtils.getLang(request.getCookies())));
        }
    }

    @Transactional
    @RequestMapping(value = "/promo", method = RequestMethod.GET)
    public ResponseEntity<?> promoCode(@RequestParam("promo") String promo, HttpServletRequest request) throws MessageExeption {
        User user = StaticUtils.getUserIdBySessionId(cookieRepository, request.getCookies());
        if (user == null)
            throw new MessageExeption(Texts.USER_NOT_FOUND.getText(StaticUtils.getLang(request.getCookies())));
        if (promo.equals(PROMO)) {
            userRepository.updateStatus(user.getId(), TStatus.getUpgradedStatus(user.getStatus(), true));
            return ResponseEntity.ok().build();
        } else {
            throw new MessageExeption(Texts.WRONG_PROMO.getText(StaticUtils.getLang(request.getCookies())));
        }
    }
}
