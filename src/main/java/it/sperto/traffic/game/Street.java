package it.sperto.traffic.game;

import java.util.*;

public class Street {
    private int travelTime;
    private String name;
    private int greenDuration;
    private float weight = 0.0f;
    private int carPoints = 0;
    List<Car> carWillPass = new ArrayList<>();


    public Street(String name, int travelTime) {
        this.name = name;
        this.travelTime = travelTime;
    }

    public int getCarPoints() {
        return carPoints;
    }

    public void setCarPoints(int carPoints) {
        this.carPoints = carPoints;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getGreenDuration() {
        return greenDuration;
    }

    public void setGreenDuration(int greenDuration) {
        this.greenDuration = greenDuration;
    }

    public List<Car> getCarWillPass() {
        return carWillPass;
    }

    public void addCarWillPass(Car car){
        this.carWillPass.add(car);
    }

    public int getTravelTime() {
        return travelTime;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Street.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("carWillPass=" + carWillPass.size())
                .add("carPoints=" + carPoints)
                .add("weigth=" + weight)
                .add("greenDuration=" + greenDuration)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Street street = (Street) o;
        return Objects.equals(name, street.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
