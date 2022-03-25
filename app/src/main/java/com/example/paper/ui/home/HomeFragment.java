package com.example.paper.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paper.R;
import com.example.paper.databinding.FragmentHomeBinding;
import com.example.paper.model.movie.Movie;
import com.example.paper.model.movie.MovieCardListAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    Movie popular_movie;
    ImageView v_popular_poster;
    TextView v_popular_title;
    TextView v_popular_overview;
    Button v_popular_button;

    private ArrayList<Movie> movies;
    private MovieCardListAdapter moviesAdapter;
    RecyclerView moviesView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Setup
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Select components
        // Attach the adapter to a ListView
        moviesView = (RecyclerView) binding.MovieCardList;

        // Popular movie
        //
        // init
        this.LaunchGetPopularMovie(this, container);

        // Movies list
        //
        movies = new ArrayList<>();
        this.LaunchGetMovies(this);

        moviesView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        moviesView.setLayoutManager(llm);

        // Create the adapter to convert the array to views
        moviesAdapter = new MovieCardListAdapter(movies);
        moviesView.setAdapter(moviesAdapter);

        return root;
    }

    public void LaunchGetMovies(Fragment context) {
        String API_KEY = "8b466a6b5e68647ae3e550470e2bb324"; // getResources().getString(R.string.api_key);

        // Get the data from the server with Ion
        Ion.with(context)
                .load("https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.e("MainActivity", "Error: " + e.getMessage());
                        } else {
                            Log.i("MainActivity", "Success: " + result.toString());
                            JsonArray results = result.getAsJsonArray("results");
                            for (int i = 0; i < results.size(); i++) {
                                JsonObject movie = results.get(i).getAsJsonObject();
                                movies.add(new Movie(
                                        movie.get("id").getAsString(),
                                        movie.get("title").getAsString(),
                                        movie.get("release_date").getAsString(),
                                        movie.get("poster_path").getAsString(),
                                        movie.get("adult").getAsBoolean(),
                                        movie.get("overview").getAsString(),
                                        movie.get("vote_average").getAsString()
                                ));
                            }
                            // Print the films
                            for (Movie movie : movies) {
                                Log.i("MainActivity", movie.toString());
                            }

                            moviesAdapter.notifyDataSetChanged();
                        }
                    }
                });
        }

    public void LaunchGetPopularMovie(Fragment context, ViewGroup container) {
        String API_KEY = "8b466a6b5e68647ae3e550470e2bb324"; // getResources().getString(R.string.api_key);

        // Get the data from the server with Ion
        Ion.with(context)
                .load("https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.e("MainActivity", "Error: " + e.getMessage());
                        } else {
                            Log.i("MainActivity", "Success: " + result.toString());
                            JsonArray results = result.getAsJsonArray("results");
                            JsonObject movie = results.get(10).getAsJsonObject();
                            popular_movie = new Movie(
                                    movie.get("id").getAsString(),
                                    movie.get("title").getAsString(),
                                    movie.get("release_date").getAsString(),
                                    movie.get("poster_path").getAsString(),
                                    movie.get("adult").getAsBoolean(),
                                    movie.get("overview").getAsString(),
                                    movie.get("vote_average").getAsString()
                            );

                            // Print the films
                            Log.i("MainActivity - Popular Movie", popular_movie.toString());


                            v_popular_poster = container.findViewById(R.id.popular_poster);
                            v_popular_title = container.findViewById(R.id.popular_title);
                            v_popular_button = container.findViewById(R.id.popular_discover);
                            v_popular_overview = container.findViewById(R.id.popular_overview);

                            Log.d("Popular Movie", String.valueOf(popular_movie));

                            Uri imagePath = Uri.parse(popular_movie.getPosterPath());
                            Picasso.get()
                                    .load("https://image.tmdb.org/t/p/w300_and_h450_bestv2/" + imagePath)
                                    .into(v_popular_poster, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Toast.makeText(getContext(), "Image introuvable", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            v_popular_title.setText(popular_movie.getName());
                            v_popular_overview.setText(popular_movie.getOverview());
                        }
                    }
                });
        }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}