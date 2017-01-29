package net.opentrends.marvelheroes;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarvelHeroeFragment extends Fragment /*implements View.OnClickListener*/ {

    private static final String ARG_MARVELHEROE_ID = "marvel_heroe_id"; //Clave del argumento del Fragment

    private MarvelHeroe mMarvelHeroe;
    private MarvelComic mMarvelComic;
    private List<MarvelComic> mMarvelComics = new ArrayList<>();

    private ImageView mMHImageView;
    private TextView mMHNameTextView;
    private TextView mMHDescriptionTextView;
    private Button mDetailButton;
    private Button mWikiButton;
    private Button mComicsButton;
    private TabLayout mTabLayout;

    private RecyclerView mMarvelComicRecyclerView;
    private MarvelComicAdapter mMarvelComicAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); //Retiene el Fragment
        new FetchItemsTask().execute(); //Arranca el AsyncTask

        int marvelHeroId = (int) getArguments().getSerializable(ARG_MARVELHEROE_ID);//Recuperamos los argumentos del fragment
        mMarvelHeroe = MarvelHeroeSingleton.getInstance().getMarvelHeroe(marvelHeroId); //Recuperado, lo utilizamos para buscar el Heroe en el Singleton
        //mMarvelComic = MarvelComicSingleton.getInstance(getActivity()/*, marvelHeroId*/).getMarvelComic(marvelHeroId); // Y los comics del heroe

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_marvel_heroe, container, false); //false --> no se va a añadir la vista despleagada a la del padre, sino al código del Activity
        //Inflamos la vista

        mMHImageView = (ImageView) view.findViewById(R.id.marvel_heroe_image_view);//Conectamos los widgets
        Picasso.with(getActivity())
                .load(mMarvelHeroe.getImage())
                .placeholder(null)
                .resize(75,75)
                .into(mMHImageView);

        mMHNameTextView = (TextView) view.findViewById(R.id.marvel_heroe_name_text_view);
        mMHNameTextView.setText(mMarvelHeroe.getName());//Cargas el nombre del Heroe

        mMHDescriptionTextView = (TextView) view.findViewById(R.id.marvel_heroe_description_text_view);
        mMHDescriptionTextView.setText(mMarvelHeroe.getDescription());

        mDetailButton = (Button) view.findViewById(R.id.detail_button);
        mDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMarvelHeroe.getDetails()!=null){
                    Uri uri = Uri.parse(mMarvelHeroe.getDetails());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else Toast.makeText(getActivity(),R.string.no_resource, Toast.LENGTH_SHORT).show();
            }
        });
        mWikiButton = (Button) view.findViewById(R.id.wiki_button);
        mWikiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMarvelHeroe.getWiki()!=null){
                    Uri uri = Uri.parse(mMarvelHeroe.getWiki());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else Toast.makeText(getActivity(),R.string.no_resource, Toast.LENGTH_SHORT).show();
            }
        });
        mComicsButton = (Button) view.findViewById(R.id.comics_button);
        mComicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMarvelHeroe.getComics()!=null){
                    Uri uri = Uri.parse(mMarvelHeroe.getComics());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else Toast.makeText(getActivity(),R.string.no_resource, Toast.LENGTH_SHORT).show();
            }
        });

        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_comics)//Añadimos las pestañas
                .setCustomView(mMarvelComicRecyclerView));
        mTabLayout.addTab(mTabLayout.newTab()
                .setCustomView(null)
                .setText(R.string.tab_eventos));

        mMarvelComicRecyclerView = (RecyclerView) view.findViewById(R.id.comics_recycler_view);//Cargamos el RecyclerView
        mMarvelComicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //Configuramos el LayoutManager de manera Vertical

        updateComicsUI();

        return view;
    }

    private void updateComicsUI(){
        //MarvelComicSingleton marvelComicSingleton = MarvelComicSingleton.getInstance(getActivity());

        if(isAdded()) {
            MarvelHeroe marvelHeroe = new MarvelHeroe();
            List<MarvelComic> marvelComics = marvelHeroe.getMarvelComics();   //marvelComicSingleton.getMarvelComics();

            mMarvelComicAdapter = new MarvelComicAdapter(marvelComics);
            mMarvelComicRecyclerView.setAdapter(new MarvelComicAdapter(mMarvelComics)/*mMarvelComicAdapter*/);
        }
    }

    private class MarvelComicHolder extends RecyclerView.ViewHolder{

        private ImageView mMCImageView;
        private TextView mTitleTextView;
        private TextView mDecriptionTextView;

        private MarvelComic mMarvelComic;

        public MarvelComicHolder(View itemView) {
            super(itemView);

            mMCImageView = (ImageView) itemView.findViewById(R.id.list_item_marvel_comic_image_view); //CAMBIAR
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_marvel_comic_title_text_view);
            mDecriptionTextView = (TextView) itemView.findViewById(R.id.list_item_marvel_comic_description_text_view); //Conectamos los widgets

        }

        public void bindMarvelComic(MarvelComic marvelComic){
            mMarvelComic = marvelComic;
            Picasso.with(getActivity())
                    .load(mMarvelComic.getImage())
                    .placeholder(null)
                    .resize(75,75)
                    .into(mMCImageView);
            mTitleTextView.setText(mMarvelComic.getTitle());
            mDecriptionTextView.setText(mMarvelComic.getDecription());
        }

    }

    private class MarvelComicAdapter extends RecyclerView.Adapter<MarvelComicHolder>{

        private List<MarvelComic> mMarvelComics;

        public MarvelComicAdapter(List<MarvelComic> marvelComics) {
            mMarvelComics = marvelComics;
        }

        @Override
        public MarvelComicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_marvel_comic, parent, false);
            return new MarvelComicHolder(view);
        }

        @Override
        public void onBindViewHolder(MarvelComicHolder holder, int position) {
            MarvelComic marvelComic = mMarvelComics.get(position);
            holder.bindMarvelComic(marvelComic);

        }

        @Override
        public int getItemCount() {
            return mMarvelComics.size();
        }
    }
    /*
    @Override
    public void onClick(View v) {
        String message = "Hola";
        switch (v.getId()) {
            case R.id.detail_button:
                //Log.d()
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                break;
            case R.id.wiki_button:
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                break;
            case R.id.comics_button:
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                break;
        }

    }
    */
    public static MarvelHeroeFragment newInstance(int marvelHeroId){
        Bundle args = new Bundle();
        args.putInt(ARG_MARVELHEROE_ID, marvelHeroId); //Creamos un conjunto de argumentos

        MarvelHeroeFragment fragment = new MarvelHeroeFragment(); //Creamos una instancia del fragment
        fragment.setArguments(args); //Añadimos los argumentos
        return fragment; //Devolvemos el fragment

    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<MarvelComic>> { //AsyncTask produce un List de heroes

        @Override
        protected List<MarvelComic> doInBackground(Void... params) { //En background
            return new APIFetchr().getAllComicsOfHeroe(mMarvelHeroe);
        }

        @Override
        protected void onPostExecute(List<MarvelComic> marvelComics) {
            mMarvelComics = marvelComics;
            updateComicsUI();
        }
    }
}
