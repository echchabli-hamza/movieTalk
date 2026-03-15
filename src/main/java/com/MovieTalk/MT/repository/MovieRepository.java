package com.MovieTalk.MT.repository;

import com.MovieTalk.MT.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByCategoryId(Long categoryId);
    
    // For trending movies - by popularity score
    List<Movie> findTop10ByOrderByPopularityScoreDesc();
    
    // For new releases - by release year
    List<Movie> findTop10ByOrderByReleaseYearDesc();
    
    // For top rated movies - by rating
    List<Movie> findTop10ByOrderByRatingDesc();
    
    // For featured movies - high rating and popularity
    @Query("SELECT m FROM Movie m WHERE m.rating >= 4.0 AND m.popularityScore >= 7.0 ORDER BY (m.rating + m.popularityScore) DESC")
    List<Movie> findFeaturedMovies();
    
    // For search - by title, director, or actors
    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(m.director) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(m.actors) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Movie> searchByTitleDirectorOrActors(@Param("query") String query);
}
