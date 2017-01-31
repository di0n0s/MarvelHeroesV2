package net.opentrends.marvelheroes.controller;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.opentrends.marvelheroes.R;
import net.opentrends.marvelheroes.model.MarvelHeroe;
import net.opentrends.marvelheroes.model.MarvelHeroeSingleton;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarvelHeroesListFragment extends Fragment {
    //Fragment para el listado de heroes
    private static final String TAG = "HeroesListFragment";

    private EditText mLookingForEditText;

    private List<MarvelHeroe> mMarvelHeroes = new ArrayList<>();

    private RecyclerView mMarvelHeroeRecyclerView;
    private MarvelHeroeAdapter mMarvelHeroeAdapter = new MarvelHeroeAdapter(mMarvelHeroes);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); //Retiene el Fragment
        new FetchHeroesTask().execute(); //Arranca el AsyncTask

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marvel_heroes_list, container, false); //Inflamos el fragment

        mLookingForEditText = (EditText) view.findViewById(R.id.looking_for_name);
        mLookingForEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                List<MarvelHeroe> marvelHeroesFiltrado = new ArrayList<>();
                List<MarvelHeroe> marvelHeroesOriginales = new ArrayList<>();
                if(s.length()>=3){
                    for(MarvelHeroe marvelHeroe : mMarvelHeroes){
                        if(marvelHeroe.getName().toLowerCase().contains(s)){
                            marvelHeroesFiltrado.add(marvelHeroe);
                        }
                    }
                    mMarvelHeroeAdapter.mMarvelHeroes.addAll(marvelHeroesFiltrado);
                    updateHeroesUIBuscador(mMarvelHeroeAdapter);
                } else {
                    mMarvelHeroeAdapter.mMarvelHeroes.clear();
                    updateHeroesUI();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mMarvelHeroeRecyclerView = (RecyclerView) view.findViewById(R.id.heroe_recycler_view); //Cargamos el RecyclerView
        mMarvelHeroeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //Configuramos el LayoutManager de manera Vertical

        updateHeroesUI();

        return view;
    }


    private void updateHeroesUI(){
        MarvelHeroeSingleton marvelHeroeSingleton = MarvelHeroeSingleton.getInstance(); //Cargamos una instancia del Singleton

        if(isAdded()){ //Si el fragment está unido a la activity
            mMarvelHeroeRecyclerView.setAdapter(new MarvelHeroeAdapter(mMarvelHeroes)/*mMarvelHeroeAdapter*/); //Modificamos el RecyclerView añadiendo el adapter
            marvelHeroeSingleton.setMarvelHeroes(mMarvelHeroes);
        }
    }

    private void updateHeroesUIBuscador(MarvelHeroeAdapter marvelHeroeAdapter){
        MarvelHeroeSingleton marvelHeroeSingleton = MarvelHeroeSingleton.getInstance(); //Cargamos una instancia del Singleton

        if(isAdded()){ //Si el fragment está unido a la activity
            mMarvelHeroeRecyclerView.setAdapter(marvelHeroeAdapter); //Modificamos el RecyclerView añadiendo el adapter
            marvelHeroeSingleton.setMarvelHeroes(marvelHeroeAdapter.mMarvelHeroes);
        }
    }

    private class MarvelHeroeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{//Creamos el ViewHolder

        private ImageView mMHImageView;
        private TextView mNameTextView;
        private TextView mDescriptionTextView;

        private MarvelHeroe mMarvelHeroe;

        public MarvelHeroeHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);//Configuramos MarvelHeroeHolder como receptor de los eventos táctiles

            mMHImageView = (ImageView) itemView.findViewById(R.id.list_item_marvel_heroe_image_view);
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_marvel_heroe_name_text_view);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.list_item_marvel_heroe_description_text_view); //Conectamos los widgets

        }

        public void bindMarvelHeroe(MarvelHeroe marvelHeroe){ //Bindeamos cada objeto de Heroe con la vista
            mMarvelHeroe = marvelHeroe;
            Picasso.with(getActivity())
                    .load(mMarvelHeroe.getImage())
                    .placeholder(null)
                    .resize(75,75)
                    .into(mMHImageView);
            mNameTextView.setText(mMarvelHeroe.getName());
            mDescriptionTextView.setText(mMarvelHeroe.getDescription());
        }

        @Override
        public void onClick(View v) {
            Intent intent = MarvelHeroeActivity.newIntent(getActivity(), mMarvelHeroe.getId()); //Llamamos al método newIntent para generar un intent explicito pasandole el contexto y el ID del Heroe
            startActivity(intent); //Abre una nueva MarvelHeroeActivity

        }

    }

    private class MarvelHeroeAdapter extends RecyclerView.Adapter<MarvelHeroeHolder> /*implements Filterable*/ { //Implementamos Filterable para hacer la búsqueda del heroe
        //Creamos el adapter
        private List<MarvelHeroe> mMarvelHeroes;


        public MarvelHeroeAdapter(List<MarvelHeroe> marvelHeroes) {
            mMarvelHeroes = marvelHeroes;
        }

        @Override
        public MarvelHeroeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity()); //Cargamos el "inflador, es decir la activity
            View view = layoutInflater.inflate(R.layout.list_item_marvel_heroe, parent, false); //Creamos una vista. Desplegamos un layout que contiene una sola vista
            return new MarvelHeroeHolder(view); //Devolvemos la vista encapsulada en el ViewHolder
        }

        @Override
        public void onBindViewHolder(MarvelHeroeHolder holder, int position) {
            MarvelHeroe marvelHeroe = mMarvelHeroes.get(position); //Guarda en marvelHeroe el heroe del listado respecto a la posición que le pasamos
            holder.bindMarvelHeroe(marvelHeroe); //Enlazamos una vista de un ViewHolder a un objeto de heroe
            //mLookingForMarvelHeroe.queueLookingForMarvelHeroe(holder, marvelHeroe.getName());

        }

        @Override
        public int getItemCount() {
            return mMarvelHeroes.size(); //Devuelve el tamaño de la lista (filtrada o no)
        }
    }

    private class FetchHeroesTask extends AsyncTask<Void, Void, List<MarvelHeroe>>{ //AsyncTask produce un List de heroes

        @Override
        protected List<MarvelHeroe> doInBackground(Void... params) { //En background
            return new APIFetchr().getAllHeroes();
        }

        @Override
        protected void onPostExecute(List<MarvelHeroe> marvelHeroes) {
            mMarvelHeroes = marvelHeroes;
            updateHeroesUI();
        }
    }





}


