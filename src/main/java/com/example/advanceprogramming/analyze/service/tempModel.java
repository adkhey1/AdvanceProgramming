package com.example.advanceprogramming.analyze.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class tempModel {

    private String bID;
    private double revSpring, revSummer, revFall, revWinter, sentSpring, sentSummer, sentFall, sentWinter;
    private int springCounter, summerCounter, fallCounter, winterCounter;

    public tempModel(String input) {
        bID = input;
        revSpring = 0.0;
        revSummer = 0.0;
        revFall = 0.0;
        revWinter = 0.0;
        sentSpring = 0.0;
        sentSummer = 0.0;
        sentFall = 0.0;
        sentWinter = 0.0;
        springCounter = 0;
        summerCounter = 0;
        fallCounter = 0;
        winterCounter = 0;
    }

    @Override
    public boolean equals(Object obj) {
        tempModel vergleich = (tempModel) obj;
        return this.bID.equals(vergleich.getBID());
    }

    public void addScore(int month, double review, double sentiment) {
        switch (month) {
            case 1, 2, 3:
                if (springCounter != 0) {
                    revSpring = revSpring * springCounter;
                    sentSpring = sentSpring * springCounter;
                }
                springCounter++;
                revSpring = (revSpring + review) / springCounter;
                sentSpring = (sentSpring + sentiment) / springCounter;
                break;
            case 4, 5, 6:
                if (summerCounter != 0) {
                    revSummer = revSummer * summerCounter;
                    sentSummer = sentSummer * summerCounter;
                }
                summerCounter++;
                revSummer = (revSummer + review) / summerCounter;
                sentSummer = (sentSummer + sentiment) / summerCounter;
                break;
            case 7, 8, 9:
                if (fallCounter != 0) {
                    revFall = revFall * fallCounter;
                    sentFall = sentFall * fallCounter;
                }
                fallCounter++;
                revFall = (revFall + review) / fallCounter;
                sentFall = (sentFall + sentiment) / fallCounter;
                break;
            case 10, 11, 12:
                if (winterCounter != 0) {
                    revWinter = revWinter * winterCounter;
                    sentWinter = sentWinter * winterCounter;
                }
                winterCounter++;
                revWinter = (revWinter + review) / winterCounter;
                sentWinter = (sentWinter + sentiment) / winterCounter;
                break;
        }

    }
}
