package atlassian.round2;

import java.util.List;

class LastLapHeroFinder {
    /**
     * @return the name of the driver who shaved off the most time on their last lap,
     *         or null if nobody improved.
     */
    public String findHero(List<Driver> drivers) {
        String bestDriver = null;
        double bestImprovement = 0.0;

        for (Driver d : drivers) {
            double improvement = calculateImprovement(d);
            if (improvement > bestImprovement) {
                bestImprovement = improvement;
                bestDriver = d.getName();
            }
        }

        return bestDriver;
    }

    private double calculateImprovement(Driver driver) {
        List<Double> times = driver.getLapTimes();
        int n = times.size();
        if (n < 2) {
            return 0.0;
        }

        double sum = 0.0;
        for (int i = 0; i < n - 1; i++) {
            sum += times.get(i);
        }
        double avgPrev = sum / (n - 1);
        double lastLap = times.get(n - 1);

        return avgPrev - lastLap;
    }
}

