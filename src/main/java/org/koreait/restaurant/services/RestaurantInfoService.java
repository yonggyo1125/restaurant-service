package org.koreait.restaurant.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.global.paging.ListData;
import org.koreait.global.paging.Pagination;
import org.koreait.restaurant.controllers.RestaurantSearch;
import org.koreait.restaurant.entities.QRestaurant;
import org.koreait.restaurant.entities.Restaurant;
import org.koreait.restaurant.exceptions.RestaurantNotFoundException;
import org.koreait.restaurant.repositories.RestaurantRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;

@Lazy
@Service
@RequiredArgsConstructor
public class RestaurantInfoService {
    private final RestaurantRepository restaurantRepository;
    private final HttpServletRequest request;
    private final JPAQueryFactory queryFactory;

    public Restaurant get(Long seq) {
        return restaurantRepository.findById(seq).orElseThrow(RestaurantNotFoundException::new);
    }

    /**
     * 식당목록 조회
     *
     * @param search
     * @return
     */
    public ListData<Restaurant> getList(RestaurantSearch search)  {
        QRestaurant restaurant = QRestaurant.restaurant;

        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 50 : limit;
        String sido = search.getSido();
        String sigugun = search.getSigugun();
        String name = search.getName();


        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(sido)) {
            builder.and(restaurant.address.startsWith(sido));

            if (StringUtils.hasText(sigugun)) { // sido가 있을때만 시구군 검색
                builder.and(restaurant.address.contains(sigugun));
            }
        }

        if (StringUtils.hasText(name)) {
            builder.and(restaurant.name.contains(name.trim()));
        }


        List<String> category = search.getCategory();
        if (category != null && !category.isEmpty()) {
            builder.and(restaurant.category.in(category));
        }

        List<Long> seqs = search.getSeq();
        if (seqs != null && !seqs.isEmpty()) {
            builder.and(restaurant.seq.in(seqs));
        }

        String sort = search.getSort();
        Sort s = Sort.by(asc("name"));
        if (StringUtils.hasText(sort)) {
            String[] _sort = sort.split("_");
            String field = _sort[0];
            String direction = _sort[1];

            if (field.equals("name")) s = direction.equals("DESC") ? Sort.by(desc("name")) : Sort.by(asc("name"));
            else if (field.equals("address")) s = direction.equals("DESC") ? Sort.by(desc("address")) : Sort.by(asc("address"));
        }

        Pageable pageable = PageRequest.of(page - 1, limit, s);
        Page<Restaurant> data = restaurantRepository.findAll(builder, pageable);
        List<Restaurant> items = data.getContent();
        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), 5, limit, request);

        return new ListData<>(items, pagination);

    }

    public List<String> getCategories() {
        QRestaurant restaurant = QRestaurant.restaurant;
        List<String> items = queryFactory.select(restaurant.category).distinct()
                .from(restaurant)
                .where(restaurant.category.isNotNull())
                .orderBy(restaurant.category.asc())
                .fetch();

        return items;
    }
}
