package com.mikchaelsoloviev.musiccatalog.Model;

import java.util.Arrays;


public class Artist {
    long id;
    String name;
    String [] genres;
    long tracks;
    int albums;
    String link;
    String description;
    Cover cover;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genres=" + Arrays.toString(genres) +
                ", tracks=" + tracks +
                ", albums=" + albums +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", cover=" + cover +
                '}';
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String[] getGenres() {
        return genres;
    }

    public long getTracks() {
        return tracks;
    }

    public int getAlbums() {
        return albums;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public Cover getCover() {
        return cover;
    }

     public static class Cover{
        String small;
        String big;

        public String getSmall() {
            return small;
        }

        public String getBig() {
            return big;
        }

         public void setSmall(String small) {
             this.small = small;
         }

         public void setBig(String big) {
             this.big = big;
         }
     }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public void setTracks(long tracks) {
        this.tracks = tracks;
    }

    public void setAlbums(int albums) {
        this.albums = albums;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }
}
