package org.koreait.restaurant.controllers;

import lombok.Data;

@Data
public class NeighborSearch {
    private long lat;
    private long lon;
    private int limit;
}
