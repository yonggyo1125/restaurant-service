package org.koreait.restaurant.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

@Data
@Entity
@IdClass(WishListId.class)
public class WishList {

    @Id
    @Column(length=65, name="_UID")
    private String uid;

    @Id
    private Long seq;
}
