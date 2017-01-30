package net.opentrends.marvelheroes;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by sfcar on 30/01/2017.
 */

public class LookingForMarvelHeroe<T> extends HandlerThread {

    private static final String TAG = "LookingForMarvelHeroe";

    private static final int MESSAGE_DOWNLOAD = 0; //Identificar mensajes como solicitudes de descarga (WHAT)

    private Handler mRequestHandler; //Almacena una referencia  al manejador responsable de poner en cola la solicitud de descarga
    private ConcurrentMap<T,String> mRequestMap = new ConcurrentHashMap<>();

    private Handler mResponseHandler; //Guarda el manejador pasado desde el hilo principal
    private LookingForMarvelHeroeListener<T> mLookingForMarvelHeroeListener;

    public interface LookingForMarvelHeroeListener<T>{
        void onLookingForMarvelHeroeDownloaded(T target, List<MarvelHeroe> marvelHeroes); //recibe llamada cuando los heroes están descargados por completo y estén listos para mostrarse en la UI
    }

    public void setLookingForMarvelHeroeListener(LookingForMarvelHeroeListener<T> listener){
        mLookingForMarvelHeroeListener = listener;
    }


    public LookingForMarvelHeroe(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    public void queueLookingForMarvelHeroe(T target, String name){
        Log.i(TAG, "Got a Heroe: "+name);

        if(name==null){
            mRequestMap.remove(target);
        } else{
            mRequestMap.put(target, name);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget();
        }

    }

    public void clearQueue(){
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_DOWNLOAD){
                    T target = (T) msg.obj;
                    Log.i(TAG, "Got a request for Heroe: "+mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };

    }

    private void handleRequest(final T target) {
        final String name = mRequestMap.get(target);

        if(name == null){
            return;
        }
        final List<MarvelHeroe> marvelHeroes = new APIFetchr().getAllHeroesStartsWith(name);

        mResponseHandler.post(new Runnable() { //Verifica el requestMap
            @Override
            public void run() {
                if(mRequestMap.get(target) != null){
                    return;
                }
                mRequestMap.remove(target);
                mLookingForMarvelHeroeListener.onLookingForMarvelHeroeDownloaded(target, marvelHeroes);
            }
        });
    }
}
