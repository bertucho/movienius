package com.movienius.apiclient;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.movienius.models.Movie;
import com.movienius.models.MovieDeserializer;

public class MovieApiClient {
	private String apiUrl = "http://www.myapifilms.com/imdb/idIMDB";
	private String apiKey = "api-key";
	
	public Movie requestMyApiFilms(String title){
		RestTemplate restTemplate = new RestTemplate();
		String url = getFullRequestUrl(title);
		String json = restTemplate.getForObject(url, String.class);
		if (json == null){
			System.out.println("Error getting json response");
			return null;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Movie.class, new MovieDeserializer());
		mapper.registerModule(module);
		Movie movie = null;
		try{
			movie = mapper.readValue(json, Movie.class);
		}catch(Exception e){
			System.out.println("Error parsing json");
			System.out.println(e.getMessage());
		}
		return movie;
	}
	
	public String getFullRequestUrl(String title){
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
		        .queryParam("token", this.apiKey)
		        .queryParam("title", title)
		        .queryParam("format", "json")
		        .queryParam("language", "es-es")
		        .queryParam("aka", "0")
		        .queryParam("business", "0")
		        .queryParam("seasons", "0")
		        .queryParam("seasonsYear", "0")
		        .queryParam("technical", "0")
		        .queryParam("filter", "3")
		        .queryParam("exactFilter", "0")
		        .queryParam("limit", "1")
		        .queryParam("forceYear", "0")
		        .queryParam("trailers", "1")
		        .queryParam("movieTrivia", "0")
		        .queryParam("awards", "0")
		        .queryParam("moviePhotos", "0")
		        .queryParam("movieVideos", "0")
		        .queryParam("actors", "1")
		        .queryParam("biography", "0")
		        .queryParam("uniqueName", "0")
		        .queryParam("filmography", "0")
		        .queryParam("bornAndDead", "0")
		        .queryParam("starSign", "0")
		        .queryParam("actorActress", "0")
		        .queryParam("actorTrivia", "0")
		        .queryParam("similarMovies", "0")
		        .queryParam("adultSearch", "0")
		        .queryParam("goofs", "0")
		        .queryParam("quotes", "0")
		        .queryParam("fullSize", "0")
		        .queryParam("companyCredits", "0");
		String finalUrl = builder.build().toUriString();
		System.out.println("Api url: " + finalUrl);
		return finalUrl;
	}
}
