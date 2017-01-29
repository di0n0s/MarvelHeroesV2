package net.opentrends.marvelheroes;

/**
 * Created by sfcar on 28/01/2017.
 */

public class MarvelEvent {

    private int mId;
    private MarvelHeroe mIdHeroe;
    private String mTitle;
    private String mDescription;
    private String mImage;

    public MarvelEvent(int id, MarvelHeroe idHeroe, String title, String description, String image) {
        mId = id;
        mIdHeroe = idHeroe;
        mTitle = title;
        mDescription = description;
        mImage = image;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public MarvelHeroe getIdHeroe() {
        return mIdHeroe;
    }

    public void setIdHeroe(MarvelHeroe idHeroe) {
        mIdHeroe = idHeroe;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
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
}
