package com.mikchaelsoloviev.musiccatalog.DataAcsess;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mikchaelsoloviev.musiccatalog.Model.Artist;
import com.mikchaelsoloviev.musiccatalog.Network.IDataSettable;
import com.mikchaelsoloviev.musiccatalog.Network.ServiceHelper;
import com.mikchaelsoloviev.musiccatalog.R;
import com.mikchaelsoloviev.musiccatalog.Utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DataManager implements IDataSettable, ArtistsObservable {
    public static final String LOG_TAG = "MC_DataManager";
    public static final String PREFS_SETTINGS = "Settings";
    public static final String KEY_PREFS_LAST_LOAD_DATE = "LastLoadDate";

    private DBHelper mDataBaseHelper;
    private static Context mContext;
    private final SharedPreferences mSharedPreferences;
    private List<ArtistsChangeObserver> observers = new ArrayList<>();
    private static DataManager sDataManager;

    private DataManager(Context context) {
        mContext = context;
        mDataBaseHelper = new DBHelper(mContext);
        mSharedPreferences = context.getSharedPreferences(PREFS_SETTINGS, 0);
    }

    public static DataManager instance(Context context) {
        if (sDataManager == null) {
            sDataManager = new DataManager(context);
        } else {
            mContext = context;
        }
        return sDataManager;
    }

    public DBHelper.ArtistCursor getArtistsCursor() {
        Log.d(LOG_TAG, "getArtistList()");
        DBHelper.ArtistCursor artistCursor = null;
        if ((mSharedPreferences.contains(KEY_PREFS_LAST_LOAD_DATE))) {
            artistCursor = mDataBaseHelper.readArtists();
        }
        //проверка не пора ли обновить данные
        if ((mSharedPreferences.getLong(KEY_PREFS_LAST_LOAD_DATE, 0)
                + mContext.getResources().getInteger(R.integer.caches_time))
                < new Date().getTime()) {

            resetData();
        }
        return artistCursor;
    }

    /**
     * создает запрос на обновление данных
     */
    public void resetData() {
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            ServiceHelper.getData(this);
        }else{
            // оповещает подписчиков что обновить данные не удалось
            notifyObservers(false);
        }
    }

    /**
     *
     * @param id Id требуемого элемента
     * @return обьект Artist с данным ID  если он есть в базе данных либо null
     */
    public Artist getArtist(long id) {
        Log.d(LOG_TAG, "getArtist");
        DBHelper.ArtistCursor artistCursor = mDataBaseHelper.readArtist(id);
        if (artistCursor.moveToFirst()) {
            return artistCursor.getArtist(mContext.getString(R.string.genres_divider));
        }
        return null;
    }

    public void saveArtistList(List<Artist> artistList) {
        Log.d(LOG_TAG, "saveArtistList" );
        final List<Artist> artistsList=artistList;
        (new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                mDataBaseHelper.recreateDB();
                mDataBaseHelper.insertArtists(artistsList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mSharedPreferences.edit().putLong(KEY_PREFS_LAST_LOAD_DATE, new Date().getTime()).commit();
                Toast.makeText(mContext, R.string.text_data_loaded, Toast.LENGTH_LONG).show();
               // оповещение подписчиков об обновлении данных
                notifyObservers(true);
            }


        }).execute();



    }


    @Override
    public void setData(Object body) {
        if (body instanceof Throwable) {
            Toast.makeText(mContext, R.string.error_server_connection, Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, ((Throwable) body).toString());
        } else {
            saveArtistList((List<Artist>) body);
        }
    }

    /**
     *
     * @param successLoad успешность загруззки новых данных
     */
    @Override
    public void notifyObservers(boolean successLoad) {
        for (ArtistsChangeObserver artistsChangeObserver : observers) {
            artistsChangeObserver.update(successLoad);
        }
    }

    @Override
    public void addObserver(ArtistsChangeObserver artistsChangeObserver) {
        observers.add(artistsChangeObserver);
    }

    @Override
    public void removeObserver(ArtistsChangeObserver artistsChangeObserver) {
        observers.remove(artistsChangeObserver);
    }
}
