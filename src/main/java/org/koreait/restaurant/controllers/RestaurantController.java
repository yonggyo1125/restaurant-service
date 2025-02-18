package org.koreait.restaurant.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.global.paging.ListData;
import org.koreait.restaurant.entities.Restaurant;
import org.koreait.restaurant.exceptions.RestaurantNotFoundException;
import org.koreait.restaurant.services.RestaurantInfoService;
import org.koreait.restaurant.services.RestaurantSearchService;
import org.koreait.restaurant.services.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantInfoService infoService;
    private final RestaurantSearchService searchService;
    private final WishService wishService;

    @GetMapping("/info/{seq}")
    public Restaurant info(@PathVariable("seq") Long seq) {
        return infoService.get(seq);
    }

    @GetMapping("/list")
    public ListData<Restaurant> list(RestaurantSearch search) {
        return infoService.getList(search);
    }

    @GetMapping("/category")
    public List<String> categories() {
        return infoService.getCategories();
    }

    /**
     * 좌표로 근처에 있는 식당 조회
     *
     * @return
     */
    @GetMapping("/search")
    public ListData<Restaurant> search(@ModelAttribute NeighborSearch search) {
        List<Long> seq = searchService.search(search);
        if (seq == null || seq.isEmpty()) {
            return new ListData<>();
        }

        search.setSeq(seq);
        return infoService.getList(search);
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<Void> errorHandler() {

        return ResponseEntity.notFound().build();
    }

    /**
     * 찜하기 토글
     *
     * @param uid
     * @param seq
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/wish/{uid}/{seq}")
    public void updateWish(@PathVariable("uid") String uid, @PathVariable("seq") Long seq) {
        wishService.process(uid, seq);
    }

    @GetMapping("/wish/my/{uid}")
    public ListData<Restaurant> getMyWish(@PathVariable("uid") String uid, @ModelAttribute RestaurantSearch search) {
       return wishService.getMyWish(uid, search);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/wish/truncate/{uid}")
    public void truncate(@PathVariable("uid") String uid) {
        wishService.truncate(uid);
    }
}
