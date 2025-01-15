package org.koreait.restaurant.services;

import lombok.RequiredArgsConstructor;
import org.koreait.restaurant.controllers.RestaurantSearch;
import org.koreait.restaurant.entities.Restaurant;
import org.koreait.restaurant.repositories.RestaurantRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class RestaurantInfoService {
    private final RestaurantRepository restaurantRepository;

    public Restaurant get(Long seq) {
        return null;
    }

    public List<Restaurant> getList(RestaurantSearch search)  {
        return null;
    }
}
