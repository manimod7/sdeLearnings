package atlassian;

import java.util.List;

public class LastLapHeroComputation implements ComputationStrategy {
    @Override
    public String  compute(List<Driver> driverList) {
        String lastLapHero = "No driver";
        if(driverList.isEmpty())
            return lastLapHero;
        long minTime = Long.MAX_VALUE;
        for(Driver driver : driverList) {
            if(driver.getLaps().isEmpty())
                continue;
            Lap lastLap = driver.getLaps().get(driver.getLaps().size() - 1);
            long timeTakenForLastLap = lastLap.getEndTime().getNano() - lastLap.getStartTime().getNano();
            if(timeTakenForLastLap - driver.getAverageTimeUntilLastLap() < minTime) {
                minTime = timeTakenForLastLap - driver.getAverageTimeUntilLastLap();
                lastLapHero = driver.getDriverName();
            }

        }
        return lastLapHero;
    }
}
