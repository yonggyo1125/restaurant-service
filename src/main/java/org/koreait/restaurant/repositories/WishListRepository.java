package org.koreait.restaurant.repositories;

import org.koreait.restaurant.entities.WishList;
import org.koreait.restaurant.entities.WishListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface WishListRepository extends JpaRepository<WishList, WishListId>, QuerydslPredicateExecutor<WishList> {

}
