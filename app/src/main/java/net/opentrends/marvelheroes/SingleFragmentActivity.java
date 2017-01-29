package net.opentrends.marvelheroes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

public abstract class SingleFragmentActivity extends FragmentActivity {
    //Las activities son idénticas por lo que para no copiar código hago una superclase y hago que las activities herenden de ella.

    protected abstract Fragment createFragment(); //Obliga a ser implementado en las hijas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment); //Despliega el activity_fragment

        FragmentManager fm = getSupportFragmentManager(); //Llamamos al FragmentManager
        Fragment fragment = fm.findFragmentById(R.id.fragment_container); //Solicitamos a FragmentManager el fragment por su ID de vista contenedora

        if(fragment == null){ //Si ningún fragment se corresponde con el ID...
            fragment = createFragment(); //Se crea un nuevo MarveHeroeFragment
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit(); //Crea una transacción de Fragment que lo añada a la lista, añade el fragment y confirma la transacción
        }

    }
}
