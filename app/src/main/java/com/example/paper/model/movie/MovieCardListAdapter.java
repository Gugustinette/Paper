package com.example.paper.model.movie;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.paper.R;
import com.example.paper.model.movie.Movie;

import java.net.URI;
import java.util.ArrayList;

public class MovieCardListAdapter  extends ArrayAdapter<Movie> {
    public MovieCardListAdapter(Context context, ArrayList<Movie> objects) {
        super(context, R.layout.movie_card_item, objects);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Setup
        Context context = getContext();
        final Movie movie = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View movieView = inflater.inflate(R.layout.movie_card_item, parent, false);

        // Lookup view for data population
        ImageView movie_poster = (ImageView) movieView.findViewById(R.id.moviePoster);
        TextView movie_title = (TextView) movieView.findViewById(R.id.movieTitle);
        TextView movie_grade = (TextView) movieView.findViewById(R.id.movieGrade);

        // Populate the data into the template view using the data object
        Uri imagePath = Uri.parse(movie.getPosterPath());
        movie_poster.setImageURI(imagePath);
        movie_title.setText(movie.getName());
        movie_grade.setText(movie.getName());

        // Return the completed view to render on screen
        return movieView;

    }
}
