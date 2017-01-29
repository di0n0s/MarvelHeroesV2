package net.opentrends.marvelheroes;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.List;

/**
 * Created by sfcar on 27/01/2017.
 */

public class MarvelHeroe {

    private int mId;
    private String mName;
    private String mDescription;
    private String mImage;
    private int mComicsNumber;
    private List<MarvelComic> mMarvelComics;
    private int mEventsNumber;
    private List<MarvelEvent> mMarvelEvents;
    private String mDetails;
    private String mWiki;
    private String mComics;

    public MarvelHeroe() {
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public int getComicsNumber() {
        return mMarvelComics.size();
    }

    public void setComicsNumber(int comicsNumber) {
        mComicsNumber = mMarvelComics.size();
    }

    public List<MarvelComic> getMarvelComics() {
        return mMarvelComics;
    }

    public void setMarvelComics(List<MarvelComic> marvelComics) {
        mMarvelComics = marvelComics;
    }

    public int getEventsNumber() {
        return mEventsNumber;
    }

    public void setEventsNumber(int eventsNumber) {
        mEventsNumber = eventsNumber;
    }

    public List<MarvelEvent> getMarvelEvents() {
        return mMarvelEvents;
    }

    public void setMarvelEvents(List<MarvelEvent> marvelEvents) {
        mMarvelEvents = marvelEvents;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        mDetails = details;
    }

    public String getWiki() {
        return mWiki;
    }

    public void setWiki(String wiki) {
        mWiki = wiki;
    }

    public String getComics() {
        return mComics;
    }

    public void setComics(String comics) {
        mComics = comics;
    }
}


