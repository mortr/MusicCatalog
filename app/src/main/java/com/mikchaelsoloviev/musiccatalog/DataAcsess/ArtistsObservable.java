package com.mikchaelsoloviev.musiccatalog.DataAcsess;


public interface ArtistsObservable {
    void notifyObservers(boolean successChange);

    void addObserver(ArtistsChangeObserver artistsChangeObserver);

    void removeObserver(ArtistsChangeObserver artistsChangeObserver);
}
