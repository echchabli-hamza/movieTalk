package com.MovieTalk.MT.dto;

import com.MovieTalk.MT.entity.Movie;
import lombok.Data;

@Data
public class MovieDTO {

    private Long id;
    private String title;
    private String synopsis;
    private Integer releaseYear;
    private String director;
    private String actors;
    private Double popularityScore;
    private String imagePath;
    private Double rating;

    private Long categoryId;
    private String categoryName;

    private int ratingCount;
    private int favoriteCount;

    public static MovieDTO fromEntity(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setSynopsis(movie.getSynopsis());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setDirector(movie.getDirector());
        dto.setActors(movie.getActors());
        dto.setPopularityScore(movie.getPopularityScore());
        dto.setImagePath(movie.getImagePath());
        dto.setRating(movie.getRating());

        if (movie.getCategory() != null) {
            dto.setCategoryId(movie.getCategory().getId());
            dto.setCategoryName(movie.getCategory().getName());
        }

        if (movie.getRatings() != null) {
            dto.setRatingCount(movie.getRatings().size());
        }
        if (movie.getFavorites() != null) {
            dto.setFavoriteCount(movie.getFavorites().size());
        }

        return dto;
    }
}
