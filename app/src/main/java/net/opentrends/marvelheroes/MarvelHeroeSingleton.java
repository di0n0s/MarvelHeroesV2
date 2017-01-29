package net.opentrends.marvelheroes;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sfcar on 27/01/2017.
 */
public class MarvelHeroeSingleton {
    //Almacenamos la lista de heroes en un singleton

    private static MarvelHeroeSingleton sMarvelHeroeSingleton;
    private List<MarvelHeroe> mMarvelHeroes;

    public static MarvelHeroeSingleton getInstance() {//Context para la BBDD
        if(sMarvelHeroeSingleton==null){
            sMarvelHeroeSingleton = new MarvelHeroeSingleton(); //Si no hay una instancia la crea
        }
        return sMarvelHeroeSingleton;
    }

    public MarvelHeroeSingleton() { //Constructor privado, solo se puede acceder desde getInstance
        }

    public List<MarvelHeroe> getMarvelHeroes(){ //Acceder al listado de Heroes
        return mMarvelHeroes;
    }

    public void setMarvelHeroes(List<MarvelHeroe> marvelHeroes) {
        mMarvelHeroes = marvelHeroes;
    }

    public MarvelHeroe getMarvelHeroe(int id){ //Devuelve el heroe con el ID indicado
        for(MarvelHeroe marvelHeroe : mMarvelHeroes){
            if(marvelHeroe.getId() == id){
                return marvelHeroe;
            }
        } return null;
    }

}
