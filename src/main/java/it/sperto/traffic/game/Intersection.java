package it.sperto.traffic.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class Intersection {
    public static final String TOTAL_DUMPED_PLACE_HOLDER = "TOTAL_DUMPED_PLACE_HOLDER";
    int id;
    List<Street> streetsSemaphore = new ArrayList<>();


    public Intersection(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void addStreet(Street street) {
        streetsSemaphore.add(street);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intersection that = (Intersection) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Intersection.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("streetsSemaphore=" + streetsSemaphore.size())
                .toString();
    }

    public void calculateGreenDurationCarNumber(final int timeTick, final int maxCarTravelTime) {
        int totalCarsCount = 0;
        float minCarWillPassProportion = 100000000f;

        for (Street semaphore : streetsSemaphore) {
            totalCarsCount += semaphore.getCarWillPass().size();
        }


        for (Street semaphore : streetsSemaphore) {
            float totalCarsCountProportion = (float) semaphore.getCarWillPass().size() / totalCarsCount;
            if (!Float.isNaN(totalCarsCountProportion)) {
                semaphore.setWeight(totalCarsCountProportion);
                if (totalCarsCountProportion > 0 && minCarWillPassProportion > totalCarsCountProportion) {
                    minCarWillPassProportion = totalCarsCountProportion;
                }
            }
        }

        for (Street semaphore : streetsSemaphore) {
            int greenDuration = (int) Math.floor(semaphore.getWeight() / minCarWillPassProportion);
            if (greenDuration > timeTick) {
                greenDuration = timeTick;
            }
            semaphore.setGreenDuration(greenDuration);
        }
    }


    public void calculateGreenDurationZeroOrOne(final int timeTick, final int maxCarTravelTime) {
        int totalCarsCount = 0;
        float minWeight = 100000000f;
        for (Street semaphore : streetsSemaphore) {
            int greenDuration = semaphore.getCarWillPass().size() < 1 ? 0 : 1;
            semaphore.setGreenDuration(greenDuration);
        }
    }


    public void calculateGreenDurationWeight( int timeTick, final int maxCarTravelTime) {
        int totalCarsCount = 0;
        int totalCarsPoint = 0;
        float minWeight = 100000000f;

        for (Street semaphore : streetsSemaphore) {
            totalCarsCount += semaphore.getCarWillPass().size();
            List<Car> cars = semaphore.getCarWillPass();
            int semaphoreCarPoints = 0;
            for (Car car : cars) {
                semaphoreCarPoints += (maxCarTravelTime - car.getMinTimeToComplete());
            }
            totalCarsPoint += semaphoreCarPoints;
            semaphore.setCarPoints(semaphoreCarPoints);
        }

        for (Street semaphore : streetsSemaphore) {
            float totalCarsCountProportion = (float) semaphore.getCarWillPass().size() / totalCarsCount;
            float totalCarsPointsProportion = (float) semaphore.getCarPoints() / totalCarsPoint;
            if (!Float.isNaN(totalCarsCountProportion) && !Float.isNaN(totalCarsPointsProportion)) {
                float weight = (totalCarsCountProportion)+(totalCarsPointsProportion*0.3f);
                semaphore.setWeight(weight);
                if (weight > 0 && minWeight > weight) {
                    minWeight = weight;
                }
            }
        }

        for (Street semaphore : streetsSemaphore) {
            int greenDuration = (int) Math.floor(semaphore.getWeight() / minWeight);
            if (greenDuration > timeTick) {
                greenDuration = timeTick;
            }
            semaphore.setGreenDuration(greenDuration);
        }
    }

    public String dumpToSolutionFile() {
        boolean exit = true;
        for (Street semaphore : streetsSemaphore) {
            if (semaphore.getGreenDuration() > 0) {
                exit = false;
                break;
            }
        }

        if (exit) {
            return null;
        }


        StringBuilder sb = new StringBuilder();
        sb.append(this.id).append(System.lineSeparator());
        sb.append(TOTAL_DUMPED_PLACE_HOLDER).append(System.lineSeparator());
        int intersectionDumpedCount = 0;
        for (Street semaphore : streetsSemaphore) {
            if (semaphore.getGreenDuration() < 1) {
                continue;
            }
            sb.append(semaphore.getName()).append(" ").append(semaphore.getGreenDuration()).append(System.lineSeparator());
            intersectionDumpedCount++;
        }
        return sb.toString().replace(TOTAL_DUMPED_PLACE_HOLDER, Integer.toString(intersectionDumpedCount));
    }
}
