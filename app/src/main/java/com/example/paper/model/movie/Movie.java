package com.example.paper.model.movie;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String id;
    private String name;
    private String releaseData;
    private String posterPath;
    private Boolean adult;
    private String overview;
    private String grade;

    public Movie(String id, String name, String releaseData, String posterPath, Boolean adult, String overview, String grade) {
        this.id = id;
        this.name = name;
        this.releaseData = releaseData;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.grade = grade;
    }

    // all getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseData() {
        return releaseData;
    }

    public void setReleaseData(String releaseData) {
        this.releaseData = releaseData;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", releaseData='" + releaseData + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", adult=" + adult +
                ", overview='" + overview + '\'' +
                '}';
    }

    // Parcelable

    protected Movie(Parcel in) {
        id = in.readString();
        name = in.readString();
        releaseData = in.readString();
        posterPath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(releaseData);
        parcel.writeString(posterPath);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(overview);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
