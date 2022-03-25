package com.example.paper.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.paper.R;
import com.example.paper.databinding.FragmentHomeBinding;
import com.example.paper.model.movie.Movie;
import com.example.paper.model.movie.MovieCardListAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private ArrayList<Movie> movies;
    private MovieCardListAdapter moviesAdapter;
    ListView moviesView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Setup
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */

        // Data setup
        movies = new ArrayList<>();

        // Create the adapter to convert the array to views
        moviesAdapter = new MovieCardListAdapter(this.getContext(), movies);
        // Attach the adapter to a ListView
        moviesView = binding.MovieCardList;
        moviesView.setAdapter(moviesAdapter);

        // Launch API Call
        this.LaunchGetMovies(this);

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
                                        movie.get("overview").getAsString()
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}