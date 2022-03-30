package com.example.paper.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paper.databinding.FragmentHomeBinding;
import com.example.paper.model.movie.Movie;
import com.example.paper.model.movie.MovieCardListAdapter;
import com.example.paper.ui.movie_page.MoviePageActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    // API KEY
    String API_KEY = "8b466a6b5e68647ae3e550470e2bb324";

    // More Popular Movie
    Movie popular_movie;
    ImageView v_popular_poster;
    TextView v_popular_title;
    TextView v_popular_overview;
    Button v_popular_button;

    // Last Movies
    private ArrayList<Movie> lastMovies;
    private MovieCardListAdapter lastMoviesAdapter;
    RecyclerView lastMoviesView;

    // Best Movies
    private ArrayList<Movie> bestMovies;
    private MovieCardListAdapter bestMoviesAdapter;
    RecyclerView bestMoviesView;

    // Best Movies
    private ArrayList<Movie> actionsMovies;
    private MovieCardListAdapter actionsMoviesAdapter;
    RecyclerView actionsMoviesView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Setup
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Select components
        //
        // Last Movies Recycler View
        lastMoviesView = (RecyclerView) binding.LastMoviesList;
        // Best Movies Recycler View
        bestMoviesView = (RecyclerView) binding.BestMoviesList;
        // Action Movies Recycler View
        actionsMoviesView = (RecyclerView) binding.ActionsMoviesList;
        // Popular Movie
        v_popular_poster = binding.popularPoster;
        v_popular_title = binding.popularTitle;
        v_popular_button = binding.popularDiscover;
        v_popular_overview = binding.popularOverview;

        // Data
        //
        // Setup
        lastMovies = new ArrayList<>();
        bestMovies = new ArrayList<>();
        actionsMovies = new ArrayList<>();

        // Data
        //
        // Display Setup
        // Last Movies View
        lastMoviesView.setHasFixedSize(true);
        LinearLayoutManager llmLASTMOVIES = new LinearLayoutManager(this.getContext());
        llmLASTMOVIES.setOrientation(LinearLayoutManager.HORIZONTAL);
        lastMoviesView.setLayoutManager(llmLASTMOVIES);
        // Create the adapter to convert the array to views
        lastMoviesAdapter = new MovieCardListAdapter(lastMovies);
        lastMoviesView.setAdapter(lastMoviesAdapter);

        // Best Movies View
        bestMoviesView.setHasFixedSize(true);
        LinearLayoutManager llmBESTMOVIES = new LinearLayoutManager(this.getContext());
        llmBESTMOVIES.setOrientation(LinearLayoutManager.HORIZONTAL);
        bestMoviesView.setLayoutManager(llmBESTMOVIES);
        // Create the adapter to convert the array to views
        bestMoviesAdapter = new MovieCardListAdapter(bestMovies);
        bestMoviesView.setAdapter(bestMoviesAdapter);

        // Actions Movies View
        actionsMoviesView.setHasFixedSize(true);
        LinearLayoutManager llmACTIONSMOVIES = new LinearLayoutManager(this.getContext());
        llmACTIONSMOVIES.setOrientation(LinearLayoutManager.HORIZONTAL);
        actionsMoviesView.setLayoutManager(llmACTIONSMOVIES);
        // Create the adapter to convert the array to views
        actionsMoviesAdapter = new MovieCardListAdapter(actionsMovies);
        actionsMoviesView.setAdapter(actionsMoviesAdapter);

        // Data
        //
        // Call
        this.LaunchGetPopularMovie(this);
        this.LaunchGetMovies(this);
        this.LaunchGetBestMovies(this);
        this.LaunchGetActionsMovies(this);

        return root;
    }

    public void LaunchGetMovies(Fragment context) {
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
                                lastMovies.add(new Movie(
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
                            for (Movie movie : lastMovies) {
                                Log.i("MainActivity", movie.toString());
                            }

                            lastMoviesAdapter.notifyDataSetChanged();
                        }
                    }
                });
        }

    public void LaunchGetPopularMovie(Fragment context) {
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

                            JsonObject movie = results.get(0).getAsJsonObject();
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

                            Log.d("Popular Movie", String.valueOf(popular_movie));

                            Uri imagePath = Uri.parse(popular_movie.getPosterPath());
                            Picasso.get()
                                    .load(imagePath)
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
                            v_popular_button.setOnClickListener(v -> {
                                Intent movieIntent = new Intent(v.getContext(), MoviePageActivity.class);
                                movieIntent.putExtra("movie", popular_movie);
                                startActivity(movieIntent);
                            });
                        }
                    }
                });
        }

    public void LaunchGetBestMovies(Fragment context) {
        // Get the data from the server with Ion
        Ion.with(context)
                .load("https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&vote_average.gte=8")
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
                                bestMovies.add(new Movie(
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
                            for (Movie movie : bestMovies) {
                                Log.i("MainActivity", movie.toString());
                            }

                            bestMoviesAdapter.notifyDataSetChanged();
                        }
                    }
                });
        }


    public void LaunchGetActionsMovies(Fragment context) {
        // Get the data from the server with Ion
        Ion.with(context)
                .load("https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&with_genres=28")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.e("MainActivity", "Error: " + e.getMessage());
                        } else {
                            Log.i("MainActivity ACTION", "Success: " + result.toString());
                            JsonArray results = result.getAsJsonArray("results");
                            for (int i = 0; i < results.size(); i++) {
                                JsonObject movie = results.get(i).getAsJsonObject();
                                actionsMovies.add(new Movie(
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
                            for (Movie movie : actionsMovies) {
                                Log.i("MainActivity", movie.toString());
                            }

                            bestMoviesAdapter.notifyDataSetChanged();
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
