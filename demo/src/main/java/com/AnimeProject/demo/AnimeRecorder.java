package com.AnimeProject.demo;

// This class holds data for one anime record. It is used to store rows loaded from the CSV file.
public class AnimeRecorder {
    // Fields represent the values for each column in the CSV row.
    private String name;
    private int episodes;
    private String genre;
    private int members;
    private double rating;
    private boolean is_highrated;
    // Optional type/category for the anime (e.g., TV, Movie, OVA)
    private String type;

    // Empty constructor: required when creating objects before setting values.
    // This is useful for CSV parsing libraries that build an object first and fill
    // in its fields later.
    public AnimeRecorder() {
    }

    // Full constructor: useful when you want to create an AnimeRecorder with all
    // values at once.
    public AnimeRecorder(int episodes, String genre, int members, double rating, String type) {

        this.episodes = episodes;
        this.genre = genre;
        this.members = members;
        this.rating = rating;

        this.type = type;
    }

    // Getter and setter methods allow other code to read and write each field
    // safely.

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    // Return the number of members as an integer.
    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    // Getter and setter for the optional type field.
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
