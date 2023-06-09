package com.zmanav.dummy.dummyAPI.repository;

import com.zmanav.dummy.dummyAPI.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
