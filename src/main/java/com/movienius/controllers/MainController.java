package com.movienius.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.movienius.apiclient.MovieApiClient;
import com.movienius.models.Movie;
import com.movienius.models.MovieRepository;

@Controller
public class MainController {
	
	@Autowired
	MovieRepository movieRep;
	
	@RequestMapping("/redirect")
	public ModelAndView index() {
		return new ModelAndView("index");
	}

	@RequestMapping("/login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@RequestMapping("/home")
	public ModelAndView home() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		ModelAndView model = new ModelAndView("home").addObject("name", name);
		return model;
	}

	@RequestMapping("/")
	public ModelAndView userManager() {
		return new ModelAndView("index");
	}
	
	@RequestMapping(value = "/movies-html", method = RequestMethod.GET)
	public String moviesHtml(Model model) {
	    model.addAttribute("movies", this.movieRep.findAll());
	    
	    return "movies :: moviesList";
	}
	
	@RequestMapping(value = "/movies/get/{title}", method = RequestMethod.GET)
	public String getAndCreateMovie(Model model,
									@PathVariable String title,
									@RequestParam(value="force", required=false) Boolean force){
		Movie movie = this.movieRep.findByTitle(title);
		if (movie != null && (force == null || !force)){
			model.addAttribute("movie", movie);
			return "movies :: movie-display";
		}
		MovieApiClient apiClient = new MovieApiClient();
		movie = apiClient.requestMyApiFilms(title);
		Movie existingMovie = this.movieRep.findFirstByTitle(movie.getTitle());
		if (existingMovie != null){
			model.addAttribute("movie", existingMovie);
			return "movies :: movie-display";
		}
		this.movieRep.save(movie);
		model.addAttribute("movie", movie);
		return "movie :: movie-display";
	}
	
	@RequestMapping(value = "/movies/movie-template/{id}", method = RequestMethod.GET)
	public String showMovie(Model model,
							@PathVariable long id){
		Movie movie = this.movieRep.findOne(id);
		if (movie == null) throw new MovieNotFoundException();
		model.addAttribute("movie", movie);
		return "movie :: movie-display";
	}
	
	@RequestMapping(value = "/movies/movie-form/{id}", method = RequestMethod.GET)
	public String getMovieForm(Model model,
							   @PathVariable long id){
		Movie movie = this.movieRep.findOne(id);
		if (movie == null) throw new MovieNotFoundException();
		model.addAttribute("movie", movie);
		return "movie :: movie-form";
	}
	
}