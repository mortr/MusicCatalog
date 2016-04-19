package com.mikchaelsoloviev.musiccatalog.Utils;


public class Utils {
    /**
     *
     * @param genres
     * @param genresDivider отображаемый разделитель жанров
     * @return обьект String включающий все жанры данного исполнителя
     */
    public static String getGenres(String[] genres, String genresDivider) {
        StringBuilder stringBuilder = new StringBuilder();
        int genresCount = genres.length;
        if (genresCount > 0){
        stringBuilder.append(genres[0]);

        for (int i = 1; i < genresCount; i++) {
            stringBuilder.append(genresDivider).append(genres[i]);
        }}
        return stringBuilder.toString();
    }

    public static String[] getGenres(String genres, String genresDivider) {
        return genres.split(genresDivider);
    }

}
