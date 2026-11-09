package com.AnimeProject.demo;
import org.springframework.stereotype.Service;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Service class responsible for reading anime data and predicting ratings.
@Service 
public class AnimeService {
    // Store all anime records loaded from the CSV file.
    private List<AnimeRecorder> animeList;
    
    // R Model Regression Coefficients (from linear regression analysis)
    private static final double INTERCEPT = 5.948;
    
    // Type coefficients
    private static final Map<String, Double> TYPE_COEFFICIENTS = new HashMap<>();
    static {
        TYPE_COEFFICIENTS.put("Music", -0.4416);
        TYPE_COEFFICIENTS.put("ONA", -0.5804);
        TYPE_COEFFICIENTS.put("OVA", 0.07186);
        TYPE_COEFFICIENTS.put("Special", 0.1190);
        TYPE_COEFFICIENTS.put("TV", 0.2224);
        // Default for other types: 0
    }
    
    // Episode coefficient
    private static final double EPISODES_COEFF = 0.0004963;
    
    // Genre coefficients
    private static final Map<String, Double> GENRE_COEFFICIENTS = new HashMap<>();
    static {
        GENRE_COEFFICIENTS.put("Drama", 0.3885);
        GENRE_COEFFICIENTS.put("Romance", 0.1448);
        GENRE_COEFFICIENTS.put("School", 0.2472);
        GENRE_COEFFICIENTS.put("Supernatural", 0.1812);
        GENRE_COEFFICIENTS.put("Action", 0.06757);
        GENRE_COEFFICIENTS.put("Adventure", 0.1433);
        GENRE_COEFFICIENTS.put("Fantasy", 0.1911);
        GENRE_COEFFICIENTS.put("Magic", 0.09953);
        GENRE_COEFFICIENTS.put("Military", 0.3316);
        GENRE_COEFFICIENTS.put("Shounen", 0.3777);
        GENRE_COEFFICIENTS.put("Comedy", 0.1138);
        GENRE_COEFFICIENTS.put("Historical", 0.2228);
        GENRE_COEFFICIENTS.put("Parody", 0.09868);
        GENRE_COEFFICIENTS.put("Samurai", 0.08792);
        GENRE_COEFFICIENTS.put("Sci-Fi", 0.1208);
        GENRE_COEFFICIENTS.put("Thriller", 0.1806);
        GENRE_COEFFICIENTS.put("Sports", 0.2088);
        GENRE_COEFFICIENTS.put("Super Power", 0.09171);
        GENRE_COEFFICIENTS.put("Space", 0.04446);
        GENRE_COEFFICIENTS.put("Slice of Life", 0.2024);
        GENRE_COEFFICIENTS.put("Mecha", -0.009225);
        GENRE_COEFFICIENTS.put("Music", 0.06512);
        GENRE_COEFFICIENTS.put("Mystery", 0.3863);
        GENRE_COEFFICIENTS.put("Seinen", 0.2543);
        GENRE_COEFFICIENTS.put("Martial Arts", 0.06901);
        GENRE_COEFFICIENTS.put("Vampire", -0.02623);
        GENRE_COEFFICIENTS.put("Shoujo", 0.2029);
        GENRE_COEFFICIENTS.put("Horror", -0.1906);
        GENRE_COEFFICIENTS.put("Police", 0.3674);
        GENRE_COEFFICIENTS.put("Psychological", 0.1754);
        GENRE_COEFFICIENTS.put("Demons", -0.05420);
        GENRE_COEFFICIENTS.put("Ecchi", -0.1167);
        GENRE_COEFFICIENTS.put("Josei", 0.5663);
        GENRE_COEFFICIENTS.put("Shounen Ai", -0.1742);
        GENRE_COEFFICIENTS.put("Game", 0.1005);
        GENRE_COEFFICIENTS.put("Dementia", -1.027);
        GENRE_COEFFICIENTS.put("Harem", 0.04420);
        GENRE_COEFFICIENTS.put("Cars", -0.007647);
        GENRE_COEFFICIENTS.put("Kids", -0.1877);
        GENRE_COEFFICIENTS.put("Shoujo Ai", -0.008111);
    }

    // Constructor initializes the list and loads the CSV data immediately.
    public AnimeService() {
        this.animeList = new ArrayList<>();
        loadCSV();
    }

    // Read the CSV file and convert each row into an AnimeRecorder object.
    private void loadCSV() {
        try (CSVReader reader = new CSVReader(new FileReader("cleaned_anime.csv"))) {
            // Skip the first row because it is the CSV header.
            reader.readNext();

            // Read each CSV row until we reach the end of the file.
            String[] line;
            while ((line = reader.readNext()) != null) {
                AnimeRecorder anime = new AnimeRecorder();
                anime.setGenre(line[0]);
                anime.setType(line[1]);
                try {
                    anime.setEpisodes(Integer.parseInt(line[2]));
                } catch (NumberFormatException e) {
                    anime.setEpisodes(-1);
                }
                try {
                    anime.setRating(Double.parseDouble(line[3]));
                } catch (NumberFormatException e) {
                    anime.setRating(0.0);
                }
                try {
                    anime.setMembers(Integer.parseInt(line[4].replaceAll("[^0-9]", "")));
                } catch (NumberFormatException e) {
                    anime.setMembers(0);
                }
                animeList.add(anime); // ← add LAST, after everything is set
            }

            System.out.println("Loaded anime records");
        } catch (IOException e) {
            System.err.println("Error loading CSV: " + e.getMessage());
        } catch (CsvValidationException e) {
            System.err.println("Error loading CSV: " + e.getMessage());
        }

    }

    // Prediction method using R regression model: given `episodes`, `members`, `genre`, and `type`, 
    // return a predicted rating based on statistical coefficients from linear regression analysis.
    // Formula: rating = intercept + type_coeff + genre_coeff + (episodes * episodes_coeff) + log1p(members) * 0.18
    public double predictRating(int episodes, int members, String genre, String type) {
        System.out.println("DEBUG: episodes=" + episodes + " members=" + members + " genre=" + genre + " type=" + type);

        double prediction = INTERCEPT;

        // Type coefficient
        if (type != null && !type.trim().isEmpty()) {
            double typeCoeff = TYPE_COEFFICIENTS.getOrDefault(type.trim(), 0.0);
            prediction += typeCoeff;
        }

        // Genre coefficients
        if (genre != null && !genre.trim().isEmpty()) {
            String[] genres = genre.split(",");
            for (String g : genres) {
                String trimmedGenre = g.trim();
                double genreCoeff = GENRE_COEFFICIENTS.getOrDefault(trimmedGenre, 0.0);
                prediction += genreCoeff;
            }
        }

        // Episodes contribution
        prediction += episodes * EPISODES_COEFF;

        // Members contribution, scaled gently so high values do not saturate to 10 easily
        int safeMembers = Math.max(1, members);
        prediction += Math.log1p(safeMembers) * 0.18;

        // Clamp to a realistic range
        return Math.max(0.0, Math.min(9.8, prediction));
    }
}
