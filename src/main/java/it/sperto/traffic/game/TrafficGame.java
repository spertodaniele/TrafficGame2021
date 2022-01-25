package it.sperto.traffic.game;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

public class TrafficGame {

    private final Map<String, Street> STREETS_MAP = new HashMap<>();
    private final List<Car> CARS = new ArrayList<>();
    private final Map<Integer, Intersection> INTERSECTIONS_MAP = new HashMap<>();
    private final String RESOURCE_FILE_PATH;
    private int maxTravelTime = 0;
    public static int DURATION = 0;
    private int INTERSECTIONS_COUNT = 0;
    private int STREETS_COUNT = 0;
    private int CARS_COUNT = 0;
    private int BONUS_POINTS = 1;


    public String play() throws Exception {
        List<Intersection> intersections = new ArrayList<Intersection>(INTERSECTIONS_MAP.values());
        for (Intersection intersection : intersections) {
            if (RESOURCE_FILE_PATH.endsWith("f_forever_jammed.txt")){
                intersection.calculateGreenDurationWeight(21,maxTravelTime);
            } else {
                intersection.calculateGreenDurationZeroOrOne(0, maxTravelTime);
            }
        }


        StringBuffer solution = new StringBuffer();
        int intersectionDumpedCount = 0;
        solution.append("TOTAL_DUMPED_PLACE_HOLDER").append(System.lineSeparator());
        for (Intersection intersection : intersections) {
            String solutionDumped = intersection.dumpToSolutionFile();
            if (solutionDumped != null) {
                intersectionDumpedCount++;
                solution.append(solutionDumped);
            }
        }

        String solutionPath = RESOURCE_FILE_PATH + ".out";
        try (PrintWriter out = new PrintWriter(solutionPath, "UTF-8")) {
            out.write(solution.toString().replace("TOTAL_DUMPED_PLACE_HOLDER", Integer.toString(intersectionDumpedCount)));
        }
        return solutionPath;
    }



    public TrafficGame(File inputFile) throws IOException {
        RESOURCE_FILE_PATH = inputFile.getAbsolutePath();
        Car[] carsTmp = null;
        try (Stream<String> stream = Files.lines(Paths.get(inputFile.getAbsolutePath()))) {
            List<String> lines = stream.collect(Collectors.toList());

            //BEGIN INPUT FILE PARSING..
            StringTokenizer tokenizer = new StringTokenizer(lines.get(0), " ");
            DURATION = Integer.parseInt((String) tokenizer.nextElement());
            INTERSECTIONS_COUNT = Integer.parseInt((String) tokenizer.nextElement());
            STREETS_COUNT = Integer.parseInt((String) tokenizer.nextElement());
            CARS_COUNT = Integer.parseInt((String) tokenizer.nextElement());
            BONUS_POINTS = Integer.parseInt((String) tokenizer.nextElement());

            out.println("They are " + DURATION + " seconds to do the works");
            out.println("They are " + INTERSECTIONS_COUNT + " intersections");
            out.println("They are " + STREETS_COUNT + " streets");
            out.println("They are " + CARS_COUNT + " cars");
            out.println("The bonus is " + BONUS_POINTS + " points");

            carsTmp = new Car[CARS_COUNT];

            int carCount = 0;
            for (int i = 1; i < lines.size(); i++) {
                String[] line = lines.get(i).split("\\s+");
                if (isStreetLine(line)) {
                    String name = line[2];
                    int tripTime = Integer.parseInt(line[3]);
                    int startIntersection = Integer.parseInt(line[0]);
                    int endIntersection = Integer.parseInt(line[1]);
                    Street street = new Street(name, tripTime);
                    STREETS_MAP.put(street.getName(), street);

                    Intersection intersectionTmp = INTERSECTIONS_MAP.get(endIntersection);
                    if (intersectionTmp == null) {
                        intersectionTmp = new Intersection(endIntersection);
                        INTERSECTIONS_MAP.put(endIntersection, intersectionTmp);
                    }
                    intersectionTmp.addStreet(street);
                } else {
                    Car car = new Car();
                    for (int j = 1; j < line.length; j++) {
                        car.addStreet(STREETS_MAP.get(line[j]));
                    }
                    carsTmp[carCount++] = car;
                }
            }
        }

        for (Car car : carsTmp) {
            if (car.getMinTimeToComplete() < DURATION) {
                CARS.add(car);
                if (maxTravelTime < car.getMinTimeToComplete()) maxTravelTime = car.getMinTimeToComplete();
            }
        }

        for (Car car : CARS){
            for (Street street: car.getStreets()){
                street.addCarWillPass(car);
            }
        }

    }

    private static boolean isStreetLine(String[] line) {
        if (line.length == 4) {
            try {
                int num = Integer.parseInt(line[1]);
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

}
