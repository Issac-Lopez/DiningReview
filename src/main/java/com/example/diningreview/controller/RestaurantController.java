Skip to content
        Search or jump to…
        Pulls
        Issues
        Marketplace
        Explore

@Issac-Lopez
        brettjames250
        /
        dining-review
        Public
        Code
        Issues
        Pull requests
        Actions
        Projects
        Wiki
        Security
        Insights
        dining-review/src/main/java/com/example/diningreview/controller/RestaurantController.java /
@brettjames250
brettjames250 Adding restaurant search endpoint and test
        Latest commit eb688fa on Nov 3, 2021
        History
        1 contributor
        44 lines (36 sloc)  1.7 KB

        package com.example.diningreview.controller;

        import com.example.diningreview.model.Restaurant;
        import com.example.diningreview.repositories.RestaurantRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.server.ResponseStatusException;

        import java.util.Optional;

@RestController
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/restaurants")
    public Iterable<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @GetMapping("/restaurants/search")
    public Optional<Restaurant> getRestaurantsByPostcode(@RequestParam String postcode) {
        return restaurantRepository.findByPostcode(postcode);
    }

    @GetMapping("/restaurants/{id}")
    public Optional<Restaurant> getRestaurantBy(@PathVariable("id") Long id) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        if (restaurantOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This restaurant does not exist");
        }
        return restaurantRepository.findById(id);
    }

    @PostMapping("/restaurants")
    public Restaurant createNewRestaurant(@RequestBody Restaurant restaurant) {
        if (restaurantRepository.existsByPostcode(restaurant.getPostcode()) && restaurantRepository.existsByName(restaurant.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A restaurant with this name and postcode already exists");
        }
        return restaurantRepository.save(restaurant);
    }
}
© 2022 GitHub, Inc.
        Terms
        Privacy
        Security
        Status
        Docs
        Contact GitHub
        Pricing
        API
        Training
        Blog
        About
        Loading complete