package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Integer> {
    @Query("select r from Route r where r.distance between ?1 and ?2 and r.from.name = ?3")
    List<Route> findByDistanceAndHub(Integer distanceStart, Integer distanceEnd, String hub);

    List<Route> findByDistanceBetweenAndTo_Name(Integer distanceStart, Integer distanceEnd, String name);

}