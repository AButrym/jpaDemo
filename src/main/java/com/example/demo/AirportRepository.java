package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface AirportRepository extends JpaRepository<Airport, Integer> {
    Optional<Airport> findByName(String name);
}
