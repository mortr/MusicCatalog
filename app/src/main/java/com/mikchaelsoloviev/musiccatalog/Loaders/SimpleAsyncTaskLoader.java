package com.mikchaelsoloviev.musiccatalog.Loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class SimpleAsyncTaskLoader<E> extends AsyncTaskLoader<E> {
    private E mData;

    public SimpleAsyncTaskLoader(Context context) {
        super(context);
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mData != null) {
            deliverResult(mData);
        } else {

            if (takeContentChanged() || mData == null) {
                forceLoad();
            }
        }
    }


    @Override
    public void deliverResult(E data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        E oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    protected E getData() {
        return mData;
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }


    @Override
    protected void onReset() {
        onStopLoading();

        if (mData != null) {
            releaseResources(mData);
            mData = null;
        }

    }




    @Override
    public void onCanceled(E data) {
        super.onCanceled(data);
        if (mData != null) {
            releaseResources(data);
        }
    }

    protected void releaseResources(E data) {
    }
}
