package net.opentrends.marvelheroes;

import android.support.v4.app.Fragment;

/**
 * Created by sfcar on 27/01/2017.
 */

public class MarvelHeroesListActivity extends SingleFragmentActivity {
    //Activity para albergar el Fragment del Listado de Heroes
    @Override
    protected Fragment createFragment() {
        return new MarvelHeroesListFragment();
    }
}
