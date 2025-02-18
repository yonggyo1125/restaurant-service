package org.koreait.restaurant.services;

import lombok.RequiredArgsConstructor;
import org.koreait.global.paging.ListData;
import org.koreait.restaurant.controllers.RestaurantSearch;
import org.koreait.restaurant.entities.QWishList;
import org.koreait.restaurant.entities.Restaurant;
import org.koreait.restaurant.entities.WishList;
import org.koreait.restaurant.entities.WishListId;
import org.koreait.restaurant.repositories.WishListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishService {
    private final WishListRepository repository;
    private final RestaurantInfoService infoService;

    /**
     * 찜하기 추가 또는 제거
     *
     * @param uid
     * @param seq
     */
    public void process(String uid, Long seq) {
        WishListId wishListId = new WishListId(uid, seq);
        if (repository.existsById(wishListId)) { // 제거
            repository.deleteById(wishListId);
        } else { // 추가
            WishList wishList = new WishList();
            wishList.setUid(uid);
            wishList.setSeq(seq);
            repository.save(wishList);
        }

        repository.flush();
    }

    /**
     * 내가 찜한 식당 목록 조회
     *
     * @param search
     * @return
     */
    public ListData<Restaurant> getMyWish(String uid, RestaurantSearch search) {
        QWishList wishList = QWishList.wishList;
        List<WishList> wishes = (List<WishList>)repository.findAll(wishList.uid.eq(uid));
        if (wishes.isEmpty()) {
            return new ListData<>();
        }

        List<Long> seqs = wishes.stream().map(WishList::getSeq).toList();
        search.setSeq(seqs);

        return infoService.getList(search);
    }

    /**
     * 찜한 식당 모두 삭제
     *
     */
    public void truncate(String uid) {
        QWishList wishList = QWishList.wishList;
        List<WishList> wishes = (List<WishList>)repository.findAll(wishList.uid.eq(uid));
        repository.deleteAll(wishes);
        repository.flush();
    }
}
