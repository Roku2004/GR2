package com.example.catalogservice.repository;

import com.example.catalogservice.model.CatalogMovie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogMovieRepository extends JpaRepository<CatalogMovie, Integer> {
}
