package com.mikchaelsoloviev.musiccatalog.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mikchaelsoloviev.musiccatalog.DataAcsess.ArtistsChangeObserver;
import com.mikchaelsoloviev.musiccatalog.DataAcsess.ArtistsObservable;
import com.mikchaelsoloviev.musiccatalog.DataAcsess.DataManager;
import com.mikchaelsoloviev.musiccatalog.Loaders.ArtistLoader;
import com.mikchaelsoloviev.musiccatalog.Model.Artist;
import com.mikchaelsoloviev.musiccatalog.R;
import com.mikchaelsoloviev.musiccatalog.Utils.Utils;

public class ArtistFragment extends Fragment implements ArtistsChangeObserver,LoaderManager.LoaderCallbacks<Artist> {
    public static final String LOG_TAG = "MC_ArtistFragment";
    public static final String KEY_ARG_ARTIST_ID = "Key Arg Artist ID";
    public static final int ID_LOAD_ARTIST = 0;
    private Artist mArtist;

    private TextView mArtistDiscography;
    private TextView mArtistGenres;
    private SimpleDraweeView mImage;
    private TextView mArtistDescription;
    private ArtistsObservable mArtistsObservable;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mArtistsObservable = DataManager.instance(context);
        mArtistsObservable.addObserver(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(ID_LOAD_ARTIST, getArguments(), this);

        Log.d(LOG_TAG, "onCreate ");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mArtistDiscography = ((TextView) view.findViewById(R.id.artist_disco));
        mArtistGenres = ((TextView) view.findViewById(R.id.artist_genres));
        mImage = ((SimpleDraweeView) view.findViewById(R.id.artist_draweeV));
        mArtistDescription = ((TextView) view.findViewById(R.id.artist_description));
        updateUI();
    }

    public static ArtistFragment newInstance(long artistId) {

        Bundle args = new Bundle();
        args.putLong(KEY_ARG_ARTIST_ID, artistId);
        ArtistFragment fragment = new ArtistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CharSequence getArtistDiscography() {
        return mArtist.getAlbums() + getActivity().getString(R.string.text_albums)
                + getActivity().getString(R.string.divider_discography)
                + mArtist.getTracks() + getActivity().getString(R.string.text_single);
    }

    @Override
    public Loader<Artist> onCreateLoader(int id, Bundle args) {
        return new ArtistLoader(getActivity(), args.getLong(KEY_ARG_ARTIST_ID));
    }

    @Override
    public void onLoadFinished(Loader<Artist> loader, Artist data) {
        mArtist = data;
        updateUI();

    }

    private void updateUI() {
        if (mArtist != null && mArtistDescription != null) {
            mArtistDiscography.setText(getArtistDiscography());
            mArtistGenres.setText(Utils.getGenres(mArtist.getGenres()
                    , getActivity().getResources().getString(R.string.genres_divider)));
            mImage.setImageURI(Uri.parse(mArtist.getCover().getBig()));
            mArtistDescription.setText(mArtist.getDescription());
        }
    }

    @Override
    public void onLoaderReset(Loader<Artist> loader) {

    }

    @Override
    public void onDetach() {
        Log.d(LOG_TAG, "onDetach()");
        mArtistsObservable.removeObserver(this);
        mArtistsObservable = null;
        super.onDetach();
    }

    @Override
    public void update(boolean successLoad) {
        Log.d(LOG_TAG, "update");
        if (successLoad) {
            getLoaderManager().restartLoader(ID_LOAD_ARTIST,getArguments(), this);
        }

    }
}
