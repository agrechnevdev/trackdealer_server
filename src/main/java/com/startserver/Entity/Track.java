package com.startserver.Entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "track")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_load_id")
//    @Column(name = "user_load_id", nullable = false)
    private User userLoadId;

    @Column(name = "deezer_id", nullable = false)
    private Integer deezerId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artist", nullable = true)
    private String artist;

    @Column(name = "duration", nullable = true)
    private Integer duration;

    @Column(name = "genre", nullable = true)
    private String genre;

    @Column(name = "cover_image", nullable = true)
    private String coverImage;

    @Column(name = "count_like", nullable = true)
    private Integer countLike;

    @Column(name = "count_dislike", nullable = true)
    private Integer countDislike;

    @OneToMany(mappedBy = "trackId", cascade = CascadeType.PERSIST)
    private List<UserLike> userLikeList;

    @Column(name = "finish_date", nullable = true)
    private Timestamp finishDate;

    @ManyToOne
    @JoinColumn(name = "user_finish_id")
    private User userFinishId;

    @Transient
    private Boolean first = false;

    public Boolean currentUserLike(Integer userId) {
        if (userLikeList == null || userLikeList.isEmpty()){
            return null;
        } else {
            for(UserLike ul : userLikeList){
                if(ul.getUserId().getId().equals(userId)){
                    return ul.getUserike();
                }
            }
            return null;
        }
    }

    public String getUserNameLoad() {
        if (userLoadId != null)
            return userLoadId.getUsername();
        else if( userFinishId != null)
            return userFinishId.getUsername();
        return null;
    }

    public String getUserStatusLoad() {
        if (userLoadId != null)
            return userLoadId.getStatus();
        else if( userFinishId != null)
            return userFinishId.getStatus();
        return null;
    }

    public Track() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserLoadId() {
        return userLoadId;
    }

    public void setUserLoadId(User userLoadId) {
        this.userLoadId = userLoadId;
    }

    public Integer getDeezerId() {
        return deezerId;
    }

    public void setDeezerId(Integer deezerId) {
        this.deezerId = deezerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Timestamp getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Timestamp finishDate) {
        this.finishDate = finishDate;
    }

    public Integer getCountLike() {
        return countLike;
    }

    public void setCountLike(Integer countLike) {
        this.countLike = countLike;
    }

    public Integer getCountDislike() {
        return countDislike;
    }

    public void setCountDislike(Integer countDislike) {
        this.countDislike = countDislike;
    }

    public List<UserLike> getUserLikeList() {
        return userLikeList;
    }

    public void setUserLikeList(List<UserLike> userLikeList) {
        this.userLikeList = userLikeList;
    }

    public User getUserFinishId() {
        return userFinishId;
    }

    public void setUserFinishId(User userFinishId) {
        this.userFinishId = userFinishId;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }
}
