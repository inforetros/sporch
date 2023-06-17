package com.inforetros.model;

public class Track {
    private Integer pos;
    private String artist_name;
    private String track_uri;
    private String artist_uri;
    private String track_name;
    private String album_uri;
    private Integer duration_ms;
    private String album_name;

    public Track(String track_uri, String track_name, String album_name, String artist_name) {
        this.track_uri = track_uri;
        this.track_name = track_name;
        this.album_name = album_name;
        this.artist_name = artist_name;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getTrack_uri() {
        return track_uri;
    }

    public void setTrack_uri(String track_uri) {
        this.track_uri = track_uri;
    }

    public String getArtist_uri() {
        return artist_uri;
    }

    public void setArtist_uri(String artist_uri) {
        this.artist_uri = artist_uri;
    }

    public String getTrack_name() {
        return track_name;
    }

    public void setTrack_name(String track_name) {
        this.track_name = track_name;
    }

    public String getAlbum_uri() {
        return album_uri;
    }

    public void setAlbum_uri(String album_uri) {
        this.album_uri = album_uri;
    }

    public Integer getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(Integer duration_ms) {
        this.duration_ms = duration_ms;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String track_uri() {
        String[] uriParts = track_uri.split(":");
        return uriParts[uriParts.length - 1];
    }

    public String content() {
        return track_name + ' ' + album_name + ' ' + artist_name;
    }

    @Override
    public String toString() {
        return "Track[track_uri=" + track_uri + ", track_name=" + track_name + ", album_name=" + album_name + ", artist_name=" + artist_name + "]";
    }
}
