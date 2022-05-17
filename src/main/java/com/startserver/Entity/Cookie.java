package com.startserver.Entity;

import javax.persistence.*;


@Entity
@Table(name = "cookie")
public class Cookie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "data")
    private String data;

    @ManyToOne
    @JoinColumn(name = "user_id")
//    @Column(name = "user_id", nullable = false)
    private User userId;

    public Cookie() {
    }

    public Cookie(String session_id, String data, User user_id) {
        this.sessionId = session_id;
        this.data = data;
        this.userId = user_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}

