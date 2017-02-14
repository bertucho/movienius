package com.movienius.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.movienius.apiclient.MovieApiClient;
import com.movienius.models.Movie;
import com.movienius.models.MovieRepository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class MovieController {
	@Autowired
	private MovieRepository movieRep;
	
	@RequestMapping(value = "/movies", method = RequestMethod.GET, produces = "application/json")
	public Iterable<Movie> moviesList(){
		return this.movieRep.findAll();
	}
	
	@RequestMapping(value = "/movies/{id}", method = RequestMethod.GET, produces = "application/json")
	public Movie showMovie(@PathVariable long id){
		Movie movie = this.movieRep.findOne(id);
		if (movie == null) throw new MovieNotFoundException();
		return movie;
	}
	
	@RequestMapping(value = "/movies/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Movie putMovie(@PathVariable long id,
						@RequestParam(value = "title", required=false) String title,
						@RequestParam(value = "movie_url", required=false) String movie_url,
						@RequestParam(value = "description", required=false) String description,
						@RequestParam(value = "year", required=false) Integer year,
						@RequestParam(value = "director", required=false) String director,
						@RequestParam(value = "actors", required=false) String actors,
						@RequestParam(value = "poster_url", required=false) String poster_url,
						@RequestParam(value = "rating", required=false) Double rating){
		Movie movie = this.movieRep.findOne(id);
		if (movie == null) throw new MovieNotFoundException();
		if(title != null) movie.setTitle(title);
		if(movie_url != null) movie.setMovie_url(movie_url);
		if(director != null) movie.setDescription(description);
		if(year != null) movie.setYear(year);
		if(director != null) movie.setDirector(director);
		if(actors != null) movie.setActors(actors);
		if(poster_url != null) movie.setPoster_url(poster_url);
		if(rating != null) movie.setRating(rating);
		this.movieRep.save(movie);
		return movie;
	}
	
	@RequestMapping(value = "/movies/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteMovie(@PathVariable long id){
		Movie movie = this.movieRep.findOne(id);
		if (movie == null) throw new MovieNotFoundException();
		this.movieRep.delete(movie);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
	
	@RequestMapping(value = "/movies", method = RequestMethod.POST)
	public Movie addMovie(@RequestParam(value = "title") String title,
						@RequestParam(value = "movie_url") String movie_url){
		if (this.movieRep.findByTitle(title) != null) throw new MovieConflictException(title);
		
		Movie movie = new Movie(title, movie_url);
		this.movieRep.save(movie);
		return movie;
	}
}
