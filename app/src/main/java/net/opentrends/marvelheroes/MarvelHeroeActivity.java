package net.opentrends.marvelheroes;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by sfcar on 27/01/2017.
 */

public class MarvelHeroeActivity extends SingleFragmentActivity {

    private static final String EXTRA_MARVELHEROE_ID = "net.opentrends.marvelheroes.marvelheroe_id"; //Clave del Extra

    @Override
    protected Fragment createFragment() {

        int marvelHeroeId = (int) getIntent().getSerializableExtra(EXTRA_MARVELHEROE_ID); //Recuperamos el extra (ID)
        return MarvelHeroeFragment.newInstance(marvelHeroeId); //Recuperado, creamos una nueva instancia de MarvelHeroFragment
    }

    public static Intent newIntent (Context packageContext, int marvelHeroId){
        Intent intent = new Intent(packageContext, MarvelHeroeActivity.class); //Creamos un intent explicito
        intent.putExtra(EXTRA_MARVELHEROE_ID, marvelHeroId); //Le añadimos la clave string y el valor de la clave que es el id del heroe.
        return intent;
    }
}
