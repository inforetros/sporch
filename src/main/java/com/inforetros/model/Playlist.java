package com.inforetros.model;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Playlist {
    private String name;
    private boolean collaborative;
    private Integer pid;
    private Date modified_at;
    private Integer num_tracks;
    private Integer num_albums;
    private Integer num_followers;
    private List<Track> tracks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCollaborative() {
        return collaborative;
    }

    public void setCollaborative(boolean collaborative) {
        this.collaborative = collaborative;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Date getModified_at() {
        return modified_at;
    }

    public void setModified_at(Date modified_at) {
        this.modified_at = modified_at;
    }

    public Integer getNum_tracks() {
        return num_tracks;
    }

    public void setNum_tracks(Integer num_tracks) {
        this.num_tracks = num_tracks;
    }

    public Integer getNum_albums() {
        return num_albums;
    }

    public void setNum_albums(Integer num_albums) {
        this.num_albums = num_albums;
    }

    public Integer getNum_followers() {
        return num_followers;
    }

    public void setNum_followers(Integer num_followers) {
        this.num_followers = num_followers;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public String content() {
        return tracks.stream().map(com.inforetros.model.Track::content).collect(Collectors.joining("\n"));
    }

    public String trackUriList() {
        return tracks.stream().map(com.inforetros.model.Track::track_uri).collect(Collectors.joining(","));
    }
}
