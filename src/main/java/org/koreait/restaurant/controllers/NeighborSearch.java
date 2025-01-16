package org.koreait.restaurant.controllers;

import lombok.Data;

@Data
public class NeighborSearch {
    private double lat;
    private double lon;
    private int limit;
}
