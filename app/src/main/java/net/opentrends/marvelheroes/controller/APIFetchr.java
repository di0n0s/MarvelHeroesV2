package net.opentrends.marvelheroes.controller;

import android.net.Uri;
import android.util.Log;

import net.opentrends.marvelheroes.model.MarvelComic;
import net.opentrends.marvelheroes.model.MarvelEvent;
import net.opentrends.marvelheroes.model.MarvelHeroe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sfcar on 28/01/2017.
 */

public class APIFetchr { //Clase para establecer la conexión

    private static final String TAG = "APIFetchr";

    private static final String API_KEY = "5b9b2f8b3fb573facdccb65dee146c2e";

    private static final String HASH = "eab2c8e4873a9d69b088a76920253623";

    private static final String TS = "1";

    private static final String LIMIT = "100";

    public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec); //Crea un objeto URL al pasarle un String
        HttpURLConnection connection = (HttpURLConnection)url.openConnection(); //Abre una conexión apuntando a la URL

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream(); //Ejecutamos la conexión (entrada) y proporciona los bytes

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage()+" : with "+urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0){ //Invocamos método reead hasta que la conexión agote datos
                out.write(buffer, 0, bytesRead);
            }
            out.close(); //Cerramos la conexion
            return out.toByteArray(); //Devolvemos el array de bytes
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException{ //Convierte los bytes recogidos en String
        return new String(getUrlBytes(urlSpec));
    }

    public List<MarvelHeroe> getAllHeroes(){
        List<MarvelHeroe> marvelHeroes = new ArrayList<>();
        try {
            String url = Uri.parse("http://gateway.marvel.com:80/v1/public/characters")
                    .buildUpon()
                    .appendQueryParameter("limit", LIMIT)
                    .appendQueryParameter("ts", TS)
                    .appendQueryParameter("apikey", API_KEY)
                    .appendQueryParameter("hash", HASH)
                    .build().toString(); //Construimos la url completa con los parametros

            String jsonString = getUrlString(url); //Ejecutamos
            Log.i(TAG, "Received JSON: "+ jsonString);
            JSONObject jsonBody = new JSONObject(jsonString); //Alberga el JSON en un objeto JSON
            parseHeroes(marvelHeroes, jsonBody);
        } catch (IOException ioe){
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return marvelHeroes;
    }

    public List<MarvelHeroe> getAllHeroesStartsWith(String name){
        List<MarvelHeroe> marvelHeroes = new ArrayList<>();
        try {
            String url = Uri.parse("http://gateway.marvel.com:80/v1/public/characters")
                    .buildUpon()
                    .appendQueryParameter("nameStartsWith", name)
                    .appendQueryParameter("limit", LIMIT)
                    .appendQueryParameter("ts", TS)
                    .appendQueryParameter("apikey", API_KEY)
                    .appendQueryParameter("hash", HASH)
                    .build().toString(); //Construimos la url completa con los parametros

            String jsonString = getUrlString(url); //Ejecutamos
            Log.i(TAG, "Received JSON: "+ jsonString);
            JSONObject jsonBody = new JSONObject(jsonString); //Alberga el JSON en un objeto JSON
            parseHeroes(marvelHeroes, jsonBody);
        } catch (IOException ioe){
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return marvelHeroes;
    }

    public List<MarvelComic> getAllComicsOfHeroe(MarvelHeroe marvelHeroe){
        List<MarvelComic> marvelComics = new ArrayList<>();
        try {
            String url = Uri.parse("http://gateway.marvel.com:80/v1/public/characters/"+marvelHeroe.getId()+"/comics")
                    .buildUpon()
                    .appendQueryParameter("limit", LIMIT)
                    .appendQueryParameter("ts", TS)
                    .appendQueryParameter("apikey", API_KEY)
                    .appendQueryParameter("hash", HASH)
                    .build().toString(); //Construimos la url completa con los parametros

            String jsonString = getUrlString(url); //Ejecutamos
            Log.i(TAG, "Received JSON: "+ jsonString);
            JSONObject jsonBody = new JSONObject(jsonString); //Alberga el JSON en un objeto JSON
            parseComics(marvelComics, jsonBody);
        } catch (IOException ioe){
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
       return marvelComics;
    }

    public List<MarvelEvent> getAllEventsofHeroe(MarvelHeroe marvelHeroe){
        List<MarvelEvent> marvelEvents = new ArrayList<>();
        try {
            String url = Uri.parse("http://gateway.marvel.com:80/v1/public/characters/"+marvelHeroe.getId()+"/events")
                    .buildUpon()
                    .appendQueryParameter("limit", LIMIT)
                    .appendQueryParameter("ts", TS)
                    .appendQueryParameter("apikey", API_KEY)
                    .appendQueryParameter("hash", HASH)
                    .build().toString(); //Construimos la url completa con los parametros

            String jsonString = getUrlString(url); //Ejecutamos
            Log.i(TAG, "Received JSON: "+ jsonString);
            JSONObject jsonBody = new JSONObject(jsonString); //Alberga el JSON en un objeto JSON
            parseEvents(marvelEvents, jsonBody);
        } catch (IOException ioe){
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return marvelEvents;
    }


    private void parseHeroes (List<MarvelHeroe> marvelHeroes, JSONObject jsonBody) throws JSONException { //Coge el JSON y lo alberga según el modelo de datos
        JSONObject heroesJsonObject = jsonBody.getJSONObject("data");
        JSONArray heroesJsonArray = heroesJsonObject.getJSONArray("results");

        for(int i = 0; i < heroesJsonArray.length(); i++){
            JSONObject heroeJsonObject = heroesJsonArray.getJSONObject(i);

            MarvelHeroe marvelHeroe = new MarvelHeroe();
            marvelHeroe.setId(heroeJsonObject.getInt("id"));
            marvelHeroe.setName(heroeJsonObject.getString("name"));
            marvelHeroe.setDescription(heroeJsonObject.getString("description"));
            JSONObject image = heroeJsonObject.getJSONObject("thumbnail");
            String path = image.getString("path");
            String ext = image.getString("extension");
            marvelHeroe.setImage(path+"."+ext);
            JSONArray urlJSONArray = heroeJsonObject.getJSONArray("urls");
            for(int j = 0; j < urlJSONArray.length(); j++){
                JSONObject urlJSONObject = urlJSONArray.getJSONObject(j);
                if (j==0){
                    marvelHeroe.setDetails(urlJSONObject.getString("url"));
                } else if(j==1){
                    marvelHeroe.setWiki(urlJSONObject.getString("url"));
                } else if(j==2){
                    marvelHeroe.setComics(urlJSONObject.getString("url"));
                }
            }
            //Log.i(TAG, "Urls: "+marvelHeroe.getComics());

            marvelHeroes.add(marvelHeroe);
        }
    }

    private void parseComics (List<MarvelComic> marvelComics, JSONObject jsonBody) throws JSONException { //Coge el JSON y lo alberga según el modelo de datos
        JSONObject comicsJsonObject = jsonBody.getJSONObject("data");
        JSONArray comicsJsonArray = comicsJsonObject.getJSONArray("results");

        for(int i = 0; i < comicsJsonArray.length(); i++){
            JSONObject comicJsonObject = comicsJsonArray.getJSONObject(i);

            MarvelComic marvelComic = new MarvelComic();
            marvelComic.setId(comicJsonObject.getInt("id"));
            marvelComic.setTitle(comicJsonObject.getString("title"));
            marvelComic.setDecription(comicJsonObject.getString("description"));
            marvelComic.setImage(comicJsonObject.getString("thumbnail"));
            JSONObject image = comicJsonObject.getJSONObject("thumbnail");
            String path = image.getString("path");
            String ext = image.getString("extension");
            marvelComic.setImage(path+"."+ext);
            marvelComics.add(marvelComic);
        }
    }

    private void parseEvents (List<MarvelEvent> marvelEvents, JSONObject jsonBody) throws JSONException { //Coge el JSON y lo alberga según el modelo de datos
        JSONObject eventsJsonObject = jsonBody.getJSONObject("data");
        JSONArray eventsJsonArray = eventsJsonObject.getJSONArray("results");

        for(int i = 0; i < eventsJsonArray.length(); i++){
            JSONObject eventJsonObject = eventsJsonArray.getJSONObject(i);

            MarvelEvent marvelEvent = new MarvelEvent();
            marvelEvent.setId(eventJsonObject.getInt("id"));
            marvelEvent.setTitle(eventJsonObject.getString("title"));
            marvelEvent.setDescription(eventJsonObject.getString("description"));
            marvelEvent.setImage(eventJsonObject.getString("thumbnail"));
            JSONObject image = eventJsonObject.getJSONObject("thumbnail");
            String path = image.getString("path");
            String ext = image.getString("extension");
            marvelEvent.setImage(path+"."+ext);
            marvelEvents.add(marvelEvent);
        }
    }

}
