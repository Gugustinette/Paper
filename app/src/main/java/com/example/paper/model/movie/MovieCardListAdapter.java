package com.example.paper.model.movie;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.paper.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieCardListAdapter extends RecyclerView.Adapter<MovieCardListAdapter.MyViewHolder> {

    private List<Movie> movieList;

    /**
     * View holder class
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView moviePoster;

        public MyViewHolder(View view) {
            super(view);
            moviePoster = (ImageView) view.findViewById(R.id.moviePoster);
        }
    }

    public MovieCardListAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // System.out.println("Bind ["+holder+"] - Pos ["+position+"]");
        Movie movie = movieList.get(position);
        ImageView moviePoster = holder.moviePoster;
        // Populate the data into the template view using the data object
        Uri imagePath = Uri.parse(movie.getPosterPath());
        Picasso.get()
                .load("https://image.tmdb.org//t/p/w300_and_h450_bestv2/"+imagePath)
                .into(moviePoster, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(moviePoster.getContext(), "Image introuvable", Toast.LENGTH_SHORT).show();;
                    }
                });
    }

    @Override
    public int getItemCount() {
        // Log.d("RV", "Item size ["+movieList.size()+"]");
        return movieList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card_item,parent, false);
        return new MyViewHolder(v);
    }
}

/*
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
        Picasso.get()
                .load("https://image.tmdb.org//t/p/w300_and_h450_bestv2/"+imagePath)
                .into(movie_poster, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(), "Image introuvable", Toast.LENGTH_SHORT).show();;
                    }
                });
        movie_title.setText(movie.getName());
        movie_grade.setText(movie.getGrade() + "/10");

        // Return the completed view to render on screen
        return movieView;

    }
}
*/
