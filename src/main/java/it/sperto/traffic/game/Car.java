package it.sperto.traffic.game;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Car {
    private List<Street> streets = new ArrayList<>();
    private int minTimeToComplete = 0;

    public void addStreet(Street street){
        streets.add(street);
        minTimeToComplete += ((street.getTravelTime())+1);
    }

    public List<Street> getStreets() {
        return streets;
    }

    public int getMinTimeToComplete() {
        return minTimeToComplete;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Car.class.getSimpleName() + "[", "]")
                .add("path=" + streets.size())
                .add("tripDuration=" + minTimeToComplete)
                .toString();
    }
}
