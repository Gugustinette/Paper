package com.example.paper.ui.movie_page;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.paper.R;
import com.example.paper.model.movie.Movie;
import com.example.paper.model.movie.StremingProvider;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviePageActivity extends AppCompatActivity {
    ImageView v_poster;
    TextView v_title;

    TextView v_illustrations;

    TextView v_overwiew;

    TextView v_genres;

    TextView v_duration;

    TextView v_critics;


    ArrayList<StremingProvider> providers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_page);

        // Init view components
        v_poster = findViewById(R.id.poster_image);
        v_title = findViewById(R.id.movie_title);
        // Try catch to handle when the pass is null
        Movie movie = (Movie) getIntent().getExtras().get("movie");
        try {
            // Set date in view components
            Uri imagePath = Uri.parse(movie.getPosterPath());
            Picasso.get()
                    .load(imagePath)
                    .into(v_poster, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        v_title.setText(movie.getName());

        this.SetMovieProviders(this.getBaseContext(), findViewById(R.id.list_providers), movie.getId());
    }

    public void SetMovieProviders(Context context, TextView providers, String movie_id) {
        String API_KEY = "8b466a6b5e68647ae3e550470e2bb324"; // getResources().getString(R.string.api_key);

        // Get the data from the server with Ion
        Ion.with(context)
                .load("https://api.themoviedb.org/3/movie/"+ movie_id +"/watch/providers?api_key=" + API_KEY)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.e("MainActivity", "Error: " + e.getMessage());
                        } else {
                            Log.i("MainActivity", "Success: " + result.toString());
                            JsonObject results = result.getAsJsonObject("results");
                            JsonObject french_providers = results.getAsJsonObject("FR");

                            // For flatrate providers
                            try {
                            JsonArray french_flatrate_providers = french_providers.getAsJsonArray("flatrate");

                                providers.append("\nEn streaming sur : ");
                                for (int i = 0; i < french_flatrate_providers.size(); i++) {
                                    JsonObject curr_provider = french_flatrate_providers.get(i).getAsJsonObject();
                                    providers.append(curr_provider.get("provider_name").getAsString() + " ");
                                }


                            } catch (Exception exception) {
                                //exception.printStackTrace();
                            }

                            // For buyer providers
                            try {
                            JsonArray french_buyer_providers = french_providers.getAsJsonArray("buy");

                                providers.append("\nEn vente sur : ");
                                for (int i = 0; i < french_buyer_providers.size(); i++) {
                                    JsonObject curr_provider = french_buyer_providers.get(i).getAsJsonObject();
                                    providers.append(curr_provider.get("provider_name").getAsString() + " ");
                                }


                            } catch (Exception exception) {
                                //exception.printStackTrace();
                            }

                            // For VOD providers
                            try {

                            JsonArray french_rent_providers = french_providers.getAsJsonArray("rent");

                                providers.append("\nEn VOD sur : ");
                                for (int i = 0; i < french_rent_providers.size(); i++) {
                                    JsonObject curr_provider = french_rent_providers.get(i).getAsJsonObject();
                                    providers.append(curr_provider.get("provider_name").getAsString() + " ");
                                }


                            } catch (Exception exception) {
                                //exception.printStackTrace();
                            }


                        }
                    }
                });
    }

}
