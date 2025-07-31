package atlassian.round2;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Driver {
    private final String name;
    private final List<Double> lapTimes;

    public Driver(String name, List<Double> lapTimes) {
        this.name = name;
        this.lapTimes = List.copyOf(lapTimes);
    }

}