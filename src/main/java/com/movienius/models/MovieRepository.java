package com.movienius.models;

import org.springframework.data.repository.CrudRepository;


public interface MovieRepository extends CrudRepository<Movie, Long> {
	Movie findByTitle(String title);
	Movie findFirstByTitle(String title);
}