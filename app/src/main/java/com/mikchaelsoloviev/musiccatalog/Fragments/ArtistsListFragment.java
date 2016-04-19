package com.mikchaelsoloviev.musiccatalog.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mikchaelsoloviev.musiccatalog.Adapters.ArtistListCursorAdapter;
import com.mikchaelsoloviev.musiccatalog.DataAcsess.ArtistsChangeObserver;
import com.mikchaelsoloviev.musiccatalog.DataAcsess.ArtistsObservable;
import com.mikchaelsoloviev.musiccatalog.DataAcsess.DBHelper;
import com.mikchaelsoloviev.musiccatalog.DataAcsess.DataManager;
import com.mikchaelsoloviev.musiccatalog.Loaders.ArtistsCursorLoader;
import com.mikchaelsoloviev.musiccatalog.R;


public class ArtistsListFragment extends Fragment implements ArtistsChangeObserver, LoaderManager.LoaderCallbacks<DBHelper.ArtistCursor> {

    public static final String LOG_TAG = "MC_ArtistsListFragment";
    public static final int ID_LOAD_ARTISTS = 1;

    private RecyclerView mRecyclerView;
    private ArtistListCursorAdapter mArtistListCursorAdapter;
    private IListItemSettable mArtistInfoFragmentSetter;

    private int progressBarState=View.VISIBLE;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mIsLoad;
    private ArtistsObservable mArtistsObservable;
    private ProgressBar mProgressBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "onAttach");
        mArtistsObservable = DataManager.instance(context);
        // подисываемся на обновление данных в DataManager
        mArtistsObservable.addObserver(this);
        if (context instanceof IListItemSettable) {
            mArtistInfoFragmentSetter = (IListItemSettable) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        setRetainInstance(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artist_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_artist_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new RefreshListener());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);

        mProgressBar.setVisibility(progressBarState);
        mIsLoad = true;
        getLoaderManager().initLoader(ID_LOAD_ARTISTS, null, this);

    }

    public static ArtistsListFragment newInstance() {
        return new ArtistsListFragment();
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
            getLoaderManager().restartLoader(ID_LOAD_ARTISTS, null, this);
        } else {
            progressBarState=View.INVISIBLE;
            mProgressBar.setVisibility(progressBarState);
            hideSwipe();
        }

    }

    /**
     * "отключает" спинер  SwipeRefreshLayout если не идет загрузка данных
     */
    private void hideSwipe() {
        if (mIsLoad) {
            mSwipeRefreshLayout.setRefreshing(false);
            mIsLoad = false;
        }
    }


    @Override
    public Loader<DBHelper.ArtistCursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        return new ArtistsCursorLoader(getActivity());

    }

    @Override
    public void onLoadFinished(Loader<DBHelper.ArtistCursor> loader, DBHelper.ArtistCursor data) {
        Log.d(LOG_TAG, "onLoadFinished " + mIsLoad);
        hideSwipe();
        if (mArtistListCursorAdapter == null) {
            mArtistListCursorAdapter = new ArtistListCursorAdapter(mArtistInfoFragmentSetter, R.layout.item_artist_list, data);
        } else {
            mArtistListCursorAdapter.replaceData(data);
        }
        // если полученны данные -- "выключает" прогресс бар, иначе он отображается на месте списка
        if (data != null) {
            progressBarState=View.INVISIBLE;
            mProgressBar.setVisibility(progressBarState);
        }
        mRecyclerView.setAdapter(mArtistListCursorAdapter);

    }

    @Override
    public void onLoaderReset(Loader<DBHelper.ArtistCursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        mRecyclerView.setAdapter(null);
    }

    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            mIsLoad = true;
            (DataManager.instance(getActivity())).resetData();

        }
    }

    public static interface IListItemSettable {
        /**
         * Устанавливает отображение расширенной информации элемента списка
         * @param itemId Id элемента списка который нужно установить
         * @param otherParams другие параметры элемента списка, например имя
         */
        void setItem(long itemId,String ... otherParams );
    }
}
