package com.example.paper.ui.movie_page;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.paper.R;
import com.example.paper.model.movie.Movie;
import com.example.paper.model.movie.StremingProvider;
import com.google.gson.JsonArray;
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
    TextView v_runtime;
    TextView v_overwiew;
    TextView v_release_date;
    TextView v_genres;
    TextView v_adult;
    TextView v_critics;

    ImageView returnArrow;

    // API KEY
    String API_KEY = "8b466a6b5e68647ae3e550470e2bb324";

    ArrayList<StremingProvider> providers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_page);


        // Hide action bar
        getSupportActionBar().hide();

        // Init view components
        v_poster = findViewById(R.id.poster_image);
        v_title = findViewById(R.id.movie_title);
        v_runtime = findViewById(R.id.runtime);
        v_overwiew = findViewById(R.id.overview);
        v_adult = findViewById(R.id.adult);

        returnArrow = findViewById(R.id.returnArrow);

        // Listeners
        returnArrow.setOnClickListener((v) -> {
            // Return
            finish();
        });

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

        // Set simple datas
        v_title.setText(movie.getName());
        v_overwiew.setText(movie.getOverview());
        if (movie.getAdult()) {
            v_adult.setText("Atttention : Ce film est réservé à un public avertit");
        }else{
            v_adult.setText("Film tout publique");
        }

        this.SetMovieProviders(this.getBaseContext(), findViewById(R.id.list_providers), movie.getId());
        this.SetMovieDetails(this.getBaseContext(), this, movie.getId());
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

    public void SetMovieDetails(Context context, MoviePageActivity activity, String movie_id) {
        String API_KEY = "8b466a6b5e68647ae3e550470e2bb324"; // getResources().getString(R.string.api_key);

        v_runtime = activity.findViewById(R.id.runtime);
        v_genres = activity.findViewById(R.id.genres);
        v_release_date = activity.findViewById(R.id.releasedate);

        // Get the data from the server with Ion
        Ion.with(context)
                .load("https://api.themoviedb.org/3/movie/"+ movie_id +"?api_key=" + API_KEY)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.e("MainActivity", "Error: " + e.getMessage());
                        } else {
                            // Runtime
                            Integer runtime = result.get("runtime").getAsInt();
                            Integer hours = runtime / 60;
                            Integer minutes = runtime % 60;

                            v_runtime.setText(hours+":"+minutes);


                            JsonArray genres = result.getAsJsonArray("genres");

                            genres.forEach((elmnt) -> {
                                if (!v_genres.getText().equals("")) {
                                    v_genres.setText(v_genres.getText() + ", " + elmnt.getAsJsonObject().get("name").toString());
                                }else{
                                    v_genres.setText(elmnt.getAsJsonObject().get("name").toString());
                                }
                            });

                            String realese_date = result.get("release_date").getAsString();
                            String[] date = realese_date.split("-");
                            v_release_date.setText("Sortie le " + date[2] + "/" + date[1] + "/" + date[0]);


                        }
                    }
                });
    }

}
