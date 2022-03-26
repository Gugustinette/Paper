package com.example.paper.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paper.databinding.FragmentSearchBinding;
import com.example.paper.model.movie.Movie;
import com.example.paper.model.movie.MovieCardListAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.net.URI;
import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private FragmentSearchBinding binding;

    // API KEY
    String API_KEY = "8b466a6b5e68647ae3e550470e2bb324";

    // Search Elements
    EditText searchInput;

    // Searched Movies
    private ArrayList<Movie> searchedMovies;
    private MovieCardListAdapter searchedMoviesAdapter;
    RecyclerView searchedMoviesView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Setup
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Select components
        //
        // Search elements
        searchInput = (EditText) binding.searchInput;
        // Searched Movies Recycler View
        searchedMoviesView = (RecyclerView) binding.SearchedMoviesList;

        // Data
        //
        // Setup
        searchedMovies = new ArrayList<>();

        // Data
        //
        // Display Setup
        // Last Movies View
        searchedMoviesView.setHasFixedSize(true);
        LinearLayoutManager llmSEARCHEDMOVIES = new LinearLayoutManager(this.getContext());
        llmSEARCHEDMOVIES.setOrientation(LinearLayoutManager.VERTICAL);
        searchedMoviesView.setLayoutManager(llmSEARCHEDMOVIES);
        // Create the adapter to convert the array to views
        searchedMoviesAdapter = new MovieCardListAdapter(searchedMovies);
        searchedMoviesView.setAdapter(searchedMoviesAdapter);

        // Listeners
        //
        // When making research
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            // Get the input
            String input = searchInput.getText().toString();
            this.SearchForMovies(this, input);
            return false;
        });

        return root;
    }

    public void SearchForMovies(Fragment context, String searchTerm) {
        // Get the data from the server with Ion
        Ion.with(context)
                .load("https://api.themoviedb.org/3/search/movie?api_key="
                        + API_KEY
                        + "&query="
                        + URI.create(searchTerm)
                )
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
                                Log.i("MOVIE", movie.toString());
                                searchedMovies.add(new Movie(
                                        movie.get("id").getAsString(),
                                        movie.get("title").getAsString(),
                                        movie.get("release_date").getAsString(),
                                        movie.get("poster_path").isJsonNull() ? "": movie.get("poster_path").getAsString(),
                                        movie.get("adult").getAsBoolean(),
                                        movie.get("overview").getAsString(),
                                        movie.get("vote_average").getAsString()
                                ));
                            }
                            // Print the films
                            for (Movie movie : searchedMovies) {
                                Log.i("MainActivity", movie.toString());
                            }

                            searchedMoviesAdapter.notifyDataSetChanged();
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