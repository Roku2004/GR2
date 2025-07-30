package com.example.catalogservice.repository;

import com.example.catalogservice.model.Catagories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatagoriesRepository extends JpaRepository<Catagories, Integer> {
}
