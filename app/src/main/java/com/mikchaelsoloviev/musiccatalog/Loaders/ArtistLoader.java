package com.mikchaelsoloviev.musiccatalog.Loaders;

import android.content.Context;

import com.mikchaelsoloviev.musiccatalog.DataAcsess.DataManager;
import com.mikchaelsoloviev.musiccatalog.Model.Artist;


public class ArtistLoader extends SimpleAsyncTaskLoader<Artist> {
    private long mArtistId;
    public ArtistLoader(Context context,long artistId) {
        super(context);
        this.mArtistId=artistId;
    }

    @Override
    public Artist loadInBackground() {
        return (DataManager.instance(getContext()).getArtist(mArtistId));
    }
}
