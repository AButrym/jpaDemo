package com.example.demo;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j(topic = "MAIN")
@SpringBootApplication
public class Demo9Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo9Application.class, args);
    }

    @Bean
    ApplicationRunner runner(
            AirportRepository airportRepository,
            RouteRepository routeRepository,
            AirportService airportService
            ) {
        return args -> {
            Airport airport1 = Airport.builder()
                    .name("Berlin").build();
            var airport2 = Airport.builder()
                    .name("Kyiv").build();
            airportRepository.saveAll(List.of(airport1, airport2));
            log.info("{} Airports saved", airportRepository.count());
            airportRepository.findAll(Sort.by("name").descending())
                    .forEach(a -> log.info("airoport = {}", a));
            log.info("findByName(Berlin) = {}", airportRepository.findByName("Berlin"));
            var route1 = Route.builder()
                    .from(airport1)
                    .to(airport2)
                    .distance(1000)
                    .build();
            var route2 = Route.builder()
                    .from(airport1)
                    .to(airport2)
                    .distance(1000)
                    .build();
            var route3 = Route.builder()
                    .from(airport2)
                    .to(airport1)
                    .distance(1000)
                    .build();

            routeRepository.saveAll(List.of(route1, route2, route3));
            airportService.loadAndShowKyiv();
            log.info("Success");
        };
    }

}

@Slf4j
@AllArgsConstructor
@Service
class AirportService {
    AirportRepository airportRepository;

    @Transactional(readOnly = true)
    public void loadAndShowKyiv() {
        Airport kyiv = airportRepository.findByName("Kyiv")
                .orElseThrow();
        log.info("Trying to get arrivals");
        log.info("Arrivals to Kyiv: {}", kyiv.getInRoutes());
    }
}

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AIRPORTS")
class Airport {
    @Id
    String name;

    @OneToMany(mappedBy = "from", orphanRemoval = true)
    private Set<Route> outRoutes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "to", orphanRemoval = true)
    private Set<Route> inRoutes = new LinkedHashSet<>();

    @Override
    public String toString() {
        return "Airport{" +
               "name='" + name + '\'' +
               '}';
    }
}

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
class Route {
    @Id
    @GeneratedValue
    Integer id;
    @ManyToOne
    @JoinColumn(name = "from_name")
    Airport from;
    @ManyToOne
    @JoinColumn(name = "to_name")
    Airport to;
    Integer distance;

    @Override
    public String toString() {
        return "Route{" +
               "id=" + id +
               ", from=" + from.getName() +
               ", to=" + to.getName() +
               '}';
    }
}

