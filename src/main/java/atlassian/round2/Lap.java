package atlassian.round2;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class Lap {
    private LocalTime startTime;
    private LocalTime endTime;

    public Lap(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
