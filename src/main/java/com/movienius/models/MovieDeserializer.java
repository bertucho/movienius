package com.movienius.models;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class MovieDeserializer extends JsonDeserializer<Movie> {
	@Autowired
	private MovieRepository movieRep;
	 
    @Override
    public Movie deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
        Movie movie;
        JsonNode node = jp.getCodec().readTree(jp);
        if (node == null){
        	System.out.println("Error node root");
        	return null;
        }
        //get title
        String title = node.findValue("title").asText();
        //movie = movieRep.findByTitle(title);
        //if (movie != null) return movie;
        System.out.println("2");
        //get directors
        List<String> directorsList = node.findValue("directors").findValuesAsText("name");
        String directors = String.join(", ", directorsList);
        System.out.println("2");
        //get description
        String description;
        JsonNode simplePlotNode = node.findValue("simplePlot");
        if (simplePlotNode != null) description = simplePlotNode.asText();
        else description = node.findValue("plot").asText();
        
        //get year
        int year = node.findValue("year").asInt();
        System.out.println("3");
        //get actors
        String actors = String.join(", ", node.findValuesAsText("actorName"));
        
        //get poster_url
        String poster_url = node.findValue("urlPoster").asText();
        System.out.println("4");
        //get rating
        String ratingString = node.findValue("rating").asText().replaceAll(",", ".");
        Double rating = Double.valueOf(ratingString);
        
        //get movie_url
        JsonNode movie_urlNode = node.findValue("videoURL");
        String movie_url = "";
        if (movie_urlNode != null) movie_url = movie_urlNode.asText();
        // parche por error en el resultado de la api
        movie_url = movie_url.toLowerCase().replaceFirst("comvideo", "com/video");;
        System.out.println(movie_url);
        System.out.println("5");
        movie = new Movie(title, movie_url, description, year, directors, actors, poster_url, rating);
        if (movie == null) System.out.println("es null");
        return movie;
    }
}