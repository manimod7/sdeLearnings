package atlassian.round2;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Driver {
    private String driverName;
    private List<Lap> laps;

    public Driver(String driverName, List<Lap> laps) {
        this.driverName = driverName;
        this.laps = laps;
    }

    public long getAverageTimeUntilLastLap() {
        if(laps.isEmpty())
            return 0;
        long totalTime = 0;
        for(int i=0;i<laps.size()-1;i++) {
            Lap lap = laps.get(i);
            totalTime += lap.getEndTime().getNano() - lap.getStartTime().getNano();
        }
        return totalTime / (laps.size()-1);
    }
}
