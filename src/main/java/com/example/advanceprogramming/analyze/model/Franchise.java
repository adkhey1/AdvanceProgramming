package com.example.advanceprogramming.analyze.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Franchise {
    STARBUCKS("Starbucks"),
    MCDONALDS("McDonald's"),
    DUNKIN("Dunkin'"),
    SUBWAY("Subway"),
    TACO_BELL("Taco Bell"),
    CVS_PHARMACY("CVS Pharmacy"),
    WALGREENS("Walgreens"),
    BURGER_KING("Burger King"),
    WENDYS("Wendy's"),
    WAWA("Wawa"),
    DOMINOS_PIZZA("Domino's Pizza");

    private final String name;



    //TODO Fill others
}
