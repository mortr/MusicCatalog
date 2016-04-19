package com.mikchaelsoloviev.musiccatalog.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mikchaelsoloviev.musiccatalog.DataAcsess.DBHelper;
import com.mikchaelsoloviev.musiccatalog.Fragments.ArtistsListFragment;
import com.mikchaelsoloviev.musiccatalog.Model.Artist;
import com.mikchaelsoloviev.musiccatalog.R;

import static com.mikchaelsoloviev.musiccatalog.Utils.Utils.getGenres;

public class ArtistListCursorAdapter extends RecyclerView.Adapter<ArtistListCursorAdapter.ArtistHolder> {
    public static final String LOG_TAG = "MC_ArtistListAdapter";

    private DBHelper.ArtistCursor mArtistCursor;
    private final int layoutId;
    ArtistsListFragment.IListItemSettable mListItemSetter;

    /**
     * @param itemSetter объект ArtistsListFragment.IListItemSettable реализующий поведение при выборе элемента списка
     * @param layoutId   layout для представления элемента списка
     * @param artistList DBHelper.ArtistCursor с данными для отображения
     */
    public ArtistListCursorAdapter(ArtistsListFragment.IListItemSettable itemSetter, int layoutId, DBHelper.ArtistCursor artistList) {
        Log.d(LOG_TAG, "ArtistListCursorAdapter");
        this.layoutId = layoutId;
        this.mArtistCursor = artistList;
        mListItemSetter = itemSetter;
    }

    @Override
    public ArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ArtistHolder(view, mListItemSetter);
    }

    @Override
    public void onBindViewHolder(ArtistHolder holder, int position) {
        Log.d(LOG_TAG, "onBindViewHolder mArtistList");
        mArtistCursor.moveToPosition(position);
        holder.setData(mArtistCursor.getArtist(((Context) mListItemSetter).getString(R.string.genres_divider)));
    }

    @Override
    public int getItemCount() {
        if (mArtistCursor == null) {
            return 0;
        }
        return mArtistCursor.getCount();
    }


    public void replaceData(DBHelper.ArtistCursor artistCursor) {
        Log.d(LOG_TAG, "replaceData ");
        if (mArtistCursor != artistCursor) {
            Log.d(LOG_TAG, "replaceData ");
            Cursor oldCursor = this.mArtistCursor;
            this.mArtistCursor = artistCursor;

            if (oldCursor != null) {
                oldCursor.close();
            }
        }
        notifyDataSetChanged();
    }

    public void releaseCursor() {
        if (mArtistCursor != null && !mArtistCursor.isClosed()) {
            mArtistCursor.close();
            mArtistCursor = null;
        }
    }

    public  class ArtistHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTvDisco;
        private final TextView mTvGenres;
        private final TextView mTvName;
        private final SimpleDraweeView mDraweeView;
        private final View mItemView;
        private Artist mArtist;
        private ArtistsListFragment.IListItemSettable mListItemSetter;


        public ArtistHolder(View itemView, ArtistsListFragment.IListItemSettable listItemSetter) {
            super(itemView);
            mListItemSetter = listItemSetter;
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvDisco = (TextView) itemView.findViewById(R.id.tv_disco);
            mTvGenres = (TextView) itemView.findViewById(R.id.tv_genres);
            mDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.my_image_view);
            mItemView = itemView;

        }


        public void setData(Artist artist) {
            mArtist = artist;
            Uri uri = Uri.parse(mArtist.getCover().getSmall());
            mDraweeView.setImageURI(uri);
            mTvName.setText(mArtist.getName());
            Context context = (Context) mListItemSetter;
            mTvGenres.setText(getGenres(mArtist.getGenres(),
                    context.getResources().getString(R.string.genres_divider)));
            mTvDisco.setText(getDiscography(context));
            mItemView.setOnClickListener(this);
        }

        @NonNull
        private String getDiscography(Context context) {
            return mArtist.getAlbums() +
                    context.getResources().getString(R.string.text_albums) +
                    context.getResources().getString(R.string.genres_divider) +
                    mArtist.getTracks() +
                    context.getResources().getString(R.string.text_single);
        }

        @Override
        public void onClick(View v) {
            if (mListItemSetter != null) {
                Log.d(LOG_TAG, "onClick  id" + mArtist.getId());
                mListItemSetter.setItem(mArtist.getId(), mArtist.getName());
            }
        }
    }
}
