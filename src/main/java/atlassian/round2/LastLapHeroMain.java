package atlassian.round2;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LastLapHeroMain {
    public static void main(String[] args) {
        // Example drivers
        List<Driver> drivers = new ArrayList<>();

        // Driver 1
        List<Lap> laps1 = Arrays.asList(
                new Lap(LocalTime.of(0, 1, 0, 0), LocalTime.of(0, 1, 1, 100000000)),
                new Lap(LocalTime.of(0, 1, 2, 0), LocalTime.of(0, 1, 3, 200000000))
        );
        Driver driver1 = new Driver("Lewis", laps1);

        // Driver 2
        List<Lap> laps2 = Arrays.asList(
                new Lap(LocalTime.of(0, 1, 0, 0), LocalTime.of(0, 1, 1, 500000000)),
                new Lap(LocalTime.of(0, 1, 2, 0), LocalTime.of(0, 1, 2, 900000000))
        );
        Driver driver2 = new Driver("Max", laps2);

        // Driver 3
        List<Lap> laps3 = Arrays.asList(
                new Lap(LocalTime.of(0, 1, 0, 0), LocalTime.of(0, 1, 2, 0)),
                new Lap(LocalTime.of(0, 1, 3, 0), LocalTime.of(0, 1, 4, 0))
        );
        Driver driver3 = new Driver("Charles", laps3);

        drivers.add(driver1);
        drivers.add(driver2);
        drivers.add(driver3);

        ComputationStrategy strategy = new LastLapHeroComputation();
        String lastLapHero = strategy.compute(drivers);
        System.out.println("Last Lap Hero: " + lastLapHero);
    }
}

