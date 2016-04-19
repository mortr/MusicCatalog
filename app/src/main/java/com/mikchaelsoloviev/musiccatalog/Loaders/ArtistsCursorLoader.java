package com.mikchaelsoloviev.musiccatalog.Loaders;

import android.content.Context;

import com.mikchaelsoloviev.musiccatalog.DataAcsess.DBHelper;
import com.mikchaelsoloviev.musiccatalog.DataAcsess.DataManager;

/**
 * Created by Sol on 14.04.2016.
 */
public class ArtistsCursorLoader extends SimpleAsyncTaskLoader<DBHelper.ArtistCursor> {
    private Context activity;
    public ArtistsCursorLoader(Context context) {
        super(context);
        activity=context;

    }

    @Override
    public DBHelper.ArtistCursor loadInBackground() {

        return (DataManager.instance(activity)).getArtistsCursor();
    }

    @Override
    protected void releaseResources(DBHelper.ArtistCursor data) {
        super.releaseResources(data);
        if (!data.isClosed()) {
            data.close();
        }
    }

}
