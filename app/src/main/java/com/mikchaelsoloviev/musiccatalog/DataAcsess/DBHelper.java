package com.mikchaelsoloviev.musiccatalog.DataAcsess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mikchaelsoloviev.musiccatalog.Model.Artist;
import com.mikchaelsoloviev.musiccatalog.R;
import com.mikchaelsoloviev.musiccatalog.Utils.Utils;

import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String ARTIST_DATA_BASE = "artists_data_base";
    public static final String ARTISTS_TABLE_NAME = "artists_table";
    public static final String COLUMN_NAME_ID = "artist_ID";
    public static final String COLUMN_NAME_NAME = "artist_name";
    public static final String COLUMN_NAME_DESCRIPTION = "artist_description";
    public static final String COLUMN_NAME_GENRES = "artist_genres";
    public static final String COLUMN_NAME_TRACK_COUNT = "artist_track_count";
    public static final String COLUMN_NAME_ALBUMS_COUNT = "artist_albums_count";
    public static final String COLUMN_NAME_COVER_SMALL = "artist_small_cover";
    public static final String COLUMN_NAME_COVER_BIG = "artist_big_cover";
    public static final String COLUMN_NAME_LINK = "artist_link";

    private static final String CREATE_QUERY_TABLE = "CREATE TABLE " + ARTISTS_TABLE_NAME + " ("
            + COLUMN_NAME_ID + " INTEGER PRIMARY KEY ," + COLUMN_NAME_NAME + " TEXT ,"
            + ARTISTS_TABLE_NAME + " TEXT ," + COLUMN_NAME_DESCRIPTION + " TEXT ,"
            + COLUMN_NAME_GENRES + " TEXT ," + COLUMN_NAME_TRACK_COUNT + " INTEGER ,"
            + COLUMN_NAME_ALBUMS_COUNT + " INTEGER ,"
            + COLUMN_NAME_COVER_SMALL + " TEXT ," + COLUMN_NAME_COVER_BIG + " TEXT ,"
            + COLUMN_NAME_LINK + " TEXT" + ")";

    private static final String DELETE_QUERY_TABLE = "DROP TABLE IF EXISTS " + ARTISTS_TABLE_NAME;
    private final Context mContext;

    public DBHelper(Context context) {
        super(context, ARTIST_DATA_BASE, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void recreateDB() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(DELETE_QUERY_TABLE);
        db.execSQL(CREATE_QUERY_TABLE);
    }

    public ArtistCursor readArtists() {

        Cursor cursor = getReadableDatabase().query(ARTISTS_TABLE_NAME,
                new String[]{COLUMN_NAME_ID
                        , COLUMN_NAME_NAME
                        , COLUMN_NAME_DESCRIPTION
                        , COLUMN_NAME_GENRES
                        , COLUMN_NAME_TRACK_COUNT
                        , COLUMN_NAME_ALBUMS_COUNT
                        , COLUMN_NAME_LINK
                        , COLUMN_NAME_COVER_SMALL
                        , COLUMN_NAME_COVER_BIG},

                null, null, null, null, COLUMN_NAME_NAME);

        return new ArtistCursor(cursor);
    }

    public ArtistCursor readArtist(long artistId) {
        String[] params = {String.valueOf(artistId)};
        Cursor cursor = getReadableDatabase().query(ARTISTS_TABLE_NAME,
                new String[]{COLUMN_NAME_ID
                        , COLUMN_NAME_NAME
                        , COLUMN_NAME_DESCRIPTION
                        , COLUMN_NAME_GENRES
                        , COLUMN_NAME_TRACK_COUNT
                        , COLUMN_NAME_ALBUMS_COUNT
                        , COLUMN_NAME_LINK
                        , COLUMN_NAME_COVER_SMALL
                        , COLUMN_NAME_COVER_BIG},

                COLUMN_NAME_ID + " = ?", params, null, null, null);

        return new ArtistCursor(cursor);
    }

    public void insertArtist(Artist artist) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_ID, artist.getId());
        contentValues.put(COLUMN_NAME_NAME, artist.getName());
        contentValues.put(COLUMN_NAME_DESCRIPTION, artist.getDescription());
        contentValues.put(COLUMN_NAME_GENRES, Utils.getGenres(
                artist.getGenres()
                , mContext.getString(R.string.genres_divider)));
        contentValues.put(COLUMN_NAME_TRACK_COUNT, artist.getTracks());
        contentValues.put(COLUMN_NAME_ALBUMS_COUNT, artist.getAlbums());
        contentValues.put(COLUMN_NAME_LINK, artist.getLink());
        contentValues.put(COLUMN_NAME_COVER_SMALL, artist.getCover().getSmall());
        contentValues.put(COLUMN_NAME_COVER_BIG, artist.getCover().getBig());
        getWritableDatabase().insert(ARTISTS_TABLE_NAME, null, contentValues);
    }

    public void insertArtists(List<Artist> artists) {
        for (Artist artist : artists) {
            insertArtist(artist);
        }

    }

    public static class ArtistCursor extends CursorWrapper {

        public ArtistCursor(Cursor cursor) {
            super(cursor);
        }

        public Artist getArtist(String genresDivider) {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            Artist artist = new Artist();
            artist.setId(getLong(getColumnIndex(COLUMN_NAME_ID)));
            artist.setName(getString(getColumnIndex(COLUMN_NAME_NAME)));
            artist.setDescription(getString(getColumnIndex(COLUMN_NAME_DESCRIPTION)));
            artist.setGenres(Utils.getGenres(
                    getString(getColumnIndex(COLUMN_NAME_GENRES))
                    , genresDivider));
            artist.setAlbums(getInt(getColumnIndex(COLUMN_NAME_ALBUMS_COUNT)));
            artist.setTracks(getLong(getColumnIndex(COLUMN_NAME_TRACK_COUNT)));
            Artist.Cover cover = new Artist.Cover();
            cover.setSmall(getString(getColumnIndex(COLUMN_NAME_COVER_SMALL)));
            cover.setBig(getString(getColumnIndex(COLUMN_NAME_COVER_BIG)));
            artist.setCover(cover);
            artist.setLink(getString(getColumnIndex(COLUMN_NAME_LINK)));

            return artist;
        }

    }

}
