package com.example.inclass07;

public class Music {

    String trackName, artistName, albumName, updateTime, trackShareUrl;

    public Music(String trackName, String artistName, String albumName, String updateTime, String trackShareUrl) {
        this.trackName = trackName;
        this.artistName = artistName;
        this.albumName = albumName;
        this.updateTime = updateTime;
        this.trackShareUrl = trackShareUrl;
    }

    @Override
    public String toString() {
        return "Music{" +
                "trackName='" + trackName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", albumName='" + albumName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", trackShareUrl='" + trackShareUrl + '\'' +
                '}';
    }
}
