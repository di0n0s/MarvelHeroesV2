package net.opentrends.marvelheroes;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
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
        new FetchItemsTask().execute(); //Arranca el AsyncTask
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
                    mMarvelHeroeAdapter.getFilter().filter(s.toString());
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
        //List<MarvelHeroe> marvelHeroes = marvelHeroeSingleton.getMarvelHeroes(); //Cargamos los heroes sobre el List

        //mMarvelHeroeAdapter = new MarvelHeroeAdapter(marvelHeroes); //Cargamos una nueva instancia del adaptador con el list
        //mMarvelHeroeRecyclerView.setAdapter(new MarvelHeroeAdapter(mMarvelHeroeAdapter));
        if(isAdded()){ //Si el fragment está unido a la activity
            mMarvelHeroeRecyclerView.setAdapter(new MarvelHeroeAdapter(mMarvelHeroes)/*mMarvelHeroeAdapter*/); //Modificamos el RecyclerView añadiendo el adapter
            marvelHeroeSingleton.setMarvelHeroes(mMarvelHeroes);
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

    private class MarvelHeroeAdapter extends RecyclerView.Adapter<MarvelHeroeHolder> implements Filterable{ //Implementamos Filterable para hacer la búsqueda del heroe
        //Creamos el adapter
        private List<MarvelHeroe> mMarvelHeroes;
        private List<MarvelHeroe> mMarvelHeroesFilter; //List filtrado
        private CustomFilter mCustomFilter;

        public MarvelHeroeAdapter(List<MarvelHeroe> marvelHeroes) {
            mMarvelHeroes = marvelHeroes;
            mMarvelHeroesFilter = new ArrayList<>(); //Creamos un nuevo list de heroes filtrados
            mMarvelHeroesFilter.addAll(marvelHeroes); //Los añadimos
            mCustomFilter = new CustomFilter(MarvelHeroeAdapter.this);
        }

        @Override
        public MarvelHeroeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity()); //Cargamos el "inflador, es decir la activity
            View view = layoutInflater.inflate(R.layout.list_item_marvel_heroe, parent, false); //Creamos una vista. Desplegamos un layout que contiene una sola vista
            return new MarvelHeroeHolder(view); //Devolvemos la vista encapsulada en el ViewHolder
        }

        @Override
        public void onBindViewHolder(MarvelHeroeHolder holder, int position) {
            MarvelHeroe marvelHeroe = mMarvelHeroesFilter.get(position); //Guarda en marvelHeroe el heroe del listado respecto a la posición que le pasamos
            holder.bindMarvelHeroe(marvelHeroe); //Enlazamos una vista de un ViewHolder a un objeto de heroe

        }

        @Override
        public int getItemCount() {
            return mMarvelHeroesFilter.size(); //Devuelve el tamaño de la lista (filtrada o no)
        }

        @Override
        public Filter getFilter() {
            return mCustomFilter;
        }

        public class CustomFilter extends Filter{ //Clase interna para hacer el filtrado con el buscador

            private MarvelHeroeAdapter mMarvelHeroeAdapter;

            private CustomFilter(MarvelHeroeAdapter marvelHeroeAdapter) {
                super();
                mMarvelHeroeAdapter = marvelHeroeAdapter;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
            mMarvelHeroesFilter.clear();
                final FilterResults results = new FilterResults();
                if(constraint.length() > 3){ //A partir de 3 caracteres
                mMarvelHeroesFilter.addAll(mMarvelHeroes);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (final MarvelHeroe marvelHeroe : mMarvelHeroes){
                        if(marvelHeroe.getName().toLowerCase().contains(filterPattern)){
                        mMarvelHeroesFilter.add(marvelHeroe);
                            Log.i(TAG,"Tamaño de Heroes Filter: "+mMarvelHeroesFilter.size());
                        }
                    }
                }
                results.values = mMarvelHeroesFilter;
                results.count = mMarvelHeroesFilter.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                MarvelHeroeSingleton marvelHeroeSingleton = MarvelHeroeSingleton.getInstance();

//                if(isAdded()) { //Si el fragment está unido a la activity
                    mMarvelHeroeRecyclerView.setAdapter(new MarvelHeroeAdapter(mMarvelHeroesFilter)); //Modificamos el RecyclerView añadiendo el adapter
                    marvelHeroeSingleton.setMarvelHeroes(mMarvelHeroesFilter);
//                } //else this.mMarvelHeroeAdapter.notifyDataSetChanged();

                mMarvelHeroeAdapter.notifyDataSetChanged();

            }
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<MarvelHeroe>>{ //AsyncTask produce un List de heroes

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


