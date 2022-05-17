package com.startserver.Entity;

import javax.persistence.*;

@Entity
@Table(name = "userlike")
public class UserLike {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track trackId;

    @Column(name = "user_like")
    private Boolean userike;

    public UserLike() {
    }

    public UserLike(User userId, Track trackId, Boolean userike) {
        this.userId = userId;
        this.trackId = trackId;
        this.userike = userike;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Track getTrackId() {
        return trackId;
    }

    public void setTrackId(Track trackId) {
        this.trackId = trackId;
    }

    public Boolean getUserike() {
        return userike;
    }

    public void setUserike(Boolean userike) {
        this.userike = userike;
    }
}
