package com.example.catalogservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "catalog_movie")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CatalogMovie {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogid", nullable = false)
    private Catagories catagories;

    @Column(name = "movieid", nullable = false)
    private int movieid;
}
