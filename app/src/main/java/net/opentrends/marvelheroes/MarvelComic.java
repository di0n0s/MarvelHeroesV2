package net.opentrends.marvelheroes;

/**
 * Created by sfcar on 28/01/2017.
 */

public class MarvelComic {

    private int mId;
    private MarvelHeroe mIdHeroe;
    private String mTitle;
    private String mDecription;
    private String mImage;

    public MarvelComic() {
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

    public String getDecription() {
        return mDecription;
    }

    public void setDecription(String decription) {
        mDecription = decription;
    }

    public String getImage() { //CAMBIAR
        return mImage;
    }

    public void setImage(String image) { //CAMBIAR!
        mImage = image;
    }

}
