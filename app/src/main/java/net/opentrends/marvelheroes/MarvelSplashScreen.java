package net.opentrends.marvelheroes;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

public class MarvelSplashScreen extends Activity {

    private static final long SPLASH_SCREEN_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //Orientación vertial
        requestWindowFeature(Window.FEATURE_NO_TITLE);//Sin barra
        setContentView(R.layout.activity_splash_screen);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent().setClass(MarvelSplashScreen.this, MarvelHeroesListActivity.class); //Intent para abrir la siguiente
                startActivity(intent);

                finish();//Cierra la activity para que no vuelva atrás
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, SPLASH_SCREEN_DELAY); //No se abre hasta denro de 2 segundos

    }
}
